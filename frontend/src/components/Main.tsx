"use client";
import { Board, PageDto, BoardApiResponse } from "@/types/board";
import { useState, useEffect } from "react";
import LoadingSpinner from "./common/LoadingSpinner";
import LatestBoards from "./boards/LatestBoards";
import TrendingBoards from "./boards/TrandingBoards";
import TodayQuestion from "./boards/TodayQuestion";
import { useGlobalLoginUser } from "@/stores/auth/loginUser";

type BoardsProps = {
  initialBoards?: Board[];
};
type ViewType = "latest" | "trending" | "todayQuestion";

type ChatContent = {
  question: string;
  options: string[];
  answer: string;
};

type CsQuestionResponse = {
  success: boolean;
  data: ChatContent[];
};

export default function Boards({ initialBoards = [] }: BoardsProps) {
  const [boards, setBoards] = useState<Board[]>(initialBoards);
  const [pageInfo, setPageInfo] = useState<PageDto | null>(null);
  const [currentPage, setCurrentPage] = useState(1);
  const [viewType, setViewType] = useState<ViewType>("latest");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [csQuestions, setCsQuestions] = useState<ChatContent[]>([]);
  const { isLogin } = useGlobalLoginUser();

  const boardsPerPage = 12; // 3x4 그리드를 위해 한 페이지에 12개 게시글 표시

  const fetchBoards = async (page: number = 1) => {
    try {
      setLoading(true);

      // viewType에 따라 다른 정렬 파라미터 전달
      const sortParam = viewType === "trending" ? "views" : "created";
      const url = `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/boards?page=${page}&size=${boardsPerPage}`;
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

  const fetchCSQuestion = async () => {
    try {
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/csQuestion`
      );
      if (!response.ok) {
        throw new Error("CS 질문을 불러오는데 실패했습니다.");
      }
      const data: CsQuestionResponse = await response.json();
      if (data.success) {
        console.log(data);
        setCsQuestions(data.data);
      }
    } catch (error) {
      console.error("CS 질문 로딩 실패:", error);
      setError("CS 질문을 불러올 수 없습니다.");
    }
  };

  useEffect(() => {
    if (viewType === "todayQuestion") {
      fetchCSQuestion();
    } else {
      fetchBoards(currentPage);
    }
  }, [currentPage, viewType]);

  // 페이지 변경 핸들러
  const handlePageChange = (pageNumber: number) => {
    console.log("페이지 변경:", pageNumber);
    setCurrentPage(pageNumber);
    fetchBoards(pageNumber);
  };

  // 뷰 타입 변경 핸들러
  const handleViewTypeChange = (type: ViewType) => {
    if (type === "todayQuestion" && !isLogin) {
      alert("회원 유저만 이용 가능합니다.");
      return;
    }
    console.log("뷰 타입 변경:", type);
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
        <button
          className={`py-2 px-4 font-medium ${
            viewType === "todayQuestion"
              ? "border-b-2 border-rose-500 text-rose-500"
              : "text-gray-500 hover:text-gray-800"
          }`}
          onClick={() => handleViewTypeChange("todayQuestion")}
        >
          Today Question
        </button>
      </div>

      {viewType === "latest" && (
        <LatestBoards
          boards={boards}
          loading={loading}
          pageInfo={pageInfo}
          onPageChange={handlePageChange}
        />
      )}
      {viewType === "trending" && (
        <TrendingBoards
          boards={boards}
          loading={loading}
          pageInfo={pageInfo}
          onPageChange={handlePageChange}
        />
      )}
      {viewType === "todayQuestion" && (
        <TodayQuestion questions={csQuestions} />
      )}
    </>
  );
}
