"use client";
import { useState } from "react";
import Layout from "@/app/ClientLayout";
import Link from "next/link";
import Image from "next/image";

export default function editPage() {
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    // 게시글 작성 로직 구현
    console.log({ title, content });
  };

  return (
    <>
      <div className="max-w-4xl mx-auto my-8 p-6 bg-white rounded-lg shadow-sm">
        <h1 className="text-2xl font-bold mb-8">게시글 수정</h1>

        <form onSubmit={handleSubmit}>
          <div className="mb-4">
            <label htmlFor="title" className="block mb-2 text-sm font-medium">
              제목
            </label>
            <input
              type="text"
              id="title"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              className="w-full p-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-rose-300"
              placeholder="제목을 입력하세요"
              required
            />
          </div>

          <div className="mb-6">
            <label htmlFor="content" className="block mb-2 text-sm font-medium">
              내용
            </label>
            <textarea
              id="content"
              value={content}
              onChange={(e) => setContent(e.target.value)}
              className="w-full p-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-rose-300 min-h-[200px]"
              placeholder="내용을 입력하세요"
              required
            />
          </div>

          <div className="flex justify-end space-x-2">
            <button
              type="button"
              className="px-4 py-2 border border-gray-300 rounded hover:bg-gray-100 transition"
            >
              취소
            </button>
            <button
              type="submit"
              className="px-4 py-2 bg-rose-400 text-white rounded hover:bg-rose-500 transition"
            >
              수정하기
            </button>
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
    </>
  );
}
