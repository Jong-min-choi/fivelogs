"use client";

import { useState, useEffect } from "react";

interface User {
  id: number;
  nickname: string;
}

interface CommentFormProps {
  boardId?: number;
  parentId?: number;
  onSuccess?: () => void;
}

const CommentForm = ({ boardId, parentId, onSuccess }: CommentFormProps) => {
  const [comment, setComment] = useState("");
  const [loading, setLoading] = useState(false);
  const [user, setUser] = useState<User | null>(null);

  useEffect(() => {
    // 현재 로그인한 사용자 정보 가져오기
    const fetchUserInfo = async () => {
      try {
        const res = await fetch('http://localhost:8090/api/users/me', {
          method: 'GET',
          credentials: 'include',
          headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
          },
        });

        if (res.ok) {
          const data = await res.json();
          setUser(data);
        } else if (res.status === 401) {
          // 인증되지 않은 사용자
          setUser(null);
          console.log("사용자가 로그인하지 않았습니다.");
        } else {
          console.error("사용자 정보 가져오기 실패. 상태 코드:", res.status);
          setUser(null);
        }
      } catch (error) {
        console.error("사용자 정보 가져오기 실패:", error);
        setUser(null);
      }
    };

    fetchUserInfo();
  }, []);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!boardId) {
      console.warn("❌ boardId가 없습니다! 댓글 작성 중지");
      return;
    }

    if (!comment.trim()) return;

    if (!user) {
      alert("로그인이 필요한 기능입니다.");
      return;
    }

    setLoading(true);

    try {
      const res = await fetch(`http://localhost:8090/api/comments/boards/${boardId}`, {
        method: "POST",
        headers: {
          "Accept": "application/json",
          "Content-Type": "application/json",
        },
        credentials: "include",
        body: JSON.stringify({
          boardId: boardId,
          comment: comment.trim(),
          parentId: parentId || null
        }),
      });

      if (!res.ok) {
        if (res.status === 401) {
          setUser(null);
          alert("로그인이 필요합니다.");
          return;
        }
        const errorData = await res.json();
        throw new Error(errorData.message || "댓글 작성에 실패했습니다.");
      }

      const json = await res.json();
      console.log('댓글 작성 성공:', json);
      setComment("");
      onSuccess?.();
    } catch (error) {
      console.error("❌ 댓글 작성 오류:", error);
      alert(error instanceof Error ? error.message : "댓글 작성에 실패했습니다.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="mb-4">
      {/* 현재 사용자 정보 표시 */}
      {user && (
        <div className="mb-2 text-sm text-gray-600">
          <span className="font-medium">{user.nickname}</span>
          <span className="text-gray-400">님으로 작성중</span>
        </div>
      )}
      <textarea
        value={comment}
        onChange={(e) => setComment(e.target.value)}
        rows={3}
        placeholder={!user ? "로그인이 필요합니다" : parentId ? "대댓글을 입력하세요" : "댓글을 입력하세요"}
        className="w-full border rounded p-2 mb-2"
        disabled={loading || !user}
      />
      <div className="flex justify-end">
        <button
          type="submit"
          disabled={loading || !user}
          className="bg-rose-500 text-white px-4 py-1 rounded hover:bg-rose-600 disabled:opacity-50"
        >
          {loading ? "작성 중..." : parentId ? "답글 작성" : "댓글 작성"}
        </button>
      </div>
      {!user && (
        <p className="text-sm text-gray-500 mt-2">
          댓글을 작성하려면 로그인이 필요합니다.
        </p>
      )}
    </form>
  );
};

export default CommentForm;