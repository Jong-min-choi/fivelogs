"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";

interface SearchResult {
  boardId: number;  // ✅ 게시글 ID
  boardTitle: string;
  comment: string;
}

interface SearchResultListProps {
  keyword: string;
  searchType: "title" | "comment";  // ✅ 검색 타입 추가
}

export default function SearchResultList({ keyword, searchType }: SearchResultListProps) {
  const [results, setResults] = useState<SearchResult[]>([]);
  const [loading, setLoading] = useState(false);
  const router = useRouter();

  useEffect(() => {
    if (!keyword) return;

    const fetchResults = async () => {
      setLoading(true);
      try {
        const endpoint =
          searchType === "title"
            ? `/api/boards/search?keyword=${encodeURIComponent(keyword)}`
            : `/api/boards/search-by-comment?keyword=${encodeURIComponent(keyword)}`;

        const res = await fetch(endpoint);

        if (!res.ok) throw new Error("검색 실패");

        const json = await res.json();
        setResults(json.data.content || []); // Page 형태면 content에서
      } catch (err) {
        console.error("❌ 검색 실패:", err);
      } finally {
        setLoading(false);
      }
    };

    fetchResults();
  }, [keyword, searchType]);

  const handleClick = (boardId: number) => {
    router.push(`/board/${boardId}`); // ✅ 클릭 시 해당 게시물로 이동
  };

  return (
    <div className="space-y-4">
      {loading ? (
        <p className="text-center text-gray-500">검색 중...</p>
      ) : results.length === 0 ? (
        <p className="text-center text-gray-500">검색 결과가 없습니다.</p>
      ) : (
        results.map((result, index) => (
          <div
            key={index}
            className="p-4 border rounded-lg hover:bg-gray-50 cursor-pointer transition-all"
            onClick={() => handleClick(result.boardId)}
          >
            <p className="font-bold text-rose-500">{result.boardTitle}</p>
            {searchType === "comment" && (
              <p className="text-gray-600 mt-1 text-sm">{result.comment}</p> // 🔥 댓글 검색일 때만 댓글 표시
            )}
          </div>
        ))
      )}
    </div>
  );
}