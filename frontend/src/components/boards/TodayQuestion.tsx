import { useState } from "react";
import { useRouter } from "next/navigation";
import LoadingSpinner from "../common/LoadingSpinner";
import { useGlobalLoginUser } from "@/stores/auth/loginUser";

type ChatContent = {
  question: string;
  options: string[];
  answer: string;
};

type Props = {
  questions: ChatContent[];
};

export default function TodayQuestion({ questions }: Props) {
  const [selectedAnswers, setSelectedAnswers] = useState<{
    [key: number]: number;
  }>({});
  const [results, setResults] = useState<{ [key: number]: boolean }>({});
  const [attendanceSuccess, setAttendanceSuccess] = useState<boolean | null>(
    null
  );
  const router = useRouter();
  const { isLogin, loginUser } = useGlobalLoginUser();
  const handleOptionSelect = (questionIndex: number, optionIndex: number) => {
    setSelectedAnswers((prev) => ({
      ...prev,
      [questionIndex]: optionIndex,
    }));
  };

  const handleSubmitAll = async () => {
    let allCorrect = true;
    questions.forEach((question, index) => {
      const selectedAnswer = selectedAnswers[index]?.toString();
      const correctAnswer = parseInt(question.answer).toString();
      const isCorrect = selectedAnswer === correctAnswer;
      setResults((prev) => ({
        ...prev,
        [index]: isCorrect,
      }));
      if (!isCorrect) allCorrect = false;
    });

    // 선택한 정답 리스트 생성
    const answerList = questions.map((_, idx) =>
      selectedAnswers[idx] !== undefined ? selectedAnswers[idx] + 1 : null
    );

    // 모든 정답이 맞으면 출석 POST 요청
    if (allCorrect && questions.length > 0) {
      try {
        const res = await fetch(
          `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/attendances`,
          {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ answers: answerList }),
            credentials: "include",
          }
        );
        if (res.ok) {
          setAttendanceSuccess(true);
          // 출석 성공 시 이동
          alert("정답입니다!. 출석체크 완료!");
          router.push(`/${loginUser.nickname}?showAttendance=true`);
        } else {
          const errorData = await res.json();
          alert(errorData.message || "에러가 발생했습니다.");
          setAttendanceSuccess(false);
        }
      } catch {
        setAttendanceSuccess(false);
      }
    } else {
      setAttendanceSuccess(null);
    }
  };

  if (questions.length === 0) {
    return <LoadingSpinner size="md" color="rose-500" height="h-20" />;
  }

  return (
    <div className="max-w-2xl mx-auto space-y-8">
      {questions.map((question, questionIndex) => (
        <div key={questionIndex} className="bg-white p-6 rounded-lg shadow-md">
          <h2 className="text-xl font-bold mb-4">
            CS 질문 #{questionIndex + 1}
          </h2>
          <p className="text-lg mb-4">{question.question}</p>
          <div className="space-y-3 mb-6">
            {question.options.map((option, optionIndex) => (
              <div
                key={optionIndex}
                onClick={() => handleOptionSelect(questionIndex, optionIndex)}
                className={`p-3 border rounded-lg cursor-pointer transition-colors
                  ${
                    selectedAnswers[questionIndex] === optionIndex
                      ? "bg-yellow-100"
                      : "hover:bg-gray-50"
                  }`}
              >
                {option}
              </div>
            ))}
          </div>
          {results[questionIndex] !== undefined && (
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
      <div className="flex flex-col items-center mt-8 space-y-2">
        <button
          onClick={handleSubmitAll}
          disabled={
            !isLogin || Object.keys(selectedAnswers).length < questions.length
          }
          className={`cursor-pointer px-6 py-3 rounded-md text-lg font-semibold
            ${
              !isLogin || Object.keys(selectedAnswers).length < questions.length
                ? "bg-gray-300 cursor-not-allowed"
                : "bg-rose-500 text-white hover:bg-rose-600"
            }`}
        >
          채점하기
        </button>
        {attendanceSuccess === true && (
          <div className="text-green-600 font-bold">출석이 완료되었습니다!</div>
        )}
        {attendanceSuccess === false && (
          <div className="text-red-600 font-bold">
            출석 처리에 실패했습니다.
          </div>
        )}
      </div>
    </div>
  );
}
