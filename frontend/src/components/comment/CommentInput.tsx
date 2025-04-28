"use client";

import { useState } from "react";
import { useGlobalLoginUser } from "@/stores/auth/loginUser";

interface CommentInputProps {
  placeholder?: string;
  onSubmit: (comment: string) => Promise<void>;
  submitButtonText?: string;
}

export default function CommentInput({
  placeholder = "댓글을 작성해주세요.",
  onSubmit,
  submitButtonText = "댓글 작성"
}: CommentInputProps) {
  const [comment, setComment] = useState("");
  const { isLogin } = useGlobalLoginUser();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!isLogin) {
      alert("로그인이 필요한 기능입니다.");
      return;
    }

    if (!comment.trim()) {
      alert("댓글 내용을 입력해주세요.");
      return;
    }

    try {
      await onSubmit(comment.trim());
      setComment("");
    } catch (err) {
      console.error("❌ 댓글 작성 실패:", err);
      alert("댓글 작성에 실패했습니다.");
    }
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      <textarea
        value={comment}
        onChange={(e) => setComment(e.target.value)}
        placeholder={
          isLogin ? placeholder : "로그인이 필요한 기능입니다."
        }
        disabled={!isLogin}
        className="w-full p-2 border rounded-lg resize-none focus:outline-none focus:ring-2 focus:ring-rose-500 focus:border-transparent"
        rows={3}
      />
      <div className="flex justify-end">
        <button
          type="submit"
          disabled={!isLogin || !comment.trim()}
          className={`px-4 py-2 rounded-lg text-white text-sm transition-colors ${
            isLogin && comment.trim()
              ? "bg-rose-500 hover:bg-rose-600"
              : "bg-gray-300 cursor-not-allowed"
          }`}
        >
          {submitButtonText}
        </button>
      </div>
    </form>
  );
} 