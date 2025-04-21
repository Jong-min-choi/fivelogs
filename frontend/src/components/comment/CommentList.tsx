"use client";

import { useEffect, useState } from "react";
import CommentForm from "./CommentForm";
import Comment, { CommentType } from "./Comment";

interface CommentListProps {
  boardId: number;
}

export default function CommentList({ boardId }: CommentListProps) {
  const [comments, setComments] = useState<CommentType[]>([]);
  const [loading, setLoading] = useState(false);
  const [showCommentForm, setShowCommentForm] = useState(false);
  const [showComments, setShowComments] = useState(false);

  const fetchComments = async () => {
    setLoading(true);
    try {
      const res = await fetch(`http://localhost:8090/api/comments/boards/${boardId}`);

      if (!res.ok) {
        throw new Error(`HTTP error! status: ${res.status}`);
      }

      const json = await res.json();
      setComments(json.data || []);
    } catch (err) {
      console.error("❌ 댓글 불러오기 실패:", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (boardId) {
      fetchComments();
    }
  }, [boardId]);

  const handleCommentSuccess = () => {
    setShowCommentForm(false);
    fetchComments();
  };

  return (
    <div className="mt-6">
      <div className="flex items-center justify-between mb-4">
        <div className="flex items-center gap-4">
          <button
            onClick={() => setShowComments(!showComments)}
            className="text-sm px-3 py-1 rounded-full border border-gray-300 text-gray-600 hover:bg-gray-50 transition-colors flex items-center gap-2"
          >
            <span>{showComments ? "댓글 숨기기" : "댓글 보기"}</span>
            {comments.length > 0 && (
              <span className="bg-gray-100 px-2 py-0.5 rounded-full text-xs">
                {comments.length}
              </span>
            )}
          </button>
        </div>
      </div>

      {/* 댓글 목록과 작성 폼 */}
      {showComments && (
        <div className="transition-all duration-300">
          {/* 댓글 리스트 */}
          <div className="mb-6">
            {loading ? (
              <p>댓글을 불러오는 중...</p>
            ) : comments.length === 0 ? (
              <p className="text-gray-500">아직 댓글이 없습니다.</p>
            ) : (
              comments.map((comment) => (
                <Comment
                  key={comment.id}
                  comment={comment}
                  boardId={boardId}
                  onRefresh={fetchComments}
                />
              ))
            )}
          </div>

          {/* 댓글 작성 폼 - 맨 아래에 배치 */}
          <div className="mt-8 border-t pt-6">
            <CommentForm boardId={boardId} onSuccess={handleCommentSuccess} />
          </div>
        </div>
      )}
    </div>
  );
}