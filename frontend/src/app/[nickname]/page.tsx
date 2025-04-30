"use client";
import { useEffect, useState } from "react";
import Link from "next/link";
import { useParams, useRouter, useSearchParams } from "next/navigation";
import {
  BoardSummaryDto,
  BoardPageResponseDto,
  ApiResponse,
  BlogOwnerDto,
  HashtagCountDto,
} from "@/types/blog";
import { PageDto } from "@/types/board";
import Pagination from "@/components/common/Pagination";
import LoadingSpinner from "@/components/common/LoadingSpinner";
import AttendanceCalendar from "@/components/attendance/AttendanceCalendar";
import { useGlobalLoginUser } from "@/stores/auth/loginUser";
import SocialLinks from "@/components/SocialLinks";
import removeMarkdown from "remove-markdown";

export default function MyBoardPage() {
  const params = useParams();
  const router = useRouter();
  const searchParams = useSearchParams();
  const { isLogin, loginUser } = useGlobalLoginUser();

  // URL에서 받은 닉네임 디코딩
  const encodedNickname = params?.nickname as string;
  const nickname = decodeURIComponent(encodedNickname);

  // tag 쿼리 파라미터 읽기
  const selectedTag = searchParams.get("tag");

  const [currentPage, setCurrentPage] = useState(1);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [boardData, setBoardData] = useState<BoardSummaryDto[]>([]);
  const [pageInfo, setPageInfo] = useState<PageDto | null>(null);
  const [ownerInfo, setOwnerInfo] = useState<BlogOwnerDto | null>(null);
  const [hashtags, setHashtags] = useState<HashtagCountDto[]>([]);
  const [showAttendance, setShowAttendance] = useState(
    searchParams.get("showAttendance") || false
  );
  const boardsPerPage = 10; // 한 페이지에 10개 게시글 표시

  // 팔로우 상태 (ownerInfo?.isFollowing 등 실제 값으로 대체)
  const [isFollowing, setIsFollowing] = useState(false); // 팔로우 상태 초기값);
  //그냥 닉네임 가져오면 되는 문제제
  // 블로그 주인 여부 (ownerInfo?.isOwner 등 실제 값으로 대체)
  const isOwner = loginUser?.nickname === nickname ? true : false;
  const loginUserId = loginUser?.id ?? 0; // 로그인한 사용자 ID

  useEffect(() => {
    const fetchFollowStatus = async () => {
      if (!nickname) return;
      try {
        const response = await fetch(
          `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/followStatus/nickname/${nickname}`,
          {
            credentials: "include",
          }
        );
        if (!response.ok) throw new Error("팔로우 상태 조회 실패");
        const data = await response.json();
        setIsFollowing(data.data.following);
      } catch (err) {
        setIsFollowing(false);
      }
    };
    // 둘 다 호출
    fetchFollowStatus();
  }, [nickname]);

  // 팔로우/언팔로우 버튼 클릭 핸들러

  const handleFollowToggle = async () => {
    if (!isLogin) {
      router.push("/login");
      return;
    }
    if (!ownerInfo) {
      alert("정보를 불러오는 중입니다. 잠시 후 다시 시도해주세요.");
      return;
    }
    if (!isFollowing) {
      try {
        const response = await fetch(
          `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/follow/${ownerInfo.id}`,
          {
            method: "POST",
            credentials: "include",
            headers: {
              "Content-Type": "application/json",
            },
          }
        );
        if (!response.ok) {
          throw new Error("팔로우 요청 실패");
        }
        // 성공 시 팔로우 상태 토글
        setIsFollowing((prev) => !prev);
      } catch (err) {
        alert("팔로우 처리 중 오류가 발생했습니다.");
      }
    } else {
      try {
        const response = await fetch(
          `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/unfollow/${ownerInfo.id}`,
          {
            method: "DELETE",
            credentials: "include",
            headers: {
              "Content-Type": "application/json",
            },
          }
        );
        if (!response.ok) {
          throw new Error("언팔로우 요청 실패");
        }
        // 성공 시 팔로우 상태 토글
        setIsFollowing((prev) => !prev);
      } catch (err) {
        alert("언팔로우 처리 중 오류가 발생했습니다.");
      }
    }
  };

  // 페이지 변경 핸들러
  const handlePageChange = (page: number) => {
    console.log("페이지 변경:", page);
    setCurrentPage(page);
    window.scrollTo(0, 0);
  };

  // 블로그 소유자 정보 가져오기
  const fetchBlogOwnerInfo = async () => {
    try {
      // API 요청 시 닉네임 인코딩
      const url = `${
        process.env.NEXT_PUBLIC_API_BASE_URL
      }/api/users/${encodeURIComponent(nickname)}/blog`;
      console.log("블로그 소유자 정보 API 요청 URL:", url);

      const response = await fetch(url);

      if (!response.ok) {
        throw new Error(`서버 응답 오류: ${response.status}`);
      }

      const data = (await response.json()) as ApiResponse<BlogOwnerDto>;
      console.log("블로그 소유자 정보 API 응답 데이터:", data);

      if (data.success) {
        setOwnerInfo(data.data);
      } else {
        console.error(
          "블로그 소유자 정보를 가져오는데 실패했습니다:",
          data.message
        );
      }
    } catch (err) {
      console.error("블로그 소유자 정보 API 요청 중 오류 발생:", err);
    }
  };

  // 게시글 데이터 가져오기 (tag 쿼리 반영)
  const fetchBlogData = async (page: number, tag?: string | null) => {
    try {
      setLoading(true);
      let url = `${
        process.env.NEXT_PUBLIC_API_BASE_URL
      }/api/blogs/${encodeURIComponent(
        nickname
      )}?page=${page}&size=${boardsPerPage}`;

      if (tag) {
        url += `&tag=${encodeURIComponent(tag)}`;
      }
      const response = await fetch(url, {
        credentials: "include",
      });

      if (!response.ok) {
        throw new Error(`서버 응답 오류: ${response.status}`);
      }

      const data = (await response.json()) as ApiResponse<BoardPageResponseDto>;

      if (data.success) {
        const responseData = data.data;
        setBoardData(responseData.boardDtoList || []);
        setPageInfo(responseData.pageDto || null);
      } else {
        throw new Error(data.message || "데이터를 불러오는데 실패했습니다.");
      }
    } catch (err) {
      setError(
        "블로그 데이터를 불러올 수 없습니다. 잠시 후 다시 시도해주세요."
      );
    } finally {
      setLoading(false);
    }
  };

  // 해시태그 정보 가져오기
  const fetchHashtags = async () => {
    try {
      const url = `${
        process.env.NEXT_PUBLIC_API_BASE_URL
      }/api/hashtags/${encodeURIComponent(nickname)}`;
      const response = await fetch(url);

      if (!response.ok) {
        throw new Error(`서버 응답 오류: ${response.status}`);
      }

      const data = (await response.json()) as ApiResponse<HashtagCountDto[]>;

      if (data.success) {
        setHashtags(data.data);
      } else {
        console.error("해시태그 정보를 가져오는데 실패했습니다:", data.message);
      }
    } catch (err) {
      console.error("해시태그 API 요청 중 오류 발생:", err);
    }
  };

  useEffect(() => {
    fetchBlogData(currentPage, selectedTag);
    fetchBlogOwnerInfo();
    fetchHashtags();
  }, [nickname, currentPage, selectedTag]);

  // 로딩 중 표시
  if (loading && boardData.length === 0) {
    return <LoadingSpinner size="md" color="rose-500" height="h-60" />;
  }

  // 에러 표시
  if (error) {
    return (
      <div className="bg-red-50 text-red-500 p-4 rounded-lg text-center">
        {error}
      </div>
    );
  }

  return (
    <div className="flex flex-col md:flex-row md:gap-8">
      {/* 메인 콘텐츠 영역 */}
      <main className="md:w-3/4">
        {showAttendance ? (
          // 출석부를 main 영역에 표시
          <div className="flex justify-center items-start py-10">
            <AttendanceCalendar />
          </div>
        ) : (
          <>
            <div className="flex justify-between items-center mb-6">
              <h1 className="text-2xl font-bold">{nickname}의 블로그</h1>
            </div>
            {/* 로딩 인디케이터 (기존 데이터가 있을 때) */}
            {loading && boardData.length > 0 && (
              <div className="fixed inset-0 bg-white bg-opacity-70 flex items-center justify-center z-10">
                <LoadingSpinner size="md" color="rose-500" height="h-20" />
              </div>
            )}
            {/* 블로그 게시글 목록 */}
            {boardData.length === 0 && !loading ? (
              <div className="text-center p-10 bg-gray-50 rounded-lg">
                <p className="text-gray-500">게시글이 없습니다.</p>
              </div>
            ) : (
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
                {boardData.map((board) => (
                  <div key={board.id} className="border rounded-lg shadow-sm">
                    <div className="p-5">
                      <div className="flex items-center text-sm text-gray-500 mb-2">
                        <span>
                          {new Date(board.created).toLocaleDateString()}
                        </span>
                        <span className="mx-2">•</span>
                        <span>조회 {board.views}</span>
                      </div>
                      {/* 게시글 제목 + 자물쇠 */}
                      <h2 className="text-lg font-bold mb-2 flex items-center gap-2">
                        {board.title}
                        {board.boardStatus === "PRIVATE" ? (
                          <span title="비공개" className="ml-1 text-base">
                            🔒
                          </span>
                        ) : (
                          <span title="공개" className="ml-1 text-base">
                            🔓
                          </span>
                        )}
                      </h2>
                      <p className="text-gray-600 mb-4 line-clamp-2">
                        {removeMarkdown(board.content)}
                      </p>
                      {board.hashtags && board.hashtags.length > 0 && (
                        <div className="flex flex-wrap gap-2 mb-4">
                          {board.hashtags.map((tag, idx) => (
                            <span
                              key={idx}
                              className="text-xs bg-gray-100 text-gray-600 px-2 py-1 rounded-full"
                            >
                              {tag.startsWith("#") ? tag : `#${tag}`}
                            </span>
                          ))}
                        </div>
                      )}
                      <Link
                        href={`/${encodeURIComponent(nickname)}/${board.id}`}
                        className="text-rose-500 inline-flex items-center group"
                      >
                        자세히 보기
                        <svg
                          xmlns="http://www.w3.org/2000/svg"
                          className="h-4 w-4 ml-1 group-hover:translate-x-1 transition"
                          viewBox="0 0 20 20"
                          fill="currentColor"
                        >
                          <path
                            fillRule="evenodd"
                            d="M10.293 5.293a1 1 0 011.414 0l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414-1.414L12.586 11H5a1 1 0 110-2h7.586l-2.293-2.293a1 1 0 010-1.414z"
                            clipRule="evenodd"
                          />
                        </svg>
                      </Link>
                    </div>
                  </div>
                ))}
              </div>
            )}
            {/* 페이지네이션 */}
            {pageInfo && (
              <Pagination pageInfo={pageInfo} onPageChange={handlePageChange} />
            )}
          </>
        )}
      </main>

      {/* 오른쪽 사이드바 */}
      <aside className="md:w-1/4 mt-8 md:mt-0">
        <div className="sticky top-8 bg-white rounded-lg shadow-sm p-5">
          {/* 프로필 영역 */}
          <div className="flex items-start mb-5">
            {ownerInfo?.profileImageUrl ? (
              <div className="w-16 h-16 rounded-full bg-gray-200 overflow-hidden mr-4">
                <img
                  src={ownerInfo.profileImageUrl}
                  alt="프로필 이미지"
                  className="w-full h-full object-cover"
                />
              </div>
            ) : (
              <div className="w-16 h-16 rounded-full bg-gray-200 overflow-hidden mr-4">
                <div className="w-full h-full flex items-center justify-center text-gray-400 text-2xl font-bold">
                  {nickname.charAt(0).toUpperCase()}
                </div>
              </div>
            )}
            <div className="flex-1">
              <h2 className="text-xl font-bold">
                {ownerInfo?.nickname || nickname}
              </h2>
              <p className="text-gray-500 text-sm mb-2">
                {ownerInfo?.introduce ||
                  ownerInfo?.myIntroduce ||
                  "소개글이 없습니다"}
              </p>
            </div>
          </div>

          {/* SNS 링크 버튼 */}
          {ownerInfo?.githubLink && (
            <div className="flex justify-end gap-2">
              <SocialLinks
                githubLink={ownerInfo.githubLink}
                instagramLink={ownerInfo.instagramLink}
                twitterLink={ownerInfo.twitterLink}
              />
            </div>
          )}

          {/* 팔로우/언팔로우 버튼: 블로그 주인이 아닐 때만 노출 */}
          {!isOwner && (
            <button
              disabled={!ownerInfo}
              className={`cursor-pointer w-full ${
                isFollowing
                  ? "bg-gray-200 text-gray-700 hover:bg-gray-300"
                  : "bg-emerald-50 text-emerald-600 border border-emerald-500 hover:bg-emerald-100"
              } font-bold py-2 px-4 rounded mb-4 transition`}
              onClick={handleFollowToggle}
            >
              {isFollowing ? "언팔로우" : "팔로우"}
            </button>
          )}

          {/* 통계 정보 */}
          <div className="space-y-2 border-t pt-4">
            <div className="flex justify-between items-center">
              <span className="text-gray-600 flex items-center">
                <svg
                  className="w-4 h-4 mr-2"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                  xmlns="http://www.w3.org/2000/svg"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth="2"
                    d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10"
                  ></path>
                </svg>
                게시글
              </span>
              <span className="font-medium">
                {ownerInfo?.boardCount || 0}개
              </span>
            </div>

            <div className="flex justify-between items-center">
              <span className="text-gray-600 flex items-center">
                <svg
                  className="w-4 h-4 mr-2"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                  xmlns="http://www.w3.org/2000/svg"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth="2"
                    d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"
                  ></path>
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth="2"
                    d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"
                  ></path>
                </svg>
                방문자
              </span>
              <span className="font-medium">{ownerInfo?.viewCount || 0}명</span>
            </div>

            <div className="flex justify-between items-center">
              <Link href={`/${nickname}/followings`}>
                <span className="text-gray-600 flex items-center">
                  <svg
                    className="w-4 h-4 mr-2"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                    xmlns="http://www.w3.org/2000/svg"
                  >
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      strokeWidth="2"
                      d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z"
                    ></path>
                  </svg>
                  팔로잉
                </span>
              </Link>
              <Link href={`/${nickname}/followings`}>
                <span className="font-medium">
                  {ownerInfo?.followingCount || 0}명
                </span>
              </Link>
            </div>

            <div className="flex justify-between items-center">
              <Link href={`/${nickname}/followers`}>
                <span className="text-gray-600 flex items-center">
                  <svg
                    className="w-4 h-4 mr-2"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                    xmlns="http://www.w3.org/2000/svg"
                  >
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      strokeWidth="2"
                      d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z"
                    ></path>
                  </svg>
                  팔로워
                </span>
              </Link>
              <Link href={`/${nickname}/followers`}>
                <span className="font-medium">
                  {ownerInfo?.followerCount || 0}명
                </span>
              </Link>
            </div>
          </div>
          {/* 출석부 버튼 */}

          <button
            className="cursor-pointer w-full bg-rose-500 hover:bg-rose-600 text-white font-bold py-2 px-4 rounded mt-6 mb-4 transition"
            onClick={() => setShowAttendance((prev) => !prev)}
          >
            {showAttendance ? "게시글 보기" : "출석부"}
          </button>

          {/* 해시태그 목차 */}
          {hashtags.length > 0 && (
            <div className="mt-6">
              <h3 className="text-lg font-bold mb-4 flex items-center">
                <svg
                  className="w-5 h-5 mr-2 text-gray-600"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                  xmlns="http://www.w3.org/2000/svg"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth="2"
                    d="M7 20l4-16m2 16l4-16M6 9h14M4 15h14"
                  />
                </svg>
                해시태그
              </h3>
              <ul className="space-y-2">
                <li>
                  <button
                    className={`text-gray-600 hover:text-rose-500 transition-colors cursor-pointer bg-transparent border-none p-0 ${
                      !selectedTag ? "font-bold text-rose-500" : ""
                    }`}
                    onClick={() => {
                      router.push(`/${encodeURIComponent(nickname)}`);
                      setCurrentPage(1);
                    }}
                  >
                    전체
                  </button>
                </li>
                {hashtags.map((tag) => (
                  <li
                    key={tag.name}
                    className="flex justify-between items-center"
                  >
                    <button
                      className={`text-gray-600 hover:text-rose-500 transition-colors cursor-pointer bg-transparent border-none p-0 ${
                        selectedTag === tag.name
                          ? "font-bold text-rose-500"
                          : ""
                      }`}
                      onClick={() => {
                        router.push(
                          `/${encodeURIComponent(
                            nickname
                          )}?tag=${encodeURIComponent(tag.name)}`
                        );
                        setCurrentPage(1);
                      }}
                    >
                      #{tag.name}
                    </button>
                    <span className="text-sm text-gray-400">{tag.count}</span>
                  </li>
                ))}
              </ul>
            </div>
          )}
        </div>
      </aside>
    </div>
  );
}
