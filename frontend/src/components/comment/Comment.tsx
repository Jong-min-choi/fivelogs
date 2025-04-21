"use client";

import { useState, useEffect } from "react";
import CommentForm from "./CommentForm";

export interface CommentType {
  id: number;
  comment: string;
  createdDate: string;
  updatedDate?: string | null;
  likeCount: number;
  dislikeCount: number;
  likedByMe?: boolean | null;
  user?: {
    id: number;
    nickname: string;
  };
  deleted: boolean;
  replies: CommentType[];
  parentId?: number | null;
}

interface Props {
  comment: CommentType;
  boardId: number;
  onRefresh: () => void;
}

export default function Comment({ comment, boardId, onRefresh }: Props) {
  const [showReplies, setShowReplies] = useState(false);
  const [showReplyForm, setShowReplyForm] = useState(false);
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  useEffect(() => {
    // 로그인 상태 확인
    const checkLoginStatus = async () => {
      try {
        const res = await fetch('http://localhost:8090/api/users/me', {
          credentials: 'include',
        });
        setIsLoggedIn(res.ok);
      } catch (error) {
        setIsLoggedIn(false);
      }
    };

    checkLoginStatus();
  }, []);

  const handleReaction = async (isLike: boolean) => {
    if (!isLoggedIn) {
      alert("로그인이 필요한 기능입니다.");
      return;
    }

    try {
      const res = await fetch(`http://localhost:8090/api/comments/boards/${boardId}/${comment.id}/reaction?isLike=${isLike}`, {
        method: "POST",
        credentials: "include",
      });

      if (!res.ok) {
        throw new Error("리액션 처리 중 오류가 발생했습니다.");
      }

      onRefresh(); // 댓글 목록 새로고침
    } catch (err) {
      console.error("❌ 리액션 실패:", err);
      alert("리액션 처리 중 오류가 발생했습니다.");
    }
  };

  const handleReplyClick = () => {
    if (!isLoggedIn) {
      alert("로그인이 필요한 기능입니다.");
      return;
    }
    setShowReplyForm(!showReplyForm);
  };

  const sortedReplies = [...comment.replies].sort(
    (a, b) => new Date(a.createdDate).getTime() - new Date(b.createdDate).getTime()
  );

  // 삭제된 댓글이나 사용자 정보가 없는 경우의 표시 닉네임
  const displayNickname = comment.deleted ? "삭제된 사용자" : comment.user?.nickname || "알 수 없는 사용자";

  return (
    <div className="ml-2 border-l border-gray-200 pl-4 my-4">
      {/* 작성자 및 날짜 */}
      <div className="text-sm text-gray-500 mb-1">
        <span className="font-medium">{displayNickname}</span>
        <span className="mx-1">•</span>
        <span>{new Date(comment.createdDate).toLocaleString()}</span>
        {comment.updatedDate && (
          <span className="ml-1 text-xs text-gray-400">(수정됨)</span>
        )}
      </div>

      {/* 댓글 본문 */}
      <p className={`mb-1 ${comment.deleted ? "text-gray-400 italic" : ""}`}>
        {comment.deleted ? "삭제된 댓글입니다." : comment.comment}
      </p>

      {/* 좋아요 / 싫어요 / 답글 버튼 */}
      {!comment.deleted && (
        <div className="flex items-center gap-4 text-sm text-gray-500">
          <button
            className={`flex items-center gap-2 px-3 py-1 rounded-full hover:bg-rose-50 hover:text-rose-500 transition-colors ${
              comment.likedByMe === true ? "text-rose-500 bg-rose-50" : ""
            }`}
            onClick={() => handleReaction(true)}
            title={!isLoggedIn ? "로그인이 필요합니다" : "좋아요"}
          >
            <span>👍</span>
            {comment.likeCount > 0 && (
              <span className="font-medium">{comment.likeCount}</span>
            )}
          </button>

          <button
            className={`flex items-center gap-2 px-3 py-1 rounded-full hover:bg-blue-50 hover:text-blue-500 transition-colors ${
              comment.likedByMe === false ? "text-blue-500 bg-blue-50" : ""
            }`}
            onClick={() => handleReaction(false)}
            title={!isLoggedIn ? "로그인이 필요합니다" : "싫어요"}
          >
            <span>👎</span>
            {comment.dislikeCount > 0 && (
              <span className="font-medium">{comment.dislikeCount}</span>
            )}
          </button>
          <button 
            className="hover:text-gray-700 transition-colors"
            onClick={handleReplyClick}
            title={!isLoggedIn ? "로그인이 필요합니다" : "답글 작성"}
          >
            <span>💬</span>
          </button>
        </div>
      )}

      {/* 대댓글 작성 폼 */}
      {showReplyForm && (
        <div className="mt-2">
          <CommentForm
            boardId={boardId}
            parentId={comment.id}
            onSuccess={() => {
              setShowReplyForm(false);
              setShowReplies(true);
              onRefresh();
            }}
          />
        </div>
      )}

      {/* 대댓글 보기/숨기기 버튼 */}
      {sortedReplies.length > 0 && (
        <button
          className="text-xs text-gray-400 mt-2"
          onClick={() => setShowReplies((prev) => !prev)}
        >
          {showReplies
            ? "대댓글 숨기기"
            : `대댓글 보기 (${sortedReplies.length})`}
        </button>
      )}

      {/* 대댓글 목록 */}
      {showReplies &&
        sortedReplies.map((reply) => (
          <Comment
            key={reply.id}
            comment={reply}
            boardId={boardId}
            onRefresh={onRefresh}
          />
        ))}
    </div>
  );
}