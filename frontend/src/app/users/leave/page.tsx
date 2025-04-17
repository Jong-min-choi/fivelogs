"use client";
import { useState } from "react";
import Layout from "@/components/layout/Layout";
import Link from "next/link";
import Image from "next/image";

export default function LeavePage() {
  const [password, setPassword] = useState("");
  const [agreeTerms, setAgreeTerms] = useState(false);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!agreeTerms) {
      alert("유의사항에 동의해주세요.");
      return;
    }
    // 회원탈퇴 로직 구현
    console.log("회원탈퇴 제출:", { password, agreeTerms });
  };

  return (
    <Layout>
      <div className="max-w-md mx-auto my-8 p-6 bg-white rounded-lg shadow-sm">
        <h1 className="text-2xl font-bold text-center mb-8">회원탈퇴</h1>

        <ul className="mb-6 pl-5 space-y-2">
          <li className="flex items-start">
            <span className="mr-2">•</span>
            <p>회원탈퇴 시 유의사항</p>
          </li>
          <li className="flex items-start">
            <span className="mr-2">•</span>
            <p>회원탈퇴 시 모든 데이터는 즉시 삭제되며 복구가 불가능합니다.</p>
          </li>
          <li className="flex items-start">
            <span className="mr-2">•</span>
            <p>작성하신 게시물과 댓글은 삭제되지 않습니다.</p>
          </li>
          <li className="flex items-start">
            <span className="mr-2">•</span>
            <p>탈퇴 후 동일한 아이디로 재가입이 가능합니다.</p>
          </li>
        </ul>

        <form onSubmit={handleSubmit}>
          <div className="mb-6">
            <label
              htmlFor="password"
              className="block mb-2 text-sm font-medium"
            >
              비밀번호 확인
            </label>
            <input
              type="password"
              id="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="w-full p-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-rose-300"
              placeholder="비밀번호를 입력하세요"
              required
            />
          </div>

          <div className="mb-6">
            <label className="flex items-center">
              <input
                type="checkbox"
                checked={agreeTerms}
                onChange={(e) => setAgreeTerms(e.target.checked)}
                className="mr-2"
              />
              <span className="text-sm">
                위 유의사항을 모두 확인하였으며, 이에 동의합니다.
              </span>
            </label>
          </div>

          <button
            type="submit"
            className="w-full py-3 bg-red-500 text-white rounded hover:bg-red-600 transition mb-4"
          >
            회원탈퇴
          </button>

          <div className="flex justify-center">
            <Link href="/" className="text-sm text-gray-500 hover:underline">
              취소하기
            </Link>
          </div>
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
    </Layout>
  );
}
