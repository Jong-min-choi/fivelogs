"use client";
import { useState } from "react";
import Link from "next/link";
import Image from "next/image";
import { useRouter } from "next/navigation";

export default function FindIdPage() {
  const [nickname, setNickname] = useState("");
  const router = useRouter();
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await fetch(
        `${
          process.env.NEXT_PUBLIC_API_BASE_URL
        }/api/users/nickname/${encodeURIComponent(nickname)}/email`,
        {
          method: "GET",
          credentials: "include",
        }
      );
      if (!response.ok) {
        throw new Error("아이디(이메일) 찾기 요청 실패");
      }
      const data = await response.json();
      // 예시: data.email이 아이디(이메일)라고 가정
      alert(`아이디(이메일): ${data.data.email}`);
      router.push("/users/login");
    } catch (err) {
      alert("아이디(이메일) 찾기에 실패했습니다.");
    }
  };

  return (
    <>
      <div className="max-w-md mx-auto my-8 p-6 bg-white rounded-lg shadow-sm">
        <h1 className="text-2xl font-bold text-center mb-8">아이디 찾기</h1>

        <form onSubmit={handleSubmit}>
          <div className="mb-6">
            <label
              htmlFor="nickname"
              className="block mb-2 text-sm font-medium"
            >
              닉네임
            </label>
            <input
              type="text"
              id="nickname"
              value={nickname}
              onChange={(e) => setNickname(e.target.value)}
              className="w-full p-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-rose-300"
              placeholder="닉네임을 입력하세요"
              required
            />
          </div>

          <button
            type="submit"
            className="w-full py-3  bg-rose-400 text-white rounded hover:bg-rose-500 transition mb-4"
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
    </>
  );
}
