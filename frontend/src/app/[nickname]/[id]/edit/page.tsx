"use client";
import { useState, useEffect } from "react";
import Link from "next/link";
import Image from "next/image";
import { useParams, useRouter } from "next/navigation";

export default function EditPage() {
  const params = useParams();
  const router = useRouter();
  const boardId = params?.id;
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [loading, setLoading] = useState(true);
  const [status, setStatus] = useState("PUBLIC");
  const [hashtagInput, setHashtagInput] = useState("");
  const [hashtags, setHashtags] = useState<string[]>([]);

  useEffect(() => {
    const fetchPost = async () => {
      try {
        const response = await fetch(
          `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/boards/${boardId}`,
          {
            credentials: "include",
          }
        );
        if (!response.ok) {
          throw new Error("게시글을 불러오는데 실패했습니다.");
        }
        const data = await response.json();
        if (data.success) {
          setTitle(data.data.boardTitle);
          setContent(data.data.content);
          setStatus(data.data.status || "PUBLIC");
          setHashtags(
            data.data.hashtags && Array.isArray(data.data.hashtags)
              ? data.data.hashtags
              : []
          );
        }
      } catch (error) {
        console.error("게시글 불러오기 오류:", error);
        alert("게시글을 불러오는데 실패했습니다.");
      } finally {
        setLoading(false);
      }
    };

    if (boardId) {
      fetchPost();
    }
  }, [boardId]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      // 해시태그 전처리: 중복 제거 및 정규화
      const processedHashtags = Array.from(
        new Set(
          hashtags.map((tag) => {
            let normalizedTag = tag.trim().toLowerCase();
            if (!normalizedTag.startsWith("#")) {
              normalizedTag = `${normalizedTag}`;
            }
            return normalizedTag.replace(/\s+/g, "");
          })
        )
      );

      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/boards/${boardId}/edit`,
        {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
          },
          credentials: "include",
          body: JSON.stringify({
            title: title.trim(),
            content: content.trim(),
            status: status,
            hashtags: processedHashtags,
          }),
        }
      );

      const data = await response.json();

      if (!response.ok) {
        throw new Error(data.message || "게시글 수정에 실패했습니다.");
      }

      if (data.success) {
        router.back();
      } else {
        throw new Error(data.message || "게시글 수정에 실패했습니다.");
      }
    } catch (error) {
      console.error("수정 오류:", error);
      alert(
        error instanceof Error ? error.message : "게시글 수정에 실패했습니다."
      );
    }
  };

  const handleHashtagInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    // 입력 시 첫 글자가 #이면 제거
    setHashtagInput(e.target.value.replace(/^#/, ""));
  };

  const handleAddHashtag = () => {
    if (hashtagInput.trim()) {
      // 해시태그 정규화: 앞의 # 제거
      let newTag = hashtagInput.trim().toLowerCase();
      newTag = newTag.startsWith("#") ? newTag.slice(1) : newTag; // # 제거
      newTag = newTag.replace(/\s+/g, "");

      // 빈 해시태그 체크
      if (newTag.length === 0) {
        alert("유효한 해시태그를 입력해주세요.");
        setHashtagInput("");
        return;
      }

      // 중복 체크
      if (!hashtags.includes(newTag)) {
        setHashtags((prev) => [...prev, newTag]);
      } else {
        alert("이미 존재하는 해시태그입니다.");
      }
      setHashtagInput("");
    }
  };

  const handleHashtagInputKeyDown = (
    e: React.KeyboardEvent<HTMLInputElement>
  ) => {
    if (e.key === "Enter") {
      e.preventDefault();
      handleAddHashtag();
    }
  };

  const removeHashtag = (tagToRemove: string) => {
    setHashtags((prev) => prev.filter((tag) => tag !== tagToRemove));
  };

  if (loading) {
    return <div className="text-center py-8">로딩 중...</div>;
  }

  return (
    <>
      <div className="max-w-4xl mx-auto my-8 p-6 bg-white rounded-lg shadow-sm">
        <h1 className="text-2xl font-bold mb-8">게시글 수정</h1>

        <form onSubmit={handleSubmit} className="space-y-4">
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
              className="w-full p-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-rose-300 min-h-[400px]"
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
                onChange={handleHashtagInputChange}
                onKeyDown={handleHashtagInputKeyDown}
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
            <button
              type="button"
              onClick={() => router.back()}
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
