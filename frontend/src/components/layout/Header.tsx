import Image from "next/image";
import Link from "next/link";

export default function Header() {
  return (
    <header className="flex justify-between items-center mb-8">
      <div className="flex items-center gap-2">
        <Image src="/next.svg" alt="Five Guys 로고" width={30} height={30} />
        <span className="text-rose-500 font-bold">마이페이지</span>
      </div>
      <div className="flex gap-2">
        <div className="relative">
          <input
            type="text"
            placeholder="검색"
            className="border rounded-md py-1 px-3 pr-8 focus:outline-none"
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
        <button className="bg-rose-500 text-white px-4 py-1 rounded-md hover:bg-rose-600 transition">
          로그인
        </button>
        <button className="border border-gray-300 px-4 py-1 rounded-md hover:bg-gray-50 transition">
          회원가입
        </button>
      </div>
    </header>
  );
}
