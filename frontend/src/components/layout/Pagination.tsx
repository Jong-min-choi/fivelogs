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
  // 표시할 페이지 번호 배열 생성
  const pageNumbers = [];
  for (let i = 1; i <= totalPages; i++) {
    pageNumbers.push(i);
  }

  return (
    <div className="flex justify-center mt-8">
      <nav className="inline-flex">
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
              ${page === 1 ? "rounded-l-md" : ""}
              ${page === totalPages ? "rounded-r-md" : ""}
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
                d="M9 5l7 7-7 7"
              />
            </svg>
          </button>
        )}
      </nav>
    </div>
  );
}
