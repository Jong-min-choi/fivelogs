"use client";
import React from "react";
import { PageDto } from "@/types/board";

type PaginationProps = {
  pageInfo: PageDto | null;
  onPageChange: (page: number) => void;
};

export default function Pagination({
  pageInfo,
  onPageChange,
}: PaginationProps) {
  if (!pageInfo) return null;

  // API 응답에서 받은 페이지 정보
  const { currentPage, totalPage, startPage, endPage } = pageInfo;
  const totalPages = totalPage;

  // 현재 페이지 그룹 계산 (1-10, 11-20 등)
  // const currentGroup = Math.ceil(currentPage / 10);

  // 현재 페이지 그룹의 시작 페이지와 끝 페이지
  // const startPage = (currentGroup - 1) * 10 + 1;
  // const endPage  = Math.min(currentGroup * 10, totalPages)

  // 현재 페이지 그룹에 표시할 페이지 번호 배열
  const pageNumbers = Array.from(
    { length: endPage - startPage + 1 },
    (_, i) => startPage + i
  );

  return (
    <div className="flex justify-center mt-8">
      <nav className="inline-flex">
        {/* 처음 페이지 버튼 */}
        {currentPage > 1 && (
          <button
            onClick={() => onPageChange(1)}
            className="border border-gray-300 px-3 py-1 rounded-l-md focus:outline-none hover:bg-gray-50 cursor-pointer"
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

        {/* 이전 페이지 그룹 버튼 */}
        {startPage > 1 && (
          <button
            onClick={() => onPageChange(startPage - 1)}
            className="border border-gray-300 px-3 py-1 focus:outline-none hover:bg-gray-50 cursor-pointer"
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
              px-3 py-1 focus:outline-none cursor-pointer
            `}
          >
            {page}
          </button>
        ))}

        {/* 다음 페이지 그룹 버튼 */}
        {endPage < totalPages && (
          <button
            onClick={() => onPageChange(endPage + 1)}
            className="border border-gray-300 px-3 py-1 focus:outline-none hover:bg-gray-50 cursor-pointer"
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
            onClick={() => onPageChange(totalPage)}
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
