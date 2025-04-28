"use client";

import CommentInput from "./CommentInput";

interface MainCommentFormProps {
  boardId: number;
  onSuccess: () => void;
}

export default function MainCommentForm({ boardId, onSuccess }: MainCommentFormProps) {
  const handleSubmit = async (comment: string) => {
    const res = await fetch(
      `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/comments/boards/${boardId}`,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
        body: JSON.stringify({
          comment,
          boardId,
          parentId: null,
        }),
      }
    );

    if (!res.ok) {
      throw new Error("댓글 작성 실패");
    }

    const json = await res.json();
    if (json.success) {
      onSuccess();
    } else {
      throw new Error(json.message || "댓글 작성 실패");
    }
  };

  return (
    <CommentInput
      placeholder="댓글을 작성해주세요."
      onSubmit={handleSubmit}
      submitButtonText="댓글 작성"
    />
  );
} 