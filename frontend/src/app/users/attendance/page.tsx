"use client";

import { useEffect, useState } from "react";
import Calendar from "react-calendar";
import "react-calendar/dist/Calendar.css";
import { useGlobalLoginUser } from "@/stores/auth/loginUser";
type AttendanceData = string[];

const getLocalYMD = (date: Date) => {
  // 항상 YYYY-MM-DD 형식 반환 (로컬 타임존 기준)
  return (
    date.getFullYear() +
    "-" +
    String(date.getMonth() + 1).padStart(2, "0") +
    "-" +
    String(date.getDate()).padStart(2, "0")
  );
};

export default function AttendancePage() {
  const [attendanceDates, setAttendanceDates] = useState<AttendanceData>([]);
  const [loading, setLoading] = useState(false);
  const { isLogin } = useGlobalLoginUser();

  // 2025년 4월의 첫날과 마지막날로 고정
  const firstDay = new Date(2025, 3, 1); // 4월은 3
  const lastDay = new Date(2025, 3 + 1, 0);

  useEffect(() => {
    if (!isLogin) return;
    setLoading(true);
    fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL}/api/attendances`, {
      credentials: "include",
    })
      .then((res) => res.json())
      .then((data) => {
        console.log("출석 기록:", data); // 👈 확인
        setAttendanceDates(data.data.attendanceDateList || []);
      })
      .finally(() => setLoading(false));
  }, [isLogin]);

  // 출석일에 도장 이모지 표시
  const tileContent = ({ date, view }: { date: Date; view: string }) => {
    if (view === "month") {
      const ymd = getLocalYMD(date);
      if (attendanceDates.length > 0 && attendanceDates.includes(ymd)) {
        return <span className="calendar-attendance-text">✅ 출석</span>;
      }
    }
    return null;
  };

  const tileClassName = ({ date, view }: { date: Date; view: string }) => {
    if (view === "month") {
      const ymd = getLocalYMD(date);
      if (attendanceDates.includes(ymd)) {
        return "bg-green-100 text-green-900 font-bold";
      }
    }
    return "";
  };

  if (!isLogin) return <div>로그인이 필요합니다.</div>;
  if (loading) return <div>출석 기록을 불러오는 중...</div>;

  return (
    <div className="max-w-lg mx-auto py-10">
      <h1 className="text-2xl font-bold mb-6 text-center">출석부</h1>
      <Calendar
        className="custom-calendar"
        tileClassName={tileClassName}
        tileContent={tileContent}
        locale="ko-KR"
        activeStartDate={firstDay}
        minDate={firstDay}
        maxDate={lastDay}
        prevLabel={null}
        nextLabel={null}
        prev2Label={null}
        next2Label={null}
        navigationLabel={({ date }) =>
          `${date.getFullYear()}년 ${date.getMonth() + 1}월`
        }
      />
      <style jsx global>{`
        .custom-calendar .react-calendar__tile {
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: flex-start;
          height: 100px;
          padding: 8px 0;
          position: relative;
        }
        .custom-calendar .react-calendar__month-view__days__day abbr {
          margin-bottom: 0.5rem;
          font-size: 1.1rem;
        }
        .calendar-attendance-text {
          color: #15803d;
          font-weight: 700;
          font-size: 0.95rem;
          width: 100%;
          height: 100%;
          display: flex;
          align-items: center;
          justify-content: center;
        }
        .custom-calendar {
          width: 700px !important;
          max-width: 100%;
          background: white;
          border: 1px solid #eee;
          border-radius: 8px;
          padding: 16px;
          box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
        }

        .custom-calendar .react-calendar__tile {
          position: relative;
          height: 100px;
          width: 200px; /* 타일 너비를 넓힘 */
          padding: 8px 0;
        }

        .react-calendar__tile.bg-green-100 {
          background: #bbf7d0 !important;
          color: #166534 !important;
        }

        .react-calendar__navigation button {
          font-size: 1.2rem;
          font-weight: bold;
          color: #333;
        }

        .react-calendar__navigation {
          margin-bottom: 16px;
        }
      `}</style>
    </div>
  );
}
