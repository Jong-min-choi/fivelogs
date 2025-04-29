"use client";

import { useEffect, useState } from "react";
import Calendar from "react-calendar";
import "react-calendar/dist/Calendar.css";
import { useGlobalLoginUser } from "@/stores/auth/loginUser";
type AttendanceData = string[];

const getLocalYMD = (date: Date) =>
  date.getFullYear() +
  "-" +
  String(date.getMonth() + 1).padStart(2, "0") +
  "-" +
  String(date.getDate()).padStart(2, "0");

export default function AttendanceCalendar() {
  const [attendanceDates, setAttendanceDates] = useState<AttendanceData>([]);
  const [loading, setLoading] = useState(false);
  const { isLogin } = useGlobalLoginUser();

  // 현재 연도 기준으로 달력 범위 설정
  const now = new Date();
  const firstDay = new Date(now.getFullYear(), 0, 1); // 1월 1일
  const lastDay = new Date(now.getFullYear(), 11, 31); // 12월 31일

  useEffect(() => {
    if (!isLogin) return;
    setLoading(true);
    fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL}/api/attendances`, {
      credentials: "include",
    })
      .then((res) => res.json())
      .then((data) => {
        setAttendanceDates(data.data.attendanceDateList || []);
      })
      .finally(() => setLoading(false));
  }, [isLogin]);

  const tileContent = ({ date, view }: { date: Date; view: string }) => {
    if (view === "month") {
      const ymd = getLocalYMD(date);
      if (attendanceDates.includes(ymd)) {
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
    <div className="mb-8">
      <h3 className="text-lg font-bold mb-3 text-center">출석부</h3>
      <Calendar
        className="custom-calendar"
        tileClassName={tileClassName}
        tileContent={tileContent}
        locale="ko-KR"
        minDate={firstDay}
        maxDate={lastDay}
        prev2Label={null}
        next2Label={null}
        navigationLabel={({ date }) =>
          `${date.getFullYear()}년 ${date.getMonth() + 1}월`
        }
      />
      <style jsx global>{`
        .custom-calendar {
          width: 700px !important;
          max-width: 100%;
          background: white;
          border: 1px solid #eee;
          border-radius: 8px;
          padding: 12px;
          box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
        }
        .custom-calendar .react-calendar__tile {
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: flex-start;
          height: 100px;
          min-width: 40px;
          padding: 4px 0;
          position: relative;
        }
        .custom-calendar .react-calendar__month-view__days__day abbr {
          margin-bottom: 0.3rem;
          font-size: 1rem;
        }
        .calendar-attendance-text {
          color: #15803d;
          font-weight: 700;
          font-size: 0.9rem;
          width: 100%;
          display: flex;
          align-items: center;
          justify-content: center;
        }
        .react-calendar__tile.bg-green-100 {
          background: #bbf7d0 !important;
          color: #166534 !important;
        }
      `}</style>
    </div>
  );
}
