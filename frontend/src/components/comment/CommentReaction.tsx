"use client";

import { useState } from "react";
import { useGlobalLoginUser } from "@/stores/auth/loginUser";

interface CommentReactionProps {
  boardId: number;
  commentId: number;
  likeCount: number;
  dislikeCount: number;
  likedByMe: boolean | null;
  onReactionUpdate: (likeCount: number, dislikeCount: number, likedByMe: boolean | null) => void;
}

export default function CommentReaction({
  boardId,
  commentId,
  likeCount: initialLikeCount,
  dislikeCount: initialDislikeCount,
  likedByMe: initialLikedByMe,
  onReactionUpdate,
}: CommentReactionProps) {
  const { isLogin } = useGlobalLoginUser();
  const [reactionState, setReactionState] = useState({
    likeCount: initialLikeCount,
    dislikeCount: initialDislikeCount,
    likedByMe: initialLikedByMe
  });

  const handleReaction = async (isLike: boolean) => {
    if (!isLogin) {
      alert("로그인이 필요한 기능입니다.");
      return;
    }
  
    try {
      const res = await fetch(
        `http://localhost:8090/api/comments/boards/${boardId}/${commentId}/reaction`,
        {
          method: "POST",
          headers: {
            "Accept": "application/json",
            "Content-Type": "application/json",
          },
          credentials: "include",
          body: JSON.stringify({ isLike })
        }
      );
  
      if (!res.ok) {
        throw new Error("리액션 처리 중 오류가 발생했습니다.");
      }

      const json = await res.json();
      if (json.success) {
        const newState = {
          likeCount: json.data.likeCount,
          dislikeCount: json.data.dislikeCount,
          likedByMe: json.data.likedByMe
        };
        setReactionState(newState);
        onReactionUpdate(
          json.data.likeCount,
          json.data.dislikeCount,
          json.data.likedByMe
        );
      } else {
        throw new Error(json.message || "리액션 처리 중 오류가 발생했습니다.");
      }
    } catch (err) {
      console.error("❌ 리액션 실패:", err);
      alert(err instanceof Error ? err.message : "리액션 처리 중 오류가 발생했습니다.");
    }
  };

  return (
    <div className="flex items-center gap-1">
      <button
        className={`flex items-center gap-1 px-2 py-1 rounded-full hover:bg-rose-50 hover:text-rose-500 transition-colors ${
          reactionState.likedByMe === true ? "text-rose-500 bg-rose-50" : ""
        }`}
        onClick={() => handleReaction(true)}
        title={!isLogin ? "로그인이 필요합니다" : "좋아요"}
      >
        <span>{reactionState.likedByMe === true ? "💖" : "👍"}</span>
        {reactionState.likeCount > 0 && (
          <span className="text-xs font-medium">{reactionState.likeCount}</span>
        )}
      </button>

      <button
        className={`flex items-center gap-1 px-2 py-1 rounded-full hover:bg-blue-50 hover:text-blue-500 transition-colors ${
          reactionState.likedByMe === false ? "text-blue-500 bg-blue-50" : ""
        }`}
        onClick={() => handleReaction(false)}
        title={!isLogin ? "로그인이 필요합니다" : "싫어요"}
      >
        <span>{reactionState.likedByMe === false ? "👎" : "👎"}</span>
        {reactionState.dislikeCount > 0 && (
          <span className="text-xs font-medium">{reactionState.dislikeCount}</span>
        )}
      </button>
    </div>
  );
} 