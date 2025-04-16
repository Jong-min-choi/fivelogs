"use client";
import { useState } from "react";
import Layout from "@/components/layout/Layout";
import Link from "next/link";
import Image from "next/image";

export default function FindIdPage() {
  const [phoneNumber, setPhoneNumber] = useState("");
  const [authCode, setAuthCode] = useState("");
  const [isAuthCodeSent, setIsAuthCodeSent] = useState(false);
  const [isVerified, setIsVerified] = useState(false);

  const handleSendAuthCode = () => {
    if (phoneNumber) {
      // 인증번호 전송 로직 구현
      setIsAuthCodeSent(true);
      console.log("인증번호 전송: ", phoneNumber);
    }
  };

  const handleVerifyAuthCode = () => {
    if (authCode) {
      // 인증번호 확인 로직 구현
      setIsVerified(true);
      console.log("인증번호 확인: ", authCode);
    }
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    // 아이디 찾기 로직 구현
    console.log("아이디 찾기 제출");
  };

  return (
    <Layout>
      <div className="max-w-md mx-auto my-8 p-6 bg-white rounded-lg shadow-sm">
        <h1 className="text-2xl font-bold text-center mb-8">아이디 찾기</h1>

        <form onSubmit={handleSubmit}>
          <div className="mb-4">
            <label
              htmlFor="phoneNumber"
              className="block mb-2 text-sm font-medium"
            >
              휴대폰 번호
            </label>
            <div className="flex space-x-2">
              <input
                type="text"
                id="phoneNumber"
                value={phoneNumber}
                onChange={(e) => setPhoneNumber(e.target.value)}
                className="flex-1 p-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-rose-300"
                placeholder="휴대폰 번호를 입력하세요"
                required
              />
              <button
                type="button"
                onClick={handleSendAuthCode}
                className="px-4 py-2 bg-rose-400 text-white rounded hover:bg-rose-500 transition"
              >
                인증요청
              </button>
            </div>
          </div>

          <div className="mb-6">
            <label
              htmlFor="authCode"
              className="block mb-2 text-sm font-medium"
            >
              인증코드
            </label>
            <div className="flex space-x-2">
              <input
                type="text"
                id="authCode"
                value={authCode}
                onChange={(e) => setAuthCode(e.target.value)}
                className="flex-1 p-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-rose-300"
                placeholder="인증코드를 입력하세요"
                required
              />
              <button
                type="button"
                onClick={handleVerifyAuthCode}
                className="px-4 py-2 bg-rose-400 text-white rounded hover:bg-rose-500 transition"
              >
                코드 확인
              </button>
            </div>
          </div>

          <button
            type="submit"
            className="w-full py-3 bg-rose-400 text-white rounded hover:bg-rose-500 transition mb-4"
          >
            아이디 찾기
          </button>
        </form>

        <div className="flex justify-center space-x-4 text-sm text-rose-400">
          <Link href="/users/login" className="hover:underline">
            로그인
          </Link>
          <span className="text-gray-300">|</span>
          <Link href="/users/find/password" className="hover:underline">
            비밀번호 찾기
          </Link>
        </div>
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
