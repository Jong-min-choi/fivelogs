"use client";
import { Board, PageDto, BoardApiResponse } from "@/types/board";
import { useState, useEffect } from "react";
import LoadingSpinner from "./common/LoadingSpinner";
import LatestBoards from "./boards/LatestBoards";
import TrendingBoards from "./boards/TrandingBoards";
import TodayQuestion from "./boards/TodayQuestion";
import { useGlobalLoginUser } from "@/stores/auth/loginUser";

type BoardSummary = {
  id: number;
  title: string;
  content: string;
  views: number;
  hashtags: string[];
  created: string;
  updated: string;
  nickname: string;
};

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
  const [trendingBoards, setTrendingBoards] = useState<BoardSummary[]>([]);
  const [pageInfo, setPageInfo] = useState<PageDto | null>(null);
  const [currentPage, setCurrentPage] = useState(1);
  const [viewType, setViewType] = useState<ViewType>("latest");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [csQuestions, setCsQuestions] = useState<ChatContent[]>([]);
  const { isLogin } = useGlobalLoginUser();
  const boardsPerPage = 12;

  // 최신 게시글 fetch
  const fetchBoards = async (page: number = 1) => {
    try {
      setLoading(true);
      const url = `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/boards?page=${page}&size=${boardsPerPage}`;
      const response = await fetch(url);
      if (!response.ok) throw new Error(`서버 응답 오류: ${response.status}`);
      const responseData: BoardApiResponse = await response.json();
      if (responseData.success) {
        setBoards(responseData.data.boardDtoList);
        setPageInfo(responseData.data.pageDto);
      } else {
        throw new Error(
          responseData.message || "데이터를 불러오는데 실패했습니다."
        );
      }
    } catch (err) {
      setError("게시글을 불러올 수 없습니다. 잠시 후 다시 시도해주세요.");
    } finally {
      setLoading(false);
    }
  };

  // 트렌딩 게시글 fetch
  const fetchTrendingBoards = async () => {
    try {
      setLoading(true);
      const url = `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/boards/trending`;
      const response = await fetch(url);
      if (!response.ok) throw new Error(`서버 응답 오류: ${response.status}`);
      const responseData = await response.json();
      if (responseData.success) {
        console.log(responseData.data);
        setTrendingBoards(responseData.data);
      } else {
        throw new Error(
          responseData.message || "트렌딩 게시글을 불러오는데 실패했습니다."
        );
      }
    } catch (err) {
      setError(
        "트렌딩 게시글을 불러올 수 없습니다. 잠시 후 다시 시도해주세요."
      );
    } finally {
      setLoading(false);
    }
  };

  const fetchCSQuestion = async () => {
    try {
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/csQuestion`
      );
      if (!response.ok) throw new Error("CS 질문을 불러오는데 실패했습니다.");
      const data: CsQuestionResponse = await response.json();
      if (data.success) setCsQuestions(data.data);
    } catch (error) {
      setError("CS 질문을 불러올 수 없습니다.");
    }
  };

  useEffect(() => {
    if (viewType === "todayQuestion") {
      fetchCSQuestion();
    } else if (viewType === "trending") {
      fetchTrendingBoards();
    } else {
      fetchBoards(currentPage);
    }
  }, [currentPage, viewType]);

  const handlePageChange = (pageNumber: number) => {
    setCurrentPage(pageNumber);
    if (viewType === "latest") fetchBoards(pageNumber);
  };

  const handleViewTypeChange = (type: ViewType) => {
    if (type === "todayQuestion" && !isLogin) {
      alert("회원 유저만 이용 가능합니다.");
      return;
    }
    setViewType(type);
    setCurrentPage(1);
    if (type === "latest") fetchBoards(1);
    if (type === "trending") fetchTrendingBoards();
  };

  if (
    loading &&
    ((viewType === "latest" && boards.length === 0) ||
      (viewType === "trending" && trendingBoards.length === 0))
  ) {
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
          className={`py-2 px-4 font-medium cursor-pointer ${
            viewType === "latest"
              ? "border-b-2 border-rose-500 text-rose-500"
              : "text-gray-500 hover:text-gray-800"
          }`}
          onClick={() => handleViewTypeChange("latest")}
        >
          최신
        </button>
        <button
          className={`py-2 px-4 font-medium cursor-pointer ${
            viewType === "trending"
              ? "border-b-2 border-rose-500 text-rose-500"
              : "text-gray-500 hover:text-gray-800"
          }`}
          onClick={() => handleViewTypeChange("trending")}
        >
          트렌딩
        </button>
        <button
          className={`py-2 px-4 font-medium cursor-pointer ${
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
          boards={trendingBoards}
          loading={loading}
          pageInfo={null}
          onPageChange={() => {}}
        />
      )}
      {viewType === "todayQuestion" && (
        <TodayQuestion questions={csQuestions} />
      )}
    </>
  );
}
