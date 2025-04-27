"use client";

import { useState } from "react";
import SearchResultList from "./SearchResultList"

interface SearchBarProps {
  onSearch: (keyword: string, type: "title" | "comment") => void;
}

export default function SearchBar({ onSearch }: SearchBarProps) {
  const [keyword, setKeyword] = useState("");
  const [searchType, setSearchType] = useState<"title" | "comment">("title");

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!keyword.trim()) {
      alert("검색어를 입력하세요.");
      return;
    }
    onSearch(keyword, searchType);
  };

  return (
    <div>
    <form onSubmit={handleSubmit} className="flex gap-2 items-center mb-6">
    <select
        value={searchType}
        onChange={(e) => setSearchType(e.target.value as "title" | "comment")}
        className="border p-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-rose-500"
      >
        <option value="title">제목</option>
        <option value="comment">댓글</option>
      </select>
      <input
        type="text"
        placeholder="검색어를 입력하세요"
        value={keyword}
        onChange={(e) => setKeyword(e.target.value)}
        className="border p-2 rounded-lg w-60 focus:outline-none focus:ring-2 focus:ring-rose-500"
      />
      <button
        type="submit"
        className="px-4 py-2 bg-rose-500 text-white rounded-lg hover:bg-rose-600"
      >
        검색
      </button>
    </form>

    {/* 🔥 검색 결과 */}
    {keyword && (
      <div className="w-full max-w-2xl mt-8">
        <SearchResultList keyword={keyword} searchType={searchType} />
      </div>
    )}
    </div>
  );
}