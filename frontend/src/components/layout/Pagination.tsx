"use client";
import React from "react";

type PaginationProps = {
  currentPage: number;
  totalPages: number;
  onPageChange: (page: number) => void;
};

export default function Pagination({
  currentPage,
  totalPages,
  onPageChange,
}: PaginationProps) {
  // 표시할 페이지 번호 계산 (최대 10개)
  const getPageNumbers = () => {
    // 페이지가 10개 이하면 모두 표시
    if (totalPages <= 10) {
      return Array.from({ length: totalPages }, (_, i) => i + 1);
    }

    // 현재 페이지가 앞부분에 위치할 경우
    if (currentPage <= 6) {
      return Array.from({ length: 10 }, (_, i) => i + 1);
    }

    // 현재 페이지가 뒷부분에 위치할 경우
    if (currentPage > totalPages - 5) {
      return Array.from({ length: 10 }, (_, i) => totalPages - 9 + i);
    }

    // 그 외의 경우 현재 페이지를 중심으로 앞뒤로 페이지를 균형 있게 표시
    return Array.from({ length: 10 }, (_, i) => currentPage - 5 + i);
  };

  const pageNumbers = getPageNumbers();

  return (
    <div className="flex justify-center mt-8">
      <nav className="inline-flex">
        {/* 처음 페이지 버튼 */}
        {currentPage > 1 && (
          <button
            onClick={() => onPageChange(1)}
            className="border border-gray-300 px-3 py-1 rounded-l-md focus:outline-none hover:bg-gray-50"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              className="h-4 w-4"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M11 19l-7-7 7-7m8 14l-7-7 7-7"
              />
            </svg>
          </button>
        )}

        {/* 이전 페이지 버튼 */}
        {currentPage > 1 && (
          <button
            onClick={() => onPageChange(currentPage - 1)}
            className="border border-gray-300 px-3 py-1 focus:outline-none hover:bg-gray-50"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              className="h-4 w-4"
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
          </button>
        )}

        {/* 페이지 번호 버튼들 */}
        {pageNumbers.map((page) => (
          <button
            key={page}
            onClick={() => onPageChange(page)}
            className={`
              ${
                page === currentPage
                  ? "bg-rose-500 text-white"
                  : "border border-gray-300 hover:bg-gray-50"
              }
              px-3 py-1 focus:outline-none
            `}
          >
            {page}
          </button>
        ))}

        {/* 다음 페이지 버튼 */}
        {currentPage < totalPages && (
          <button
            onClick={() => onPageChange(currentPage + 1)}
            className="border border-gray-300 px-3 py-1 focus:outline-none hover:bg-gray-50"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              className="h-4 w-4"
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
          </button>
        )}

        {/* 마지막 페이지 버튼 */}
        {currentPage < totalPages && (
          <button
            onClick={() => onPageChange(totalPages)}
            className="border border-gray-300 px-3 py-1 rounded-r-md focus:outline-none hover:bg-gray-50"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              className="h-4 w-4"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M13 5l7 7-7 7m-8-14l7 7-7 7"
              />
            </svg>
          </button>
        )}
      </nav>
    </div>
  );
}
