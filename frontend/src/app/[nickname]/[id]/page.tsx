"use client";
import { useEffect, useState } from "react";
import { useParams } from "next/navigation";
import Link from "next/link";
import LoadingSpinner from "@/components/common/LoadingSpinner";
import CommentList from "@/components/comment/CommentList";
import { useGlobalLoginUser } from "@/stores/auth/loginUser";
import Image from "next/image";

// BoardDetailDto 타입 정의
interface BoardDetailDto {
  boardId: number;
  blogTitle: string;
  boardTitle: string;
  content: string;
  createdDateTime: string;
  updatedDateTime: string;
  views: number;
  hashtags: string[];
  nickName: string;
  profileImageUrl: string;
  myIntroduce: string;
}

// 이전/다음 게시글 정보 타입 정의
interface SimpleBoardDto {
  id: number;
  title: string;
}

interface PrevNextResponse {
  success: boolean;
  message: string;
  data: {
    prev: SimpleBoardDto | null;
    next: SimpleBoardDto | null;
  };
}

export default function BoardDetail() {
  const params = useParams();
  const boardId = params?.id || "";
  const nickname = params?.nickname || "";
  const [loading, setLoading] = useState(true);
  const [board, setBoard] = useState<BoardDetailDto | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [isMyBoard, setIsMyBoard] = useState(false);
  const [prevNext, setPrevNext] = useState<{
    prev: SimpleBoardDto | null;
    next: SimpleBoardDto | null;
  }>({
    prev: null,
    next: null,
  });
  const { isLogin, loginUser } = useGlobalLoginUser();

  // 팔로우 상태 관리
  const [isFollowing, setIsFollowing] = useState<boolean>(false);
  const [followId, setFollowId] = useState<number | null>(null);

  useEffect(() => {
    const fetchBoardDetail = async () => {
      try {
        setLoading(true);

        // API 호출
        const url = `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/boards/${boardId}`;
        console.log("API 요청 URL:", url);

        const response = await fetch(url);

        if (!response.ok) {
          throw new Error(`서버 응답 오류: ${response.status}`);
        }

        const data = await response.json();
        console.log("API 응답 데이터:", data);

        if (data.success) {
          setBoard(data.data);
          setFollowId(data.data.userId); // userId(팔로우 대상) 세팅

          // 이전/다음 게시글 정보 가져오기
          await fetchPrevNextBoard();
        } else {
          throw new Error(data.message || "게시글을 불러오는데 실패했습니다.");
        }
      } catch (err) {
        console.error("API 요청 중 오류 발생:", err);
        setError("게시글을 불러올 수 없습니다. 잠시 후 다시 시도해주세요.");
      } finally {
        setLoading(false);
      }
    };

    // 이전/다음 게시글 정보를 가져오는 함수
    const fetchPrevNextBoard = async () => {
      try {
        const url = `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/boards/${boardId}/author/${nickname}`;
        console.log("이전/다음 게시글 API 요청 URL:", url);

        const response = await fetch(url);

        if (!response.ok) {
          throw new Error(`서버 응답 오류: ${response.status}`);
        }

        const data: PrevNextResponse = await response.json();
        console.log("이전/다음 게시글 API 응답 데이터:", data);

        if (data.success) {
          setPrevNext({
            prev: data.data.prev,
            next: data.data.next,
          });
        }
      } catch (err) {
        console.error("이전/다음 게시글 정보 가져오기 오류:", err);
        // 이전/다음 게시글 정보 가져오기 실패는 치명적인 오류가 아니므로 별도의 에러 상태 설정하지 않음
      }
    };

    if (boardId) {
      fetchBoardDetail();
    }
  }, [boardId, nickname]);

  // 로그인 유저 정보와 게시글 정보가 모두 있을 때만 비교
  useEffect(() => {
    if (loginUser && board) {
      setIsMyBoard(loginUser.nickname === board.nickName);
    }
  }, [loginUser, board]);

  // 팔로우 상태 요청
  useEffect(() => {
    const fetchFollowStatus = async () => {
      if (!followId) return;
      try {
        const res = await fetch(
          `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/followStatus/${followId}`,
          { credentials: "include" }
        );
        if (!res.ok) {
          setIsFollowing(false);
          return;
        }
        const data = await res.json();
        // FollowStatusDto: { isFollowing: boolean }
        console.log("follow status {}", data);
        setIsFollowing(!!data.data?.following);
      } catch {
        setIsFollowing(false);
      }
    };
    fetchFollowStatus();
  }, [followId]);

  // 팔로우/언팔로우 버튼 핸들러
  const handleFollow = async () => {
    if (!isLogin) {
      alert("로그인 후 이용해주세요.");
      window.location.href = "/users/login";
      return;
    }
    if (!followId) return;
    try {
      const res = await fetch(
        `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/follow/${followId}`,
        {
          method: "POST",
          credentials: "include",
        }
      );
      if (!res.ok) throw new Error("팔로우 실패");
      setIsFollowing(true);
    } catch {
      alert("팔로우에 실패했습니다.");
    }
  };

  const handleUnfollow = async () => {
    if (!followId) return;
    try {
      const res = await fetch(
        `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/unfollow/${followId}`,
        {
          method: "DELETE",
          credentials: "include",
        }
      );
      if (!res.ok) throw new Error("언팔로우 실패");
      setIsFollowing(false);
    } catch {
      alert("언팔로우에 실패했습니다.");
    }
  };

  useEffect(() => {
    // 게시글 조회수 증가 API 호출
    if (boardId) {
      fetch(
        `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/boards/${boardId}/views`,
        {
          method: "POST",
        }
      ).catch((err) => {
        // 에러는 무시 (조회수 실패해도 화면엔 영향 없음)
        console.error("조회수 증가 실패:", err);
      });
    }
  }, [boardId]);

  // 게시글 삭제 핸들러
  const handleDelete = async () => {
    if (!window.confirm("정말로 이 게시글을 삭제하시겠습니까?")) {
      return;
    }

    try {
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/boards/${boardId}`,
        {
          method: "DELETE",
          credentials: "include",
        }
      );

      if (!response.ok) {
        throw new Error("게시글 삭제에 실패했습니다.");
      }

      alert("게시글이 성공적으로 삭제되었습니다.");
      window.location.href = "/";
    } catch (error) {
      console.error("삭제 중 오류 발생:", error);
      alert("게시글 삭제에 실패했습니다. 다시 시도해주세요.");
    }
  };

  if (loading) {
    return <LoadingSpinner size="md" color="rose-500" height="h-60" />;
  }

  if (error || !board) {
    return (
      <div className="bg-red-50 text-red-500 p-4 rounded-lg text-center">
        {error || "게시글을 찾을 수 없습니다."}
      </div>
    );
  }

  return (
    <main className="py-6 max-w-4xl mx-auto px-4">
      {/* 블로그 헤더 */}
      <div className="mb-8">
        {/* 날짜 정보 */}
        <div className="flex items-center text-sm text-gray-500 mb-3">
          <span>{board.createdDateTime}</span>
          {board.updatedDateTime &&
            board.updatedDateTime !== board.createdDateTime && (
              <>
                <span className="mx-2">•</span>
                <span>수정됨: {board.updatedDateTime}</span>
              </>
            )}
          <span className="mx-2">•</span>
          <span className="flex items-center">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              className="h-4 w-4 mr-1"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"
              />
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"
              />
            </svg>
            {board.views.toLocaleString()}
          </span>
        </div>

        {/* 블로그 제목과 게시글 제목 영역 */}
        <div className="bg-gray-50 p-6 rounded-lg mb-6 border border-gray-800">
          {/* 블로그 제목 */}
          {board.blogTitle && (
            <div className="flex items-center justify-between mb-3">
              <div className="flex items-center text-lg text-blue-400 font-medium cursor-pointer hover:text-blue-600 transition-colors">
                <Link href={`/${nickname}`} className="flex items-center">
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    className="h-5 w-5 mr-2 text-blue-400"
                    fill="none"
                    viewBox="0 0 24 24"
                    stroke="currentColor"
                  >
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      strokeWidth={2}
                      d="M19 20H5a2 2 0 01-2-2V6a2 2 0 012-2h10a2 2 0 012 2v1m2 13a2 2 0 01-2-2V7m2 13a2 2 0 002-2V9a2 2 0 00-2-2h-2m-4-3H9M7 16h6M7 8h6v4H7V8z"
                    />
                  </svg>
                  <span className="font-medium underline underline-offset-2">
                    {board.blogTitle}
                  </span>
                </Link>
              </div>

              {/* 팔로우/언팔로우 버튼 (본인 블로그가 아닐 때만) */}
              {!isMyBoard &&
                followId &&
                (isFollowing ? (
                  <button
                    className="border border-rose-400 text-rose-500 px-5 py-1.5 rounded-full font-semibold hover:bg-rose-50 transition ml-2"
                    type="button"
                    onClick={handleUnfollow}
                  >
                    언팔로우
                  </button>
                ) : (
                  <button
                    className="border border-emerald-500 text-emerald-600 px-5 py-1.5 rounded-full font-semibold hover:bg-emerald-50 transition ml-2"
                    type="button"
                    onClick={handleFollow}
                  >
                    팔로우
                  </button>
                ))}

              {/* 수정/삭제 버튼 (로그인 사용자의 게시물이고 삭제되지 않은 경우에만 표시) */}
              {isMyBoard && (
                <div className="flex space-x-2">
                  <Link
                    href={`/${nickname}/${boardId}/edit`}
                    className="text-white bg-gray-600 hover:bg-gray-700 px-3 py-1 rounded text-xs font-medium transition-colors flex items-center"
                  >
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      className="h-3.5 w-3.5 mr-1"
                      fill="none"
                      viewBox="0 0 24 24"
                      stroke="currentColor"
                    >
                      <path
                        strokeLinecap="round"
                        strokeLinejoin="round"
                        strokeWidth={2}
                        d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"
                      />
                    </svg>
                    수정
                  </Link>

                  {/* 삭제 버튼 */}
                  <button
                    onClick={handleDelete}
                    className="text-white bg-red-500 hover:bg-red-600 px-3 py-1 rounded text-xs font-medium transition-colors flex items-center"
                  >
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      className="h-3.5 w-3.5 mr-1"
                      fill="none"
                      viewBox="0 0 24 24"
                      stroke="currentColor"
                    >
                      <path
                        strokeLinecap="round"
                        strokeLinejoin="round"
                        strokeWidth={2}
                        d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"
                      />
                    </svg>
                    삭제
                  </button>
                </div>
              )}
            </div>
          )}

          {/* 게시글 제목 */}
          <h1 className="text-3xl font-bold text-gray-800 mb-4">
            {board.boardTitle}
          </h1>

          {/* 해시태그 */}
          {board.hashtags && board.hashtags.length > 0 && (
            <div className="flex flex-wrap gap-2 mt-4">
              {board.hashtags.map((tag, index) => (
                <span
                  key={index}
                  className="bg-gray-200 text-gray-700 px-3 py-1.5 rounded-full text-sm hover:bg-gray-300 transition-colors"
                >
                  {tag.startsWith("#") ? tag : `#${tag}`}
                </span>
              ))}
            </div>
          )}
        </div>
      </div>

      {/* 블로그 콘텐츠 */}
      <div
        className="prose prose-slate max-w-none mb-60"
        dangerouslySetInnerHTML={{ __html: board.content }}
      ></div>

      {/* 작성자 프로필 영역 */}
      <div className="bg-gray-50 p-6 rounded-lg mb-8 border">
        <div className="flex items-center">
          <div className="w-16 h-16 rounded-full bg-gray-200 flex items-center justify-center text-gray-600 mr-4 overflow-hidden">
            {board.profileImageUrl ? (
              <Image
                src={board.profileImageUrl}
                alt={board.nickName}
                width={64}
                height={64}
                className="w-full h-full object-cover"
              />
            ) : (
              <span className="text-2xl font-bold">
                {board.nickName.charAt(0)}
              </span>
            )}
          </div>
          <div>
            <div className="text-lg font-bold mb-1">{board.nickName}</div>
            {board.myIntroduce && (
              <div className="text-gray-600">{board.myIntroduce}</div>
            )}
          </div>
        </div>
      </div>

      {/* 댓글 영역 */}
      <CommentList boardId={Number(boardId)} />

      {/* 이전/다음 버튼 */}
      <div className="flex justify-between mt-12 border-t pt-6">
        {prevNext.prev ? (
          <Link
            href={`/${nickname}/${prevNext.prev.id}`}
            className="flex items-center text-gray-700 hover:text-rose-500 transition-colors"
          >
            <div className="flex items-center">
              <div className="w-10 h-10 rounded-full bg-gray-100 flex items-center justify-center mr-3">
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  className="h-5 w-5"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke="currentColor"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2}
                    d="M15 19l-7-7 7-7"
                  />
                </svg>
              </div>
              <div className="flex flex-col">
                <span className="text-xs font-medium text-gray-500">
                  이전 포스트
                </span>
                <span className="font-medium">{prevNext.prev.title}</span>
              </div>
            </div>
          </Link>
        ) : (
          <div></div>
        )}

        {prevNext.next ? (
          <Link
            href={`/${nickname}/${prevNext.next.id}`}
            className="flex items-center text-gray-700 hover:text-rose-500 transition-colors text-right"
          >
            <div className="flex items-center">
              <div className="flex flex-col items-end">
                <span className="text-xs font-medium text-gray-500">
                  다음 포스트
                </span>
                <span className="font-medium">{prevNext.next.title}</span>
              </div>
              <div className="w-10 h-10 rounded-full bg-gray-100 flex items-center justify-center ml-3">
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  className="h-5 w-5"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke="currentColor"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2}
                    d="M9 5l7 7-7 7"
                  />
                </svg>
              </div>
            </div>
          </Link>
        ) : (
          <div></div>
        )}
      </div>
    </main>
  );
}
