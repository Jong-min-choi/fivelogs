"use client";
import { useState } from "react";
import Layout from "@/app/ClientLayout";
import Link from "next/link";
import Image from "next/image";
import { useRouter } from "next/navigation";

export default function LoginPage() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [rememberMe, setRememberMe] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState("");
  const router = useRouter();

  const socialLoginForKakaoUrl = `${process.env.NEXT_PUBLIC_API_BASE_URL}/oauth2/authorization/kakao`;
  const redirectUrlAfterSocialLogin = process.env.NEXT_PUBLIC_FRONT_BASE_URL;

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      setIsLoading(true);
      setError("");

      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/users/login`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            email,
            password,
            rememberMe,
          }),
          credentials: "include", // ★ 이거 필수!
        }
      );

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "로그인에 실패했습니다.");
      }

      const data = await response.json();

      window.location.href = "/";
    } catch (err) {
      console.error("로그인 오류:", err);
      setError(
        err instanceof Error ? err.message : "로그인 중 오류가 발생했습니다."
      );
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <>
      <div className="max-w-md mx-auto my-8 p-6 bg-white rounded-lg shadow-sm">
        <h1 className="text-2xl font-bold text-center mb-8">로그인</h1>

        {error && (
          <div className="mb-4 p-2 bg-red-100 text-red-700 rounded">
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit}>
          <div className="mb-4">
            <label htmlFor="email" className="block mb-2 text-sm font-medium">
              이메일
            </label>
            <input
              type="email"
              id="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="w-full p-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-rose-300"
              placeholder="이메일을 입력하세요"
              required
            />
          </div>

          <div className="mb-4">
            <label
              htmlFor="password"
              className="block mb-2 text-sm font-medium"
            >
              비밀번호
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

          <div className="flex justify-between mb-4 text-sm">
            <div className="flex items-center">
              <Link
                href="/users/find/id"
                className="text-rose-400 hover:text-rose-500"
              >
                이메일 찾기
              </Link>
              <span className="mx-2 text-gray-300">|</span>
              <Link
                href="/users/find/password"
                className="text-rose-400 hover:text-rose-500"
              >
                비밀번호 찾기
              </Link>
            </div>
          </div>

          <button
            type="submit"
            className="w-full py-3 bg-rose-400 text-white rounded hover:bg-rose-500 transition"
            disabled={isLoading}
          >
            {isLoading ? "로그인 중..." : "로그인"}
          </button>
        </form>

        <div className="border-t border-gray-200 my-6 relative">
          <span className="absolute top-0 left-1/2 transform -translate-x-1/2 -translate-y-1/2 bg-white px-4 text-sm text-gray-500">
            간편 로그인
          </span>
        </div>

        <button
          type="button"
          className="w-full py-3 bg-yellow-300 text-gray-800 rounded flex items-center justify-center font-medium hover:bg-yellow-200 transition"
        >
          <span className="mr-2">
            <svg viewBox="0 0 24 24" width="20" height="20">
              <path
                d="M12 3c-4.97 0-9 4.03-9 9s4.03 9 9 9 9-4.03 9-9-4.03-9-9-9z"
                fill="#FEE500"
              />
              <path
                d="M12.89 12.1c-.15 0-.28.12-.28.27s.13.28.28.28c.15 0 .28-.13.28-.28s-.12-.28-.28-.28z"
                fill="#3C1E1E"
              />
              <path
                d="M9.02 12.1c-.15 0-.28.12-.28.27s.13.28.28.28.28-.13.28-.28-.12-.28-.28-.28z"
                fill="#3C1E1E"
              />
              <path
                d="M14.18 14.33c-.59.37-1.33.57-2.09.57-.75 0-1.5-.2-2.09-.57-.12-.08-.28-.04-.35.09-.08.12-.04.28.09.35.68.43 1.52.66 2.35.66.84 0 1.68-.23 2.35-.66.12-.08.16-.23.09-.35-.08-.12-.23-.16-.35-.09z"
                fill="#3C1E1E"
              />
            </svg>
          </span>
          <a
            href={`${socialLoginForKakaoUrl}?redirectUrl=${redirectUrlAfterSocialLogin}`}
          >
            카카오톡으로 시작하기
          </a>
        </button>

        <div className="mt-4 text-center text-sm text-gray-500">
          아직 계정이 없으신가요?{" "}
          <Link href="/users/join" className="text-rose-500 hover:underline">
            회원가입
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
    </>
  );
}
