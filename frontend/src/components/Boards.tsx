"use client";
import { Board } from "@/types/board";
import Link from "next/link";
import { useState } from "react";
import Pagination from "./layout/Pagination";

type BoardsProps = {
  boards: Board[];
  categories: string[];
};

export default function Boards({ boards, categories }: BoardsProps) {
  const [currentPage, setCurrentPage] = useState(1);
  const [selectedCategory, setSelectedCategory] = useState("전체");
  const boardsPerPage = 6;

  // 선택된 카테고리에 따라 게시글 필터링
  const filteredBoards =
    selectedCategory === "전체"
      ? boards
      : boards.filter((board) => board.category === selectedCategory);

  // 현재 페이지에 표시할 게시글 계산
  const indexOfLastBoard = currentPage * boardsPerPage;
  const indexOfFirstBoard = indexOfLastBoard - boardsPerPage;
  const currentBoards = filteredBoards.slice(
    indexOfFirstBoard,
    indexOfLastBoard
  );

  // 페이지 변경 핸들러
  const handlePageChange = (pageNumber: number) => {
    setCurrentPage(pageNumber);
  };

  // 카테고리 변경 핸들러
  const handleCategoryChange = (category: string) => {
    setSelectedCategory(category);
    setCurrentPage(1); // 카테고리 변경 시 첫 페이지로 이동
  };

  return (
    <>
      <h1 className="text-2xl font-bold mb-6">개발 게시글</h1>

      <div className="flex justify-between items-center mb-4">
        {/* CategorySelector 내장 */}
        <div className="relative inline-block">
          <select
            className="appearance-none border rounded-md py-1 px-3 pr-8 bg-white focus:outline-none"
            value={selectedCategory}
            onChange={(e) => handleCategoryChange(e.target.value)}
          >
            <option value="전체">전체</option>
            {categories.map((category) => (
              <option key={category} value={category}>
                {category}
              </option>
            ))}
          </select>
          <div className="pointer-events-none absolute inset-y-0 right-0 flex items-center px-2">
            <svg
              className="fill-current h-4 w-4"
              xmlns="http://www.w3.org/2000/svg"
              viewBox="0 0 20 20"
            >
              <path d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z" />
            </svg>
          </div>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {currentBoards.map((board) => (
          /* BoardCard 내장 */
          <div
            key={board.id}
            className="border rounded-lg overflow-hidden shadow-sm hover:shadow-md transition"
          >
            <div className="p-4">
              <div className="flex items-center text-sm text-gray-500 mb-2">
                <span>{board.category}</span>
                <span className="mx-2">•</span>
                <span>{board.date}</span>
              </div>
              <h2 className="text-lg font-bold mb-2">{board.title}</h2>
              <p className="text-gray-600 mb-4">{board.description}</p>
              <Link
                href={board.link}
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
        ))}
      </div>

      <Pagination
        currentPage={currentPage}
        totalPages={Math.ceil(filteredBoards.length / boardsPerPage)}
        onPageChange={handlePageChange}
      />
    </>
  );
}
