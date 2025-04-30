"use client";

import { useEffect, useState } from "react";
import Pagination from "@/components/common/Pagination";

type AdminUser = {
  id: number;
  email: string;
  nickname: string;
  snsLinks: string | null;
  provider: string;
  userStatus: "NORMAL" | "BANNED";
  roleType: "ADMIN" | "USER";
};

type PageDto = {
  startPage: number;
  endPage: number;
  currentPage: number;
  totalPage: number;
};

export default function AdminPage() {
  const [users, setUsers] = useState<AdminUser[]>([]);
  const [pageInfo, setPageInfo] = useState<PageDto | null>(null);
  const [loading, setLoading] = useState(true);
  const [searchType, setSearchType] = useState<"id" | "email" | "nickname">(
    "id"
  );
  const [keyword, setKeyword] = useState("");
  const size = 20;

  // 회원 목록 조회
  const fetchUsers = async (page = 1, type = searchType, key = keyword) => {
    setLoading(true);
    try {
      const params = new URLSearchParams({
        page: String(page),
        size: String(size),
      });
      if (key) {
        params.append("type", type);
        params.append("keyword", key);
      }

      console.log(key);
      const res = await fetch(
        `${
          process.env.NEXT_PUBLIC_API_BASE_URL
        }/api/admin/users/search?${params.toString()}`,
        { credentials: "include" }
      );
      const data = await res.json();
      setUsers(data.data.adminUserResponseDto);
      setPageInfo(data.data.pageDto);
    } catch (e) {
      console.log(e);
      alert("회원 정보를 불러오지 못했습니다.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchUsers();
  }, []);

  // 회원 상태 변경
  const handleChangeStatus = async (user: AdminUser) => {
    const nextStatus = user.userStatus === "NORMAL" ? "BANNED" : "NORMAL";
    try {
      const res = await fetch(
        `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/admin/users/change-status`,
        {
          method: "POST",
          credentials: "include",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            userId: user.id,
            userStatus: nextStatus,
          }),
        }
      );
      const data = await res.json();
      if (data.success) {
        setUsers((prev) =>
          prev.map((u) =>
            u.id === user.id ? { ...u, userStatus: data.data } : u
          )
        );
      } else {
        alert("상태 변경 실패: " + data.message);
      }
    } catch {
      alert("상태 변경 중 오류가 발생했습니다.");
    }
  };

  // 검색 폼 제출
  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    fetchUsers(1, searchType, keyword);
  };

  // 페이지 변경 핸들러
  const handlePageChange = (page: number) => {
    fetchUsers(page, searchType, keyword);
  };

  return (
    <div className="max-w-4xl mx-auto py-10">
      <h1 className="text-2xl font-bold mb-6">관리자 - 회원 관리</h1>
      {/* 검색창 추가 */}
      <form onSubmit={handleSearch} className="flex gap-2 mb-6">
        <select
          value={searchType}
          onChange={(e) =>
            setSearchType(e.target.value as "id" | "email" | "nickname")
          }
          className="border rounded px-2 py-1"
        >
          <option value="id">ID</option>
          <option value="email">이메일</option>
          <option value="nickname">닉네임</option>
        </select>
        <input
          type="text"
          value={keyword}
          onChange={(e) => setKeyword(e.target.value)}
          className="border rounded px-2 py-1 flex-1"
          placeholder="검색어를 입력하세요"
        />
        <button
          type="submit"
          className="bg-rose-500 text-white px-4 py-1 rounded hover:bg-rose-600"
        >
          검색
        </button>
      </form>
      {loading ? (
        <div className="text-center text-gray-400 py-10">로딩 중...</div>
      ) : (
        <>
          <table className="w-full border text-center">
            <thead>
              <tr className="bg-gray-100">
                <th className="py-2">ID</th>
                <th>이메일</th>
                <th>닉네임</th>
                <th>상태</th>
                <th>권한</th>
                <th>상태 변경</th>
              </tr>
            </thead>
            <tbody>
              {users.map((user) => (
                <tr key={user.id} className="border-t">
                  <td className="py-2">{user.id}</td>
                  <td>{user.email}</td>
                  <td>{user.nickname}</td>
                  <td>
                    <span
                      className={
                        user.userStatus === "NORMAL"
                          ? "text-emerald-600 font-semibold"
                          : "text-rose-500 font-semibold"
                      }
                    >
                      {user.userStatus === "NORMAL" ? "정상" : "정지"}
                    </span>
                  </td>
                  <td>{user.roleType === "ADMIN" ? "관리자" : "일반"}</td>
                  <td>
                    <button
                      className={`cursor-pointer px-3 py-1 rounded font-bold ${
                        user.userStatus === "NORMAL"
                          ? "bg-rose-100 text-rose-600 hover:bg-rose-200"
                          : "bg-emerald-100 text-emerald-600 hover:bg-emerald-200"
                      }`}
                      onClick={() => handleChangeStatus(user)}
                    >
                      {user.userStatus === "NORMAL" ? "정지" : "정상"}
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
          {/* 페이지네이션 */}
          <Pagination pageInfo={pageInfo} onPageChange={handlePageChange} />
        </>
      )}
    </div>
  );
}
