"use client";
import { useState, KeyboardEvent } from "react";
import Layout from "@/app/ClientLayout";
import Link from "next/link";
import Image from "next/image";
import { useRouter } from "next/navigation";

export default function WritePage() {
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [hashtagInput, setHashtagInput] = useState("");
  const [hashtags, setHashtags] = useState<string[]>([]);
  const [status, setStatus] = useState("PUBLIC"); // 기본값은 공개
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState("");
  const router = useRouter();

  const handleAddHashtag = () => {
    const cleanTag = hashtagInput.trim().replace(/^#/, ""); // 앞의 # 제거
    if (cleanTag && !hashtags.includes(cleanTag)) {
      setHashtags([...hashtags, cleanTag]);
      setHashtagInput("");
    }
  };

  const handleHashtagKeyDown = (e: KeyboardEvent<HTMLInputElement>) => {
    if (e.key === "Enter") {
      e.preventDefault();
      handleAddHashtag();
    }
  };

  const removeHashtag = (tagToRemove: string) => {
    setHashtags(hashtags.filter((tag) => tag !== tagToRemove));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!title.trim()) {
      setError("제목을 입력해주세요");
      return;
    }

    if (!content.trim()) {
      setError("내용을 입력해주세요");
      return;
    }

    try {
      setIsLoading(true);
      setError("");

      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/boards`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          credentials: "include", // 인증 정보 포함
          body: JSON.stringify({
            title,
            content,
            hashtags,
            status,
          }),
        }
      );

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "게시글 작성에 실패했습니다.");
      }

      const data = await response.json();
      console.log("게시글 작성 성공:", data);

      // 게시글 작성 성공 후 해당 게시글 페이지나 목록 페이지로 이동
      alert("게시글이 성공적으로 등록되었습니다.");
      router.push("/");
    } catch (err) {
      console.error("게시글 작성 오류:", err);
      setError(
        err instanceof Error
          ? err.message
          : "게시글 작성 중 오류가 발생했습니다."
      );
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <>
      <div className="max-w-4xl mx-auto my-8 p-6 bg-white rounded-lg shadow-sm">
        <h1 className="text-2xl font-bold mb-8">새 게시글 작성</h1>

        {error && (
          <div className="mb-4 p-2 bg-red-100 text-red-700 rounded">
            {error}
          </div>
        )}

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

          <div className="mb-4">
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

          <div className="mb-4">
            <label htmlFor="status" className="block mb-2 text-sm font-medium">
              공개 설정
            </label>
            <div className="flex gap-4">
              <label className="flex items-center">
                <input
                  type="radio"
                  name="status"
                  value="PUBLIC"
                  checked={status === "PUBLIC"}
                  onChange={() => setStatus("PUBLIC")}
                  className="mr-2"
                />
                <span>공개</span>
              </label>
              <label className="flex items-center">
                <input
                  type="radio"
                  name="status"
                  value="PRIVATE"
                  checked={status === "PRIVATE"}
                  onChange={() => setStatus("PRIVATE")}
                  className="mr-2"
                />
                <span>비공개</span>
              </label>
            </div>
          </div>

          <div className="mb-6">
            <label
              htmlFor="hashtags"
              className="block mb-2 text-sm font-medium"
            >
              해시태그
            </label>
            <div className="flex flex-wrap items-center gap-2 mb-2">
              {hashtags.map((tag, index) => (
                <div
                  key={index}
                  className="flex items-center bg-rose-100 text-rose-800 px-2 py-1 rounded text-sm"
                >
                  #{tag}
                  <button
                    type="button"
                    className="ml-1 text-rose-600 hover:text-rose-800"
                    onClick={() => removeHashtag(tag)}
                  >
                    &times;
                  </button>
                </div>
              ))}
            </div>
            <div className="flex gap-2">
              <input
                type="text"
                id="hashtags"
                value={hashtagInput}
                onChange={(e) =>
                  setHashtagInput(e.target.value.replace(/^#/, ""))
                } // 입력 시 # 자동 제거
                onKeyDown={handleHashtagKeyDown}
                className="flex-1 p-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-rose-300"
                placeholder="해시태그 입력 후 Enter 또는 추가 버튼을 클릭하세요"
              />
              <button
                type="button"
                onClick={handleAddHashtag}
                className="px-4 py-2 bg-gray-200 rounded hover:bg-gray-300 transition"
              >
                추가
              </button>
            </div>
          </div>

          <div className="flex justify-end space-x-2">
            <Link href="/">
              <button
                type="button"
                className="px-4 py-2 border border-gray-300 rounded hover:bg-gray-100 transition"
              >
                취소
              </button>
            </Link>
            <button
              type="submit"
              className="px-4 py-2 bg-rose-400 text-white rounded hover:bg-rose-500 transition"
              disabled={isLoading}
            >
              {isLoading ? "게시 중..." : "게시하기"}
            </button>
          </div>
        </form>
      </div>
    </>
  );
}
