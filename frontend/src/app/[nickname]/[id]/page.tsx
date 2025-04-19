"use client";
import { useEffect, useState } from "react";
import { useParams } from "next/navigation";
import Link from "next/link";
import LoadingSpinner from "@/components/common/LoadingSpinner";

// BoardDetailDto 타입 정의
interface BoardDetailDto {
  blogTitle: string;
  boardTitle: string;
  content: string;
  createdDateTime: string;
  updatedDateTime: string;
  views: number;
  hashtags: string[];
  nickName: string;
  profileImageLink: string;
  myIntroduce: string;
}

export default function BoardDetail() {
  const params = useParams();
  const boardId = params?.id || "";
  const nickname = params?.nickname || "";
  const [loading, setLoading] = useState(true);
  const [board, setBoard] = useState<BoardDetailDto | null>(null);
  const [error, setError] = useState<string | null>(null);
  // 임시 로그인 사용자 정보 (실제로는 로그인 컨텍스트나 상태에서 가져와야 함)
  const [isMyBoard, setIsMyBoard] = useState(false);

  useEffect(() => {
    const fetchBoardDetail = async () => {
      try {
        setLoading(true);

        // API 호출
        const url = `http://localhost:8090/api/boards/${boardId}`;
        console.log("API 요청 URL:", url);

        const response = await fetch(url);

        if (!response.ok) {
          throw new Error(`서버 응답 오류: ${response.status}`);
        }

        const data = await response.json();
        console.log("API 응답 데이터:", data);

        if (data.success) {
          setBoard(data.data);

          // 게시글 작성자가 현재 로그인한 사용자인지 확인
          // 실제로는 로그인 컨텍스트나 상태에서 사용자 정보를 가져와 비교해야 함
          // 여기서는 예시로 localStorage에서 사용자 정보를 가져와 비교
          // const loggedInUser = localStorage.getItem("nickName") || "";
          // setIsMyBoard(data.data.nickName === loggedInUser);
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

    if (boardId) {
      fetchBoardDetail();
    }
  }, [boardId]);

  // 게시글 삭제 핸들러
  const handleDelete = async () => {
    if (!window.confirm("정말로 이 게시글을 삭제하시겠습니까?")) {
      return;
    }

    try {
      const response = await fetch(
        `http://localhost:8090/api/boards/${boardId}`,
        {
          method: "DELETE",
        }
      );

      if (!response.ok) {
        throw new Error("게시글 삭제에 실패했습니다.");
      }

      // 삭제 성공 시 메인 페이지로 이동
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
              <div className="flex items-center text-lg text-gray-800 font-medium">
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  className="h-5 w-5 mr-2"
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
                <span className="font-bold">{board.blogTitle}</span>
              </div>

              {/* 수정/삭제 버튼 (로그인 사용자의 게시물인 경우에만 표시) */}
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
            {board.profileImageLink ? (
              <img
                src={board.profileImageLink}
                alt={board.nickName}
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

      {/* 이전/다음 버튼 */}
      <div className="flex justify-between mt-12 border-t pt-6">
        <Link
          href="/"
          className="flex items-center text-gray-500 hover:text-gray-700"
        >
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
              d="M15 19l-7-7 7-7"
            />
          </svg>
          목록으로
        </Link>
        <div className="flex gap-4">
          <button className="px-4 py-2 border rounded-md hover:bg-gray-50 transition">
            이전 글
          </button>
          <button className="px-4 py-2 border rounded-md hover:bg-gray-50 transition">
            다음 글
          </button>
        </div>
      </div>
    </main>
  );
}
