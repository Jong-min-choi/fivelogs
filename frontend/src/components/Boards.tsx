"use client";
import { Board, PageDto, BoardApiResponse } from "@/types/board";
import Link from "next/link";
import { useState, useEffect } from "react";
import Pagination from "./layout/Pagination";
import LoadingSpinner from "./common/LoadingSpinner";

type BoardsProps = {
  initialBoards?: Board[];
};

type ViewType = "latest" | "trending";

export default function Boards({ initialBoards = [] }: BoardsProps) {
  const [boards, setBoards] = useState<Board[]>(initialBoards);
  const [pageInfo, setPageInfo] = useState<PageDto | null>(null);
  const [currentPage, setCurrentPage] = useState(1);
  const [viewType, setViewType] = useState<ViewType>("latest");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const boardsPerPage = 12; // 3x4 그리드를 위해 한 페이지에 12개 게시글 표시

  const fetchBoards = async (page: number = 1) => {
    try {
      setLoading(true);

      // viewType에 따라 다른 정렬 파라미터 전달
      const sortParam = viewType === "trending" ? "views" : "created";
      const url = `http://localhost:8090/api/boards?page=${page}&size=${boardsPerPage}`;
      console.log("API 요청 URL:", url);

      const response = await fetch(url);

      if (!response.ok) {
        throw new Error(`서버 응답 오류: ${response.status}`);
      }

      const responseData: BoardApiResponse = await response.json();
      console.log("API 응답 데이터:", responseData);

      if (responseData.success) {
        setBoards(responseData.data.boardDtoList);
        setPageInfo(responseData.data.pageDto);
      } else {
        throw new Error(
          responseData.message || "데이터를 불러오는데 실패했습니다."
        );
      }
    } catch (err) {
      console.error("API 요청 중 오류 발생:", err);
      setError("게시글을 불러올 수 없습니다. 잠시 후 다시 시도해주세요.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchBoards(currentPage);
  }, []);

  // 페이지 변경 핸들러
  const handlePageChange = (pageNumber: number) => {
    console.log("페이지 변경:", pageNumber);
    setCurrentPage(pageNumber);
    fetchBoards(pageNumber);
  };

  // 뷰 타입 변경 핸들러
  const handleViewTypeChange = (type: ViewType) => {
    setViewType(type);
    setCurrentPage(1); // 뷰 타입 변경 시 첫 페이지로 이동
    fetchBoards(1); // 뷰 타입 변경 시 첫 페이지 데이터 다시 요청
  };

  if (loading && boards.length === 0) {
    return <LoadingSpinner size="md" color="rose-500" height="h-60" />;
  }

  if (error) {
    return (
      <div className="bg-red-50 text-red-500 p-4 rounded-lg text-center">
        {error}
      </div>
    );
  }

  // 현재 페이지 계산 (API의 페이지는 0부터 시작, UI는 1부터 시작)
  const displayCurrentPage = pageInfo ? pageInfo.presentPage + 1 : currentPage;
  const totalPages = pageInfo?.totalPage || 1;

  return (
    <>
      <h1 className="text-2xl font-bold mb-6">개발 게시글</h1>

      <div className="flex mb-6 border-b">
        <button
          className={`py-2 px-4 font-medium ${
            viewType === "latest"
              ? "border-b-2 border-rose-500 text-rose-500"
              : "text-gray-500 hover:text-gray-800"
          }`}
          onClick={() => handleViewTypeChange("latest")}
        >
          최신
        </button>
        <button
          className={`py-2 px-4 font-medium ${
            viewType === "trending"
              ? "border-b-2 border-rose-500 text-rose-500"
              : "text-gray-500 hover:text-gray-800"
          }`}
          onClick={() => handleViewTypeChange("trending")}
        >
          트렌딩
        </button>
      </div>

      {loading && boards.length > 0 && (
        <div className="fixed inset-0 bg-white bg-opacity-70 flex items-center justify-center z-10">
          <LoadingSpinner size="md" color="rose-500" height="h-20" />
        </div>
      )}

      {boards.length === 0 && !loading ? (
        <div className="text-center py-10 text-gray-500">
          게시글이 없습니다.
        </div>
      ) : (
        <>
          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
            {boards.map((board) => (
              /* BoardCard 내장 */
              <div
                key={board.id}
                className="border rounded-lg overflow-hidden shadow-sm hover:shadow-md transition"
              >
                <div className="p-4 flex flex-col h-full">
                  <div className="flex items-center justify-between text-sm text-gray-500 mb-2">
                    <span>{board.created}</span>
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
                  <h2 className="text-lg font-bold mb-2">{board.title}</h2>
                  <p className="text-gray-600 mb-4">
                    {board.content.length > 100
                      ? board.content.substring(0, 100) + "..."
                      : board.content}
                  </p>

                  <div className="mt-auto">
                    {/* 해시태그 */}
                    <div className="flex flex-wrap gap-1 mb-3">
                      {board.hashtags.map((tag, index) => (
                        <span
                          key={index}
                          className="text-xs bg-gray-100 text-gray-600 px-2 py-1 rounded-full"
                        >
                          {tag.startsWith("#") ? tag : `#${tag}`}
                        </span>
                      ))}
                    </div>

                    <Link
                      href={`/blog/${board.id}`}
                      className="text-rose-500 flex items-center group"
                    >
                      자세히 보기
                      <svg
                        xmlns="http://www.w3.org/2000/svg"
                        className="h-4 w-4 ml-1 group-hover:translate-x-1 transition"
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
                    </Link>
                  </div>
                </div>
              </div>
            ))}
          </div>

          <div className="mt-6">
            <Pagination
              currentPage={displayCurrentPage}
              totalPages={totalPages}
              onPageChange={handlePageChange}
            />
          </div>
        </>
      )}
    </>
  );
}
