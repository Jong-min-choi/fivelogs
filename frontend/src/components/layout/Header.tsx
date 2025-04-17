"use client";
import Image from "next/image";
import Link from "next/link";
import { useState } from "react";

export default function Header() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);

  const toggleDropdown = () => {
    setIsDropdownOpen(!isDropdownOpen);
  };

  return (
    <header className="flex justify-between items-center mb-8">
      <div className="flex items-center gap-2">
        <Image src="/next.svg" alt="Five Guys 로고" width={30} height={30} />
        <span className="text-rose-500 font-bold">마이페이지</span>
      </div>
      <div className="flex gap-2 items-center">
        <div className="relative">
          <input
            type="text"
            placeholder="검색"
            className="border rounded-md py-1 px-3 pr-8 focus:outline-none w-64"
          />
          <button className="absolute right-2 top-1/2 transform -translate-y-1/2">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              width="16"
              height="16"
              fill="currentColor"
              className="bi bi-search"
              viewBox="0 0 16 16"
            >
              <path d="M11.742 10.344a6.5 6.5 0 1 0-1.397 1.398h-.001c.03.04.062.078.098.115l3.85 3.85a1 1 0 0 0 1.415-1.414l-3.85-3.85a1.007 1.007 0 0 0-.115-.1zM12 6.5a5.5 5.5 0 1 1-11 0 5.5 5.5 0 0 1 11 0z" />
            </svg>
          </button>
        </div>

        {isLoggedIn ? (
          <div className="relative">
            <div
              className="flex items-center gap-2 cursor-pointer"
              onClick={toggleDropdown}
            >
              <div className="w-8 h-8 rounded-full bg-gray-300 overflow-hidden">
                <Image
                  src="/next.svg"
                  alt="프로필 이미지"
                  width={32}
                  height={32}
                  className="object-cover"
                />
              </div>
              <span className="font-medium">김개발</span>
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="16"
                height="16"
                fill="currentColor"
                className={`transition-transform ${
                  isDropdownOpen ? "rotate-180" : ""
                }`}
                viewBox="0 0 16 16"
              >
                <path d="M7.247 11.14 2.451 5.658C1.885 5.013 2.345 4 3.204 4h9.592a1 1 0 0 1 .753 1.659l-4.796 5.48a1 1 0 0 1-1.506 0z" />
              </svg>
            </div>

            {isDropdownOpen && (
              <div className="absolute right-0 mt-2 w-48 bg-white rounded-md shadow-lg py-1 z-10 border border-gray-200">
                <Link
                  href="/users/mypage"
                  className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                >
                  마이 페이지
                </Link>
                <Link
                  href="/blogname"
                  className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                >
                  마이 블로그
                </Link>
                <Link
                  href="/write"
                  className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                >
                  글쓰기
                </Link>
                <hr className="my-1" />
                <button
                  onClick={() => setIsLoggedIn(false)}
                  className="block w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                >
                  로그아웃
                </button>
              </div>
            )}
          </div>
        ) : (
          <>
            <button
              className="bg-rose-500 text-white px-4 py-1 rounded-md hover:bg-rose-600 transition"
              onClick={() => setIsLoggedIn(true)}
            >
              로그인
            </button>
            <button className="bg-green-500 text-white font-bold px-4 py-2 rounded-md hover:bg-green-600 transition">
              <Link
                href="/users/join"
                className="block text-sm hover:text-gray-100"
              >
                회원가입
              </Link>
            </button>
          </>
        )}
      </div>
    </header>
  );
}
