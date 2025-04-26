"use client";
import { useState } from "react";
import Link from "next/link";
import Image from "next/image";
import { useRouter } from "next/navigation";
export default function ChangePasswordPage() {
  const [currentPassword, setCurrentPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const router = useRouter();
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (newPassword !== confirmPassword) {
      alert("비밀번호가 일치하지 않습니다.");
      return;
    }
    try {
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/users/change-password`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          credentials: "include",
          body: JSON.stringify({
            currentPassword,
            newPassword,
            confirmPassword,
          }),
        }
      );
      const data = await response.json();
      if (!response.ok) {
        alert(data.message || "비밀번호 변경에 실패했습니다.");
        return;
      }
      alert(data.message || "비밀번호가 성공적으로 변경되었습니다.");
      router.push("/users/mypage");
    } catch (err) {
      alert("비밀번호 변경 중 오류가 발생했습니다.");
    }
  };

  return (
    <>
      <div className="max-w-md mx-auto my-8 p-6 bg-white rounded-lg shadow-sm">
        <h1 className="text-2xl font-bold text-center mb-8">비밀번호 재설정</h1>

        <form onSubmit={handleSubmit}>
          <div className="mb-6">
            <label
              htmlFor="currentPassword"
              className="block mb-2 text-sm font-medium"
            >
              기존 비밀번호
            </label>
            <input
              type="password"
              id="currentPassword"
              value={currentPassword}
              onChange={(e) => setCurrentPassword(e.target.value)}
              className="w-full p-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-rose-300"
              placeholder="기존 비밀번호를 입력하세요"
              required
            />
          </div>

          <div className="mb-6">
            <label
              htmlFor="newPassword"
              className="block mb-2 text-sm font-medium"
            >
              새 비밀번호
            </label>
            <input
              type="password"
              id="newPassword"
              value={newPassword}
              onChange={(e) => setNewPassword(e.target.value)}
              className="w-full p-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-rose-300"
              placeholder="비밀번호를 입력하세요"
              required
            />
          </div>

          <div className="mb-8">
            <label
              htmlFor="confirmPassword"
              className="block mb-2 text-sm font-medium"
            >
              비밀번호 확인
            </label>
            <input
              type="password"
              id="confirmPassword"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              className="w-full p-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-rose-300"
              placeholder="비밀번호를 다시 입력하세요"
              required
            />
          </div>

          <button
            type="submit"
            className="w-full py-3 bg-rose-400 text-white rounded hover:bg-rose-500 transition mb-4"
          >
            비밀번호 재설정
          </button>
        </form>
      </div>

      <div className="flex justify-center mt-4">
        <Link href="/" className="inline-block mx-auto">
          <Image
            src="/next.svg"
            alt="Five Guys Logo"
            width={120}
            height={80}
            className="object-contain"
          />
        </Link>
      </div>

      <div className="text-center text-sm text-gray-500 mt-4 mb-8">
        개발자들의 지식과 경험을 공유하는 공간
      </div>

      <div className="text-center text-xs text-gray-400 mt-8">
        © 2024 FIVE Log. All rights reserved.
      </div>

      <style jsx>{`
        .custom-calendar {
          width: 900px !important;
          max-width: 100%;
          background: white;
          border: 1px solid #eee;
          border-radius: 8px;
          padding: 16px;
          box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
        }

        .custom-calendar .react-calendar__tile {
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: flex-start;
          min-width: 110px;
          height: 100px;
          padding: 8px 0;
          position: relative;
        }
      `}</style>
    </>
  );
}
