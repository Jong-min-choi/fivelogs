"use client";

import CommentInput from "./CommentInput";

interface ReplyCommentFormProps {
  boardId: number;
  parentId: number;
  onSuccess: () => void;
}

export default function ReplyCommentForm({
  boardId,
  parentId,
  onSuccess,
}: ReplyCommentFormProps) {
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
          parentId,
        }),
      }
    );

    if (!res.ok) {
      throw new Error("답글 작성 실패");
    }

    const json = await res.json();
    if (json.success) {
      onSuccess();
    } else {
      throw new Error(json.message || "답글 작성 실패");
    }
  };

  return (
    <CommentInput
      placeholder="답글을 작성해주세요."
      onSubmit={handleSubmit}
      submitButtonText="답글 작성"
    />
  );
}
