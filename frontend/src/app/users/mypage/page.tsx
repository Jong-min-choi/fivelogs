"use client";
import { useState, useEffect } from "react";
import Layout from "@/app/ClientLayout";
import Link from "next/link";
import Image from "next/image";

interface MyPageDto {
  introduce: string;
  nickname: string;
  email: string;
  blogTitle: string;
  githubLink: string;
  instagramLink: string;
  twitterLink: string;
  profileImageUrl: string;
}

interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
}

export default function MyPage() {
  const [nickname, setNickname] = useState("사용자님");
  const [profileImage, setProfileImage] = useState("");
  const [profileImageFile, setProfileImageFile] = useState<File | null>(null);
  const [introduction, setIntroduction] = useState("소개글이 없습니다.");
  const [blogTitle, setBlogTitle] = useState("블로그 제목이 없습니다.");
  const [isEditingBlogTitle, setIsEditingBlogTitle] = useState(false);
  const [tempBlogTitle, setTempBlogTitle] = useState("");
  const [email, setEmail] = useState("");
  const [githubLink, setGithubLink] = useState("");
  const [instagramLink, setInstagramLink] = useState("");
  const [twitterLink, setTwitterLink] = useState("");
  const [hasSNSLinks, setHasSNSLinks] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    fetchMyPageData();
  }, []);

  const fetchMyPageData = async () => {
    try {
      setIsLoading(true);
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/users/me/mypage`,
        {
          method: "GET",
          credentials: "include",
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      if (!response.ok) {
        throw new Error(`서버 응답 오류: ${response.status}`);
      }

      const data = (await response.json()) as ApiResponse<MyPageDto>;
      console.log("마이페이지 데이터:", data);

      if (data.success && data.data) {
        const myPageData = data.data;
        setNickname(myPageData.nickname || "사용자님");
        setIntroduction(myPageData.introduce || "소개글이 없습니다.");
        setBlogTitle(myPageData.blogTitle || "블로그 제목이 없습니다.");
        setEmail(myPageData.email || "");

        setProfileImage(myPageData.profileImageUrl || ""); // 프로필 이미지 URL 세팅
        console.log("프로필 이미지 URL:", myPageData.profileImageUrl);
        setGithubLink(myPageData.githubLink || "");
        setInstagramLink(myPageData.instagramLink || "");
        setTwitterLink(myPageData.twitterLink || "");

        const snsExists =
          !!myPageData.githubLink ||
          !!myPageData.instagramLink ||
          !!myPageData.twitterLink;
        setHasSNSLinks(snsExists);


      } else {
        throw new Error(
          data.message || "마이페이지 데이터를 불러오는데 실패했습니다."
        );
      }
    } catch (err: any) {
      console.error("API 요청 중 오류 발생:", err);
      setError(err.message || "마이페이지 데이터를 불러올 수 없습니다.");
    } finally {
      setIsLoading(false);
    }
  };

  // 프로필 이미지 업로드 핸들러
  const handleProfileImageChange = async (
    e: React.ChangeEvent<HTMLInputElement>
  ) => {
    const file = e.target.files?.[0];
    if (!file) return;
    setProfileImageFile(file);

    // 미리보기
    const reader = new FileReader();
    reader.onloadend = () => {
      setProfileImage(reader.result as string);
    };
    reader.readAsDataURL(file);

    // 서버 업로드 예시
    const formData = new FormData();
    formData.append("profileImage", file);
    try {
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/images/profile/upload`,
        {
          method: "POST",
          credentials: "include",
          body: formData,
        }
      );
      if (!response.ok) throw new Error("프로필 이미지 업로드 실패");
      // 업로드 성공 후 서버에서 받은 이미지 URL로 갱신 (예시)
      const data = await response.json();
      if (data.data?.profileImageUrl) {
        console.log(data.data);
        setProfileImage(data.data.profileImageUrl);
      }
      console.log("프로필 이미지 업로드 성공:", data.data);
      alert("프로필 이미지가 변경되었습니다.");
    } catch (err) {
      alert("프로필 이미지 업로드에 실패했습니다.");
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    // TODO: 프로필 업데이트 API 호출 로직 추가
    console.log({ introduction, githubLink, instagramLink, twitterLink });
    try {
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/users/me/mypage/sns`,
        {
          method: "POST",
          credentials: "include",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            githubLink,
            instagramLink,
            twitterLink,
          }),
        }
      );
    
      if (!response.ok) {
        throw new Error(`수정 실패: ${response.status}`);
      }
  
      const data = (await response.json()) as ApiResponse<MyPageDto>;
      console.log("수정 완료 응답:", data);
      
      if (data.success) {
        alert( hasSNSLinks
          ? "SNS 링크가 성공적으로 수정되었습니다."
          : "SNS 링크가 성공적으로 추가되었습니다.");
        // 최신 상태 반영
        fetchMyPageData();
      } else {
        throw new Error(data.message || "SNS 링크 수정 실패");
      }
    } catch (err: any) {
      console.error("SNS 링크 수정 중 오류:", err);
      alert(err.message || "오류가 발생했습니다.");
    } finally {
      setIsEditing(false);
    }
  };

  // 블로그 제목 수정 함수
  const handleUpdateBlogTitle = async () => {
    if (!tempBlogTitle.trim()) {
      alert("블로그 제목을 입력해주세요.");
      return;
    }

    try {
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/blogs/${nickname}`,
        {
          method: "PUT",
          credentials: "include",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            title: tempBlogTitle,
          }),
        }
      );

      if (!response.ok) {
        throw new Error("블로그 제목 수정 실패");
      }

      const data = await response.json();
      if (data.success) {
        setBlogTitle(tempBlogTitle);
        setIsEditingBlogTitle(false);
        alert("블로그 제목이 수정되었습니다.");
      } else {
        throw new Error(data.message || "블로그 제목 수정 실패");
      }
    } catch (err: any) {
      console.error("블로그 제목 수정 중 오류:", err);
      alert(err.message || "블로그 제목 수정 중 오류가 발생했습니다.");
    }
  };

  if (isLoading) {
    return (
      <div className="flex justify-center items-center h-96">
        <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-rose-500"></div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="bg-red-50 text-red-500 p-4 rounded-lg text-center max-w-4xl mx-auto my-8">
        {error}
      </div>
    );
  }

  return (
    <div className="max-w-4xl mx-auto my-8 p-6 bg-white rounded-lg shadow-sm">
      <div className="flex flex-col md:flex-row">
        {/* 프로필 정보 - 왼쪽 */}
        <div className="md:w-1/3 flex flex-col items-center mb-6 md:mb-0">
          <div className="relative w-40 h-40 rounded-full bg-gray-200 mb-4 overflow-hidden flex items-center justify-center">
            {profileImage ? (
              <Image
                src={profileImage}
                alt="프로필 이미지"
                layout="fill"
                objectFit="cover"
              />
            ) : (
              <span className="text-4xl font-bold text-gray-600">
                {nickname.charAt(0)}
              </span>
            )}
          </div>
          <button
            className="text-rose-400 text-sm mb-4"
            onClick={() =>
              document.getElementById("profileImageInput")?.click()
            }
            type="button"
          >
            프로필 사진 변경
          </button>
          <input
            id="profileImageInput"
            type="file"
            accept="image/*"
            className="hidden"
            onChange={handleProfileImageChange}
          />
          <h2 className="text-xl font-bold mb-1">{nickname}</h2>
          <p className="text-gray-600 mb-4">Frontend Developer</p>

          <div className="flex space-x-3">
            {githubLink && (
              <Link
                href={githubLink}
                target="_blank"
                className="text-gray-700 hover:text-gray-900"
              >
                <svg
                  className="w-6 h-6"
                  fill="currentColor"
                  viewBox="0 0 24 24"
                  aria-hidden="true"
                >
                  <path
                    fillRule="evenodd"
                    d="M12 2C6.477 2 2 6.484 2 12.017c0 4.425 2.865 8.18 6.839 9.504.5.092.682-.217.682-.483 0-.237-.008-.868-.013-1.703-2.782.605-3.369-1.343-3.369-1.343-.454-1.158-1.11-1.466-1.11-1.466-.908-.62.069-.608.069-.608 1.003.07 1.531 1.032 1.531 1.032.892 1.53 2.341 1.088 2.91.832.092-.647.35-1.088.636-1.338-2.22-.253-4.555-1.113-4.555-4.951 0-1.093.39-1.988 1.029-2.688-.103-.253-.446-1.272.098-2.65 0 0 .84-.27 2.75 1.026A9.564 9.564 0 0112 6.844c.85.004 1.705.115 2.504.337 1.909-1.296 2.747-1.027 2.747-1.027.546 1.379.202 2.398.1 2.651.64.7 1.028 1.595 1.028 2.688 0 3.848-2.339 4.695-4.566 4.943.359.309.678.92.678 1.855 0 1.338-.012 2.419-.012 2.747 0 .268.18.58.688.482A10.019 10.019 0 0022 12.017C22 6.484 17.522 2 12 2z"
                    clipRule="evenodd"
                  />
                </svg>
              </Link>
            )}
            {instagramLink && (
              <Link
                href={instagramLink}
                target="_blank"
                className="text-gray-700 hover:text-gray-900"
              >
                <svg
                  className="w-6 h-6"
                  fill="currentColor"
                  viewBox="0 0 24 24"
                  aria-hidden="true"
                >
                  <path
                    fillRule="evenodd"
                    d="M12.315 2c2.43 0 2.784.013 3.808.06 1.064.049 1.791.218 2.427.465a4.902 4.902 0 011.772 1.153 4.902 4.902 0 011.153 1.772c.247.636.416 1.363.465 2.427.048 1.067.06 1.407.06 4.123v.08c0 2.643-.012 2.987-.06 4.043-.049 1.064-.218 1.791-.465 2.427a4.902 4.902 0 01-1.153 1.772 4.902 4.902 0 01-1.772 1.153c-.636.247-1.363.416-2.427.465-1.067.048-1.407.06-4.123.06h-.08c-2.643 0-2.987-.012-4.043-.06-1.064-.049-1.791-.218-2.427-.465a4.902 4.902 0 01-1.772-1.153 4.902 4.902 0 01-1.153-1.772c-.247-.636-.416-1.363-.465-2.427-.047-1.024-.06-1.379-.06-3.808v-.63c0-2.43.013-2.784.06-3.808.049-1.064.218-1.791.465-2.427a4.902 4.902 0 011.153-1.772A4.902 4.902 0 015.45 2.525c.636-.247 1.363-.416 2.427-.465C8.901 2.013 9.256 2 11.685 2h.63zm-.081 1.802h-.468c-2.456 0-2.784.011-3.807.058-.975.045-1.504.207-1.857.344-.467.182-.8.398-1.15.748-.35.35-.566.683-.748 1.15-.137.353-.3.882-.344 1.857-.047 1.023-.058 1.351-.058 3.807v.468c0 2.456.011 2.784.058 3.807.045.975.207 1.504.344 1.857.182.466.399.8.748 1.15.35.35.683.566 1.15.748.353.137.882.3 1.857.344 1.054.048 1.37.058 4.041.058h.08c2.597 0 2.917-.01 3.96-.058.976-.045 1.505-.207 1.858-.344.466-.182.8-.398 1.15-.748.35-.35.566-.683.748-1.15.137-.353.3-.882.344-1.857.048-1.055.058-1.37.058-4.041v-.08c0-2.597-.01-2.917-.058-3.96-.045-.976-.207-1.505-.344-1.858a3.097 3.097 0 00-.748-1.15 3.098 3.098 0 00-1.15-.748c-.353-.137-.882-.3-1.857-.344-1.023-.047-1.351-.058-3.807-.058zM12 6.865a5.135 5.135 0 110 10.27 5.135 5.135 0 010-10.27zm0 1.802a3.333 3.333 0 100 6.666 3.333 3.333 0 000-6.666zm5.338-3.205a1.2 1.2 0 110 2.4 1.2 1.2 0 010-2.4z"
                    clipRule="evenodd"
                  />
                </svg>
              </Link>
            )}
            {twitterLink && (
              <Link
                href={twitterLink}
                target="_blank"
                className="text-gray-700 hover:text-gray-900"
              >
                <svg
                  className="w-6 h-6"
                  fill="currentColor"
                  viewBox="0 0 24 24"
                  aria-hidden="true"
                >
                  <path d="M8.29 20.251c7.547 0 11.675-6.253 11.675-11.675 0-.178 0-.355-.012-.53A8.348 8.348 0 0022 5.92a8.19 8.19 0 01-2.357.646 4.118 4.118 0 001.804-2.27 8.224 8.224 0 01-2.605.996 4.107 4.107 0 00-6.993 3.743 11.65 11.65 0 01-8.457-4.287 4.106 4.106 0 001.27 5.477A4.072 4.072 0 012.8 9.713v.052a4.105 4.105 0 003.292 4.022 4.095 4.095 0 01-1.853.07 4.108 4.108 0 003.834 2.85A8.233 8.233 0 012 18.407a11.616 11.616 0 006.29 1.84" />
                </svg>
              </Link>
            )}
          </div>
        </div>

        {/* 자기소개 및 블로그 정보 - 오른쪽 */}
        <div className="md:w-2/3 md:pl-8">
          <h3 className="text-xl font-bold mb-4">자기소개</h3>
          <div className="bg-gray-50 p-4 rounded-lg mb-6">
            <p className="text-gray-700">{introduction}</p>
          </div>

          <h3 className="text-xl font-bold mb-4">블로그 제목</h3>
          <div className="bg-gray-50 p-4 rounded-lg mb-6">
            <div className="flex justify-between items-center">
              {isEditingBlogTitle ? (
                <div className="flex-1 flex gap-2">
                  <input
                    type="text"
                    value={tempBlogTitle}
                    onChange={(e) => setTempBlogTitle(e.target.value)}
                    className="flex-1 p-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-rose-300"
                    placeholder="새로운 블로그 제목"
                  />
                  <button
                    onClick={handleUpdateBlogTitle}
                    className="px-3 py-1 bg-rose-400 text-white rounded hover:bg-rose-500"
                  >
                    저장
                  </button>
                  <button
                    onClick={() => setIsEditingBlogTitle(false)}
                    className="px-3 py-1 bg-gray-300 text-gray-700 rounded hover:bg-gray-400"
                  >
                    취소
                  </button>
                </div>
              ) : (
                <>
                  <p className="text-gray-900 font-bold">{blogTitle}</p>
                  <button
                    onClick={() => {
                      setTempBlogTitle(blogTitle);
                      setIsEditingBlogTitle(true);
                    }}
                    className="text-rose-400 text-sm hover:text-rose-500"
                  >
                    수정
                  </button>
                </>
              )}
            </div>
          </div>

          <h3 className="text-xl font-bold mb-4">이메일 주소</h3>
          <div className="bg-gray-50 p-4 rounded-lg mb-6">
            <p className="text-gray-900">{email}</p>
          </div>

          <h3 className="text-xl font-bold mt-8 mb-4">SNS 링크 설정</h3>
          <form onSubmit={handleSubmit}>
            <div className="space-y-4">
              <div className="flex items-center">
                <svg
                  className="w-6 h-6 mr-3 text-gray-700"
                  fill="currentColor"
                  viewBox="0 0 24 24"
                  aria-hidden="true"
                >
                  <path
                    fillRule="evenodd"
                    d="M12 2C6.477 2 2 6.484 2 12.017c0 4.425 2.865 8.18 6.839 9.504.5.092.682-.217.682-.483 0-.237-.008-.868-.013-1.703-2.782.605-3.369-1.343-3.369-1.343-.454-1.158-1.11-1.466-1.11-1.466-.908-.62.069-.608.069-.608 1.003.07 1.531 1.032 1.531 1.032.892 1.53 2.341 1.088 2.91.832.092-.647.35-1.088.636-1.338-2.22-.253-4.555-1.113-4.555-4.951 0-1.093.39-1.988 1.029-2.688-.103-.253-.446-1.272.098-2.65 0 0 .84-.27 2.75 1.026A9.564 9.564 0 0112 6.844c.85.004 1.705.115 2.504.337 1.909-1.296 2.747-1.027 2.747-1.027.546 1.379.202 2.398.1 2.651.64.7 1.028 1.595 1.028 2.688 0 3.848-2.339 4.695-4.566 4.943.359.309.678.92.678 1.855 0 1.338-.012 2.419-.012 2.747 0 .268.18.58.688.482A10.019 10.019 0 0022 12.017C22 6.484 17.522 2 12 2z"
                    clipRule="evenodd"
                  />
                </svg>
                <input
                  type="text"
                  placeholder="GitHub 프로필 URL"
                  value={githubLink}
                  onChange={(e) => setGithubLink(e.target.value)}
                  className="flex-1 p-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-rose-300"
                />
              </div>
              <div className="flex items-center">
                <svg
                  className="w-6 h-6 mr-3 text-gray-700"
                  fill="currentColor"
                  viewBox="0 0 24 24"
                  aria-hidden="true"
                >
                  <path
                    fillRule="evenodd"
                    d="M12.315 2c2.43 0 2.784.013 3.808.06 1.064.049 1.791.218 2.427.465a4.902 4.902 0 011.772 1.153 4.902 4.902 0 011.153 1.772c.247.636.416 1.363.465 2.427.048 1.067.06 1.407.06 4.123v.08c0 2.643-.012 2.987-.06 4.043-.049 1.064-.218 1.791-.465 2.427a4.902 4.902 0 01-1.153 1.772 4.902 4.902 0 01-1.772 1.153c-.636.247-1.363.416-2.427.465-1.067.048-1.407.06-4.123.06h-.08c-2.643 0-2.987-.012-4.043-.06-1.064-.049-1.791-.218-2.427-.465a4.902 4.902 0 01-1.772-1.153 4.902 4.902 0 01-1.153-1.772c-.247-.636-.416-1.363-.465-2.427-.047-1.024-.06-1.379-.06-3.808v-.63c0-2.43.013-2.784.06-3.808.049-1.064.218-1.791.465-2.427a4.902 4.902 0 011.153-1.772A4.902 4.902 0 015.45 2.525c.636-.247 1.363-.416 2.427-.465C8.901 2.013 9.256 2 11.685 2h.63zm-.081 1.802h-.468c-2.456 0-2.784.011-3.807.058-.975.045-1.504.207-1.857.344-.467.182-.8.398-1.15.748-.35.35-.566.683-.748 1.15-.137.353-.3.882-.344 1.857-.047 1.023-.058 1.351-.058 3.807v.468c0 2.456.011 2.784.058 3.807.045.975.207 1.504.344 1.857.182.466.399.8.748 1.15.35.35.683.566 1.15.748.353.137.882.3 1.857.344 1.054.048 1.37.058 4.041.058h.08c2.597 0 2.917-.01 3.96-.058.976-.045 1.505-.207 1.858-.344.466-.182.8-.398 1.15-.748.35-.35.566-.683.748-1.15.137-.353.3-.882.344-1.857.048-1.055.058-1.37.058-4.041v-.08c0-2.597-.01-2.917-.058-3.96-.045-.976-.207-1.505-.344-1.858a3.097 3.097 0 00-.748-1.15 3.098 3.098 0 00-1.15-.748c-.353-.137-.882-.3-1.857-.344-1.023-.047-1.351-.058-3.807-.058zM12 6.865a5.135 5.135 0 110 10.27 5.135 5.135 0 010-10.27zm0 1.802a3.333 3.333 0 100 6.666 3.333 3.333 0 000-6.666zm5.338-3.205a1.2 1.2 0 110 2.4 1.2 1.2 0 010-2.4z"
                    clipRule="evenodd"
                  />
                </svg>
                <input
                  type="text"
                  placeholder="Instagram 프로필 URL"
                  value={instagramLink}
                  onChange={(e) => setInstagramLink(e.target.value)}
                  className="flex-1 p-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-rose-300"
                />
              </div>
              <div className="flex items-center">
                <svg
                  className="w-6 h-6 mr-3 text-gray-700"
                  fill="currentColor"
                  viewBox="0 0 24 24"
                  aria-hidden="true"
                >
                  <path d="M8.29 20.251c7.547 0 11.675-6.253 11.675-11.675 0-.178 0-.355-.012-.53A8.348 8.348 0 0022 5.92a8.19 8.19 0 01-2.357.646 4.118 4.118 0 001.804-2.27 8.224 8.224 0 01-2.605.996 4.107 4.107 0 00-6.993 3.743 11.65 11.65 0 01-8.457-4.287 4.106 4.106 0 001.27 5.477A4.072 4.072 0 012.8 9.713v.052a4.105 4.105 0 003.292 4.022 4.095 4.095 0 01-1.853.07 4.108 4.108 0 003.834 2.85A8.233 8.233 0 012 18.407a11.616 11.616 0 006.29 1.84" />
                </svg>
                <input
                  type="text"
                  placeholder="Twitter 프로필 URL"
                  value={twitterLink}
                  onChange={(e) => setTwitterLink(e.target.value)}
                  className="flex-1 p-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-rose-300"
                />
              </div>
            </div>
            <div className="flex justify-end mt-4 gap-2">
              <Link
                href="/users/change/password"
                className="px-4 py-2 bg-gray-200 text-gray-700 rounded hover:bg-gray-300 transition flex items-center"
              >
                비밀번호 변경
              </Link>
              <button
                type="submit"
                className="px-4 py-2 bg-rose-400 text-white rounded hover:bg-rose-500 transition"
              >
                회원정보 수정
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}
