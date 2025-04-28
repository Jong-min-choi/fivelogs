"use client";

import { useState } from "react";
import SearchBar from './SearchBar';
import SearchResultList from './SearchResultList';

export default function SearchPage() {
  const [keyword, setKeyword] = useState("");
  const [searchType, setSearchType] = useState<"title" | "comment">("title");

  const handleSearch = (keyword: string, type: "title" | "comment") => {
    setKeyword(keyword);
    setSearchType(type);
  };

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-2xl font-bold mb-8">검색</h1>

      <SearchBar onSearch={handleSearch} />

      {/* 🔥 검색 결과 컴포넌트 추가 */}
      {keyword && (
        <div className="mt-8">
          <SearchResultList keyword={keyword} searchType={searchType} />
        </div>
      )}
    </div>
  );
}