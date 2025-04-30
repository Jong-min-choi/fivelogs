"use client";

import { useState, useEffect, useCallback } from "react";
import Comment, { CommentType } from "./Comment";

interface CommentRepliesProps {
  commentId: number;
  boardId: number;
  onRefresh: () => void;
  onDelete: (commentId: number) => void;
  onReplyCountChange: (count: number) => void;
  showReplies?: boolean;
  onShowRepliesChange?: (show: boolean) => void;
  forceShowReplies?: boolean;
}

export default function CommentReplies({
  commentId,
  boardId,
  onRefresh,
  onDelete,
  onReplyCountChange,
  showReplies = false,
  onShowRepliesChange,
  forceShowReplies = false,
}: CommentRepliesProps) {
  const [showRepliesState, setShowRepliesState] = useState(showReplies);
  const [replies, setReplies] = useState<CommentType[]>([]);
  const [loadingReplies, setLoadingReplies] = useState(false);
  const [totalReplies, setTotalReplies] = useState(0);

  // forceShowReplies가 true일 때 대댓글 목록 표시
  useEffect(() => {
    if (forceShowReplies) {
      setShowRepliesState(true);
    }
  }, [forceShowReplies]);

  const fetchReplies = useCallback(async () => {
    if (!showRepliesState && !forceShowReplies) return; // 답글이 보이지 않는 상태면 fetch하지 않음

    setLoadingReplies(true);
    try {
      const res = await fetch(
        `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/comments/${commentId}/replies`,
        {
          method: "GET",
          credentials: "include",
        }
      );

      if (!res.ok) throw new Error("대댓글 불러오기 실패");

      const json = await res.json();
      const fetchedReplies = json.data || [];
      // 오래된 순으로 정렬 (오래된 댓글이 위로, 새로운 댓글이 아래로)
      const sortedReplies = fetchedReplies.sort(
        (a: CommentType, b: CommentType) =>
          new Date(a.createdDate).getTime() - new Date(b.createdDate).getTime()
      );
      setReplies(sortedReplies);
      setTotalReplies(fetchedReplies.length);
      onReplyCountChange(fetchedReplies.length);
    } catch (err) {
      console.error("❌ 대댓글 불러오기 실패:", err);
    } finally {
      setLoadingReplies(false);
    }
  }, [commentId, onReplyCountChange, showRepliesState, forceShowReplies]);

  // 답글이 보이는 상태일 때만 갱신
  useEffect(() => {
    fetchReplies();
  }, [fetchReplies]);

  const handleToggleReplies = () => {
    const newState = !showRepliesState;
    setShowRepliesState(newState);
    onShowRepliesChange?.(newState);
  };

  // 초기 답글 수 확인
  useEffect(() => {
    const fetchInitialReplyCount = async () => {
      try {
        const res = await fetch(
          `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/comments/${commentId}/replies`,
          {
            method: "GET",
            credentials: "include",
          }
        );

        if (!res.ok) throw new Error("대댓글 수 불러오기 실패");

        const json = await res.json();
        const count = json.data?.length || 0;
        setTotalReplies(count);
        onReplyCountChange(count);
      } catch (err) {
        console.error("❌ 대댓글 수 불러오기 실패:", err);
      }
    };

    fetchInitialReplyCount();
  }, [commentId, onReplyCountChange]);

  if (totalReplies === 0) return null;

  return (
    <>
      <button
        className="text-xs text-gray-400 mt-2 hover:text-gray-600 transition-colors"
        onClick={handleToggleReplies}
      >
        {showRepliesState || forceShowReplies
          ? "대댓글 숨기기"
          : `대댓글 ${totalReplies}개 보기`}
      </button>

      {(showRepliesState || forceShowReplies) && (
        <div className="pl-4 mt-2">
          {loadingReplies ? (
            <p className="text-xs text-gray-400">대댓글 불러오는 중...</p>
          ) : (
            <div className="space-y-4">
              {replies.map((reply) => (
                <Comment
                  key={reply.id}
                  comment={reply}
                  boardId={boardId}
                  onRefresh={fetchReplies}
                  onDelete={onDelete}
                />
              ))}
            </div>
          )}
        </div>
      )}
    </>
  );
}
