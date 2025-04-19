"use client";
import { useState } from "react";
import Layout from "@/app/ClientLayout";
import Link from "next/link";
import Image from "next/image";

export default function MyBoardPage() {
  const [currentPage, setCurrentPage] = useState(1);
  const [selectedCategory, setSelectedCategory] = useState("전체");

  // 블로그 게시글 더미 데이터
  const boardItems = [
    {
      id: "1",
      category: "기술",
      date: "2024.02.15",
      title: "효율적인 코드 작성을 위한 10가지 팁",
      description:
        "개발자로서 더 나은 코드를 작성하기 위한 실용적인 팁들을 소개합니다. 코드 리뷰부터 문서화까지, 프로젝트의 효율을 높이는 방법을 알아보세요.",
      author: "김개발",
    },
    {
      id: "2",
      category: "라이프스타일",
      date: "2024.02.14",
      title: "서울의 숨은 카페 명소 TOP 10",
      description:
        "분위기 좋은 카페부터 맛있는 커피로 유명한 곳까지, 서울의 숨은 카페들을 소개합니다. 주말 데이트 코스로 추천드려요.",
      author: "박여행",
    },
    {
      id: "3",
      category: "여행",
      date: "2024.02.13",
      title: "한국의 전통 사찰 여행기",
      description:
        "아름다운 풍경과 고요한 분위기의 전통 사찰을 찾아 떠난 여행기를 소개합니다.",
      author: "이여행",
    },
    {
      id: "4",
      category: "개발",
      date: "2024.02.12",
      title: "V11 버전 업데이트 소식",
      description: "최신 버전의 주요 기능과 개선사항을 소개합니다.",
      author: "최버전",
    },
    {
      id: "5",
      category: "기술",
      date: "2024.02.11",
      title: "신기술 트렌드 분석",
      description: "2024년 주목해야 할 기술 트렌드를 분석합니다.",
      author: "정분석",
    },
    {
      id: "6",
      category: "AI",
      date: "2024.02.07",
      title: "AI 개발 시작하기",
      description:
        "AI 개발을 시작하는 개발자를 위한 기초 가이드와 실전 팁을 공유합니다.",
      author: "김인공",
    },
    {
      id: "7",
      category: "클라우드",
      date: "2024.02.08",
      title: "클라우드 서비스 비교",
      description: "주요 클라우드 서비스의 특징과 장단점을 비교 분석합니다.",
      author: "한구름",
    },
    {
      id: "8",
      category: "백엔드",
      date: "2024.02.05",
      title: "마이크로서비스 아키텍처 설계",
      description:
        "실제 프로젝트에서 마이크로서비스를 구현하는 방법과 모범 사례를 소개합니다.",
      author: "박서버",
    },
    {
      id: "9",
      category: "프론트엔드",
      date: "2024.02.04",
      title: "React 컴포넌트 최적화 기법",
      description:
        "React 애플리케이션의 성능을 향상시키는 다양한의 최적화 기법을 소개합니다.",
      author: "이리액트",
    },
    {
      id: "10",
      category: "기술",
      date: "2024.02.03",
      title: "Git 고급 사용법",
      description:
        "Git을 더 효율적으로 사용하기 위한 고급 팁과 기법을 알아봅니다.",
      author: "박깃",
    },
  ];

  // 카테고리 목록
  const categories = [
    "전체",
    "기술",
    "라이프스타일",
    "여행",
    "개발",
    "AI",
    "클라우드",
    "백엔드",
    "프론트엔드",
  ];

  // 현재 카테고리에 따라 게시글 필터링
  const filteredBoards =
    selectedCategory === "전체"
      ? boardItems
      : boardItems.filter((board) => board.category === selectedCategory);

  // 페이지당 게시글 수
  const boardsPerPage = 4;

  // 전체 페이지 수 계산
  const totalPages = Math.ceil(filteredBoards.length / boardsPerPage);

  // 현재 페이지에 표시할 게시글
  const indexOfLastBoard = currentPage * boardsPerPage;
  const indexOfFirstBoard = indexOfLastBoard - boardsPerPage;
  const currentBoards = filteredBoards.slice(
    indexOfFirstBoard,
    indexOfLastBoard
  );

  // 페이지 변경 핸들러
  const handlePageChange = (page: number) => {
    setCurrentPage(page);
    window.scrollTo(0, 0);
  };

  // 카테고리 변경 핸들러
  const handleCategoryChange = (category: string) => {
    setSelectedCategory(category);
    setCurrentPage(1);
  };

  return (
    <div className="flex flex-col md:flex-row md:gap-8">
      {/* 메인 콘텐츠 영역 */}
      <main className="md:w-3/4">
        <div className="flex justify-between items-center mb-6">
          <h1 className="text-2xl font-bold">My Board</h1>

          {/* 카테고리 필터 */}
          <div className="relative inline-block">
            <select
              className="appearance-none border rounded-md py-1 px-3 pr-8 bg-white focus:outline-none"
              value={selectedCategory}
              onChange={(e) => handleCategoryChange(e.target.value)}
            >
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

        {/* 블로그 게시글 목록 */}
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
          {currentBoards.map((board) => (
            <div key={board.id} className="border rounded-lg shadow-sm">
              <div className="p-5">
                <div className="flex items-center text-sm text-gray-500 mb-2">
                  <span>{board.category}</span>
                  <span className="mx-2">•</span>
                  <span>{board.date}</span>
                </div>
                <h2 className="text-lg font-bold mb-2">{board.title}</h2>
                <p className="text-gray-600 mb-4 line-clamp-2">
                  {board.description}
                </p>
                <Link
                  href={`/board/${board.id}`}
                  className="text-rose-500 inline-flex items-center"
                >
                  자세히 보기
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    className="h-4 w-4 ml-1"
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

        {/* 페이지네이션 */}
        <div className="flex justify-center">
          <nav className="inline-flex">
            {/* 이전 페이지 버튼 */}
            {currentPage > 1 && (
              <button
                onClick={() => handlePageChange(currentPage - 1)}
                className="border border-gray-300 px-3 py-1 rounded-l-md hover:bg-gray-100"
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
            {Array.from({ length: totalPages }, (_, i) => i + 1).map((page) => (
              <button
                key={page}
                onClick={() => handlePageChange(page)}
                className={`${
                  currentPage === page
                    ? "bg-rose-500 text-white"
                    : "border border-gray-300 hover:bg-gray-100"
                } px-3 py-1`}
              >
                {page}
              </button>
            ))}

            {/* 다음 페이지 버튼 */}
            {currentPage < totalPages && (
              <button
                onClick={() => handlePageChange(currentPage + 1)}
                className="border border-gray-300 px-3 py-1 rounded-r-md hover:bg-gray-100"
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
      </main>

      {/* 오른쪽 사이드바 - 프로필 영역 */}
      <aside className="md:w-1/4 mt-8 md:mt-0">
        <div className="sticky top-8 bg-gray-50 rounded-xl p-6">
          <div className="flex flex-col items-center mb-4">
            <div className="w-24 h-24 rounded-full bg-gray-200 overflow-hidden mb-4">
              <Image
                src="/next.svg"
                alt="프로필 이미지"
                width={96}
                height={96}
                className="object-cover"
              />
            </div>
            <h2 className="text-xl font-bold">김개발</h2>
            <p className="text-gray-600 mb-4">프론트엔드 개발자</p>

            <div className="w-full space-y-4">
              <div className="grid grid-cols-3 gap-2 text-center">
                <div>
                  <p className="font-medium text-sm">총 게시글</p>
                  <p className="text-lg">26개</p>
                </div>
                <div>
                  <p className="font-medium text-sm">총 댓글</p>
                  <p className="text-lg">124개</p>
                </div>
                <div>
                  <p className="font-medium text-sm">방문자</p>
                  <p className="text-lg">1,234명</p>
                </div>
              </div>
            </div>
          </div>

          <div className="border-t pt-4 mt-2">
            <h3 className="font-medium mb-2">시리즈</h3>
            <ul className="space-y-2">
              <li className="text-sm">• React 완벽 가이드 (12)</li>
              <li className="text-sm">• TypeScript 기초 (8)</li>
              <li className="text-sm">• 웹 성능 최적화 (6)</li>
            </ul>
          </div>
        </div>
      </aside>
    </div>
  );
}
