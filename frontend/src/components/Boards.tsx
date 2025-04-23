"use client";
import { Board, PageDto, BoardApiResponse } from "@/types/board";
import Link from "next/link";
import { useState, useEffect } from "react";
import Pagination from "./common/Pagination";
import LoadingSpinner from "./common/LoadingSpinner";

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
  const [selectedAnswers, setSelectedAnswers] = useState<{
    [key: number]: number;
  }>({});
  const [submittedQuestions, setSubmittedQuestions] = useState<{
    [key: number]: boolean;
  }>({});
  const [results, setResults] = useState<{ [key: number]: boolean }>({});
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

  const handleOptionSelect = (questionIndex: number, optionIndex: number) => {
    if (submittedQuestions[questionIndex]) return; // 이미 제출된 문제는 선택 불가
    setSelectedAnswers((prev) => ({
      ...prev,
      [questionIndex]: optionIndex,
    }));
  };

  const handleSubmitAll = () => {
    csQuestions.forEach((question, index) => {
      const selectedAnswer = selectedAnswers[index]?.toString();
      const correctAnswer = question.answer;

      setSubmittedQuestions((prev) => ({
        ...prev,
        [index]: true,
      }));

      setResults((prev) => ({
        ...prev,
        [index]: selectedAnswer === correctAnswer,
      }));
    });
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

      {viewType === "todayQuestion" ? (
        <div className="max-w-2xl mx-auto space-y-8">
          {csQuestions.length > 0 ? (
            <>
              {csQuestions.map((question, questionIndex) => (
                <div
                  key={questionIndex}
                  className="bg-white p-6 rounded-lg shadow-md"
                >
                  <h2 className="text-xl font-bold mb-4">
                    CS 질문 #{questionIndex + 1}
                  </h2>
                  <p className="text-lg mb-4">{question.question}</p>
                  <div className="space-y-3 mb-6">
                    {question.options.map((option, optionIndex) => (
                      <div
                        key={optionIndex}
                        className={`p-3 border rounded-lg cursor-pointer transition-colors
                          ${
                            selectedAnswers[questionIndex] === optionIndex
                              ? "bg-yellow-100"
                              : "hover:bg-gray-50"
                          }
                          ${
                            submittedQuestions[questionIndex]
                              ? "cursor-not-allowed"
                              : ""
                          }`}
                        onClick={() =>
                          handleOptionSelect(questionIndex, optionIndex)
                        }
                      >
                        {option}
                      </div>
                    ))}
                  </div>

                  {submittedQuestions[questionIndex] && (
                    <div
                      className={`mt-4 p-4 rounded-lg ${
                        results[questionIndex] ? "bg-green-100" : "bg-red-100"
                      }`}
                    >
                      <p className="font-semibold">
                        {results[questionIndex]
                          ? "정답입니다! 👏"
                          : "틀렸습니다. 다시 도전해보세요! 💪"}
                      </p>
                    </div>
                  )}
                </div>
              ))}

              {/* 전체 채점 버튼 */}
              <div className="flex justify-center mt-8">
                <button
                  onClick={handleSubmitAll}
                  disabled={
                    Object.keys(selectedAnswers).length < csQuestions.length ||
                    Object.keys(submittedQuestions).length ===
                      csQuestions.length
                  }
                  className={`px-6 py-3 rounded-md text-lg font-semibold
                    ${
                      Object.keys(selectedAnswers).length <
                        csQuestions.length ||
                      Object.keys(submittedQuestions).length ===
                        csQuestions.length
                        ? "bg-gray-300 cursor-not-allowed"
                        : "bg-rose-500 text-white hover:bg-rose-600"
                    }`}
                >
                  전체 문제 채점하기
                </button>
              </div>
            </>
          ) : (
            <LoadingSpinner size="md" color="rose-500" height="h-20" />
          )}
        </div>
      ) : (
        <>
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
                          {board.hashtags &&
                            board.hashtags.map((tag, index) => (
                              <span
                                key={index}
                                className="text-xs bg-gray-100 text-gray-600 px-2 py-1 rounded-full"
                              >
                                {tag.startsWith("#") ? tag : `#${tag}`}
                              </span>
                            ))}
                        </div>

                        <Link
                          href={`/${board.nickname}/${board.id}`}
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
                  pageInfo={pageInfo}
                  onPageChange={handlePageChange}
                />
              </div>
            </>
          )}
        </>
      )}
    </>
  );
}
