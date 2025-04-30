"use client";

import { useState, useRef, useEffect, useCallback } from "react";
import Link from "next/link";
import removeMarkdown from "remove-markdown";

type SearchResult = {
  id: number;
  title: string;
  content: string;
  nickname: string;
  profileImageUrl?: string;
  created: string;
  hashtags: string[];
};

export default function Page() {
  const [input, setInput] = useState("");
  const [hashtags, setHashtags] = useState<string[]>([]);
  const [results, setResults] = useState<SearchResult[]>([]);
  const [loading, setLoading] = useState(false);
  const [hasMore, setHasMore] = useState(true);
  const [lastId, setLastId] = useState<number>(0);
  const [searchStarted, setSearchStarted] = useState(false);
  const observer = useRef<IntersectionObserver | null>(null);
  const loadMoreRef = useRef<HTMLDivElement | null>(null);
  const inputRef = useRef<HTMLInputElement>(null);
  // 검색 실행
  const fetchSearch = useCallback(
    async (isFirst: boolean = false) => {
      if (loading) return;
      setLoading(true);
      try {
        const params = new URLSearchParams();
        if (input && isFirst) params.append("title", input.trim());
        hashtags.forEach((tag) => params.append("hashtags", tag));
        params.append("lastId", isFirst ? "0" : String(lastId));
        // size는 컨트롤러에서 기본 12, 필요시 추가
        const res = await fetch(
          `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/boards/search?${params}`
        );
        const data = await res.json();
        const boards: SearchResult[] = data.data || [];
        if (isFirst) {
          setResults(boards);
        } else {
          setResults((prev) => [...prev, ...boards]);
        }
        setHasMore(boards.length > 0);
        setLastId(boards.length > 0 ? boards[boards.length - 1].id : lastId);
      } catch {
        if (isFirst) setResults([]);
        setHasMore(false);
      } finally {
        setLoading(false);
      }
    },
    [input, hashtags, lastId, loading]
  );

  // 엔터 입력 처리
  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === "Enter") {
      if (input.startsWith("#")) {
        const tag = input.replace(/^#/, "").trim();
        if (tag && !hashtags.includes(tag)) {
          setHashtags([...hashtags, tag]);
        }
        setInput("");
      } else {
        setLastId(0);
        setSearchStarted(true);
        fetchSearch(true);
      }
    }
  };

  // 해시태그 삭제
  const removeHashtag = (tag: string) => {
    setHashtags(hashtags.filter((t) => t !== tag));
  };

  // 해시태그가 변경되면 검색 재시작
  useEffect(() => {
    if (searchStarted) {
      setLastId(0);
      fetchSearch(true);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [hashtags]);

  // 무한 스크롤 옵저버
  useEffect(() => {
    if (!searchStarted || !hasMore || loading) return;
    if (observer.current) observer.current.disconnect();
    observer.current = new window.IntersectionObserver((entries) => {
      if (entries[0].isIntersecting) {
        fetchSearch(false);
      }
    });
    if (loadMoreRef.current) {
      observer.current.observe(loadMoreRef.current);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [loadMoreRef, hasMore, loading, searchStarted, fetchSearch]);

  return (
    <div className="max-w-2xl mx-auto py-10">
      {/* 검색창 */}
      <div className="mb-8">
        <div className="relative">
          <input
            ref={inputRef}
            type="text"
            className="w-full border px-4 py-3 rounded text-lg focus:outline-none pl-10"
            placeholder='검색어를 입력하세요. (예: "제목" 또는 "#해시태그")'
            value={input}
            onChange={(e) => setInput(e.target.value)}
            onKeyDown={handleKeyDown}
          />
          <span className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400 pointer-events-none">
            <svg
              className="w-5 h-5"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <circle cx="11" cy="11" r="8" strokeWidth="2" />
              <path strokeWidth="2" d="M21 21l-3.5-3.5" />
            </svg>
          </span>
        </div>
        {/* 해시태그 리스트 */}
        {hashtags.length > 0 && (
          <div className="flex flex-wrap gap-2 mt-3">
            {hashtags.map((tag) => (
              <span
                key={tag}
                className="bg-emerald-100 text-emerald-700 px-3 py-1 rounded-full flex items-center"
              >
                #{tag}
                <button
                  className="ml-2 text-gray-400 hover:text-gray-700"
                  onClick={() => removeHashtag(tag)}
                  type="button"
                >
                  ×
                </button>
              </span>
            ))}
          </div>
        )}
      </div>

      {/* 검색 결과 */}
      {searchStarted && results.length === 0 && !loading && (
        <div className="text-center text-gray-400 py-10">
          검색 결과가 없습니다.
        </div>
      )}
      <div className="space-y-10">
        {results.map((item) => (
          <div key={item.id} className="flex gap-4 pb-10 border-b">
            <div className="flex-shrink-0">
              <Link href={`/${item.nickname}`}>
                {item.profileImageUrl ? (
                  <img
                    src={item.profileImageUrl}
                    alt={item.nickname}
                    className="w-12 h-12 rounded-full object-cover bg-gray-200 cursor-pointer"
                  />
                ) : (
                  <div className="w-12 h-12 rounded-full bg-gray-200 flex items-center justify-center text-2xl text-gray-500 font-bold cursor-pointer">
                    {item.nickname.charAt(0).toUpperCase()}
                  </div>
                )}
              </Link>
            </div>
            <div className="flex-1">
              <div className="flex items-center gap-2 mb-1">
                <Link href={`/${item.nickname}`}>
                  <span className="font-bold cursor-pointer">
                    {item.nickname}
                  </span>
                </Link>
              </div>
              <Link href={`/${item.nickname}/${item.id}`}>
                <div className="text-xl font-bold mb-1 cursor-pointer">
                  {item.title}
                </div>
                <div className="text-gray-600 mb-2 cursor-pointer">
                  {removeMarkdown(item.content).length > 100
                    ? removeMarkdown(item.content).slice(0, 100) + "..."
                    : removeMarkdown(item.content)}
                </div>
              </Link>
              <div className="flex gap-2 mb-2">
                {item.hashtags.map((tag) => (
                  <span
                    key={tag}
                    className="bg-gray-100 text-gray-600 px-2 py-1 rounded-full text-xs"
                  >
                    #{tag}
                  </span>
                ))}
              </div>
              <div className="text-gray-400 text-sm">
                {new Date(item.created).toLocaleDateString()}
              </div>
            </div>
          </div>
        ))}
      </div>
      {/* 무한 스크롤 로딩/더보기 */}
      <div ref={loadMoreRef} />
      {loading && (
        <div className="text-center text-gray-400 py-6">검색 중...</div>
      )}
      {!hasMore && searchStarted && results.length > 0 && (
        <div className="text-center text-gray-400 py-6">
          더 이상 결과가 없습니다.
        </div>
      )}
    </div>
  );
}
