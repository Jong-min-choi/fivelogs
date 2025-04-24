"use client";

import { useState, useEffect } from "react";
import CommentForm from "./CommentForm";
import { useGlobalLoginUser } from "@/stores/auth/loginUser";

export interface CommentType {
  id: number;
  comment: string;
  createdDate: string;
  updatedDate?: string | null;
  likeCount: number;
  dislikeCount: number;
  likedByMe?: boolean | null;
  nickname: string;
  deleted: boolean;
  replies: CommentType[]; // 초기에는 사용하지 않고 fetch로 불러옴
  parentId?: number | null;
}

interface Props {
  comment: CommentType;
  boardId: number;
  onRefresh: () => void;
  onDelete: (commentId: number) => void;
}

interface ReactionResponse {
  success: boolean;
  message: string;
  data: {
    likeCount: number;
    dislikeCount: number;
    likedByMe: boolean | null;
  };
}

interface ReactionRequest {
  isLike: boolean;
}

export default function Comment({
  comment: initialComment,
  boardId,
  onRefresh,
  onDelete,
}: Props) {
  const [comment, setComment] = useState<CommentType>(initialComment);
  const [showReplies, setShowReplies] = useState(false);
  const [replies, setReplies] = useState<CommentType[]>([]);
  const [loadingReplies, setLoadingReplies] = useState(false);
  const [replyCount, setReplyCount] = useState(0);
  const [parentId, setParentId] = useState(false);
  const [showReplyForm, setShowReplyForm] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [editedComment, setEditedComment] = useState(comment.comment);
  const { isLogin, loginUser } = useGlobalLoginUser();

  // initialComment가 변경될 때마다 comment 상태 업데이트
  useEffect(() => {
    setComment(initialComment);
  }, [initialComment]);

  // editedComment 상태 업데이트
  useEffect(() => {
    setEditedComment(comment.comment);
  }, [comment.comment]);

  // 현재 로그인한 사용자의 댓글인지 확인
  const isMyComment = loginUser?.nickname === comment.nickname;

  // 초기 대댓글 수 가져오기
  useEffect(() => {
    const fetchReplyCount = async () => {
      try {
        const res = await fetch(
          `http://localhost:8090/api/comments/${comment.id}/replies`,
          {
            credentials: "include",
          }
        );
        if (!res.ok) throw new Error("대댓글 수 불러오기 실패");

        const json = await res.json();
        setReplyCount(json.data.length || 0);
      } catch (err) {
        console.error("❌ 대댓글 수 불러오기 실패:", err);
      }
    };

    fetchReplyCount();
  }, [comment.id]);

  const handleReaction = async (isLike: boolean) => {
    if (!isLogin) {
      alert("로그인이 필요한 기능입니다.");
      return;
    }

    try {
      const res = await fetch(
        `http://localhost:8090/api/comments/boards/${boardId}/${comment.id}/reaction`,
        {
          method: "POST",
          headers: {
            Accept: "application/json",
            "Content-Type": "application/json",
          },
          credentials: "include",
          body: JSON.stringify({ isLike: isLike }),
        }
      );

      if (!res.ok) {
        const errorText = await res.text();
        console.error("서버 응답:", errorText);
        throw new Error("리액션 처리 중 오류가 발생했습니다.");
      }

      const json = await res.json();
      if (json.success) {
        setComment((prevComment) => ({
          ...prevComment,
          likeCount: json.data.likeCount,
          dislikeCount: json.data.dislikeCount,
          likedByMe: json.data.likedByMe,
        }));
      } else {
        throw new Error(json.message || "리액션 처리 중 오류가 발생했습니다.");
      }
    } catch (err) {
      console.error("❌ 리액션 실패:", err);
      alert(
        err instanceof Error
          ? err.message
          : "리액션 처리 중 오류가 발생했습니다."
      );
    }
  };

  const handleReplyClick = () => {
    if (!isLogin) {
      alert("로그인이 필요한 기능입니다.");
      return;
    }
    setShowReplyForm(!showReplyForm);
  };

  const fetchReplies = async () => {
    setLoadingReplies(true);
    try {
      const res = await fetch(
        `http://localhost:8090/api/comments/${comment.id}/replies`,
        {
          method: "GET",
          credentials: "include",
        }
      );

      if (!res.ok) throw new Error("대댓글 불러오기 실패");

      const json = await res.json();
      const fetchedReplies = json.data || [];
      const sortedReplies = fetchedReplies.sort(
        (a: CommentType, b: CommentType) =>
          new Date(a.createdDate).getTime() - new Date(b.createdDate).getTime()
      );
      setReplies(sortedReplies);
      setReplyCount(fetchedReplies.length);
    } catch (err) {
      console.error("❌ 대댓글 불러오기 실패:", err);
    } finally {
      setLoadingReplies(false);
    }
  };

  const handleToggleReplies = async () => {
    if (!showReplies) {
      await fetchReplies();
    }

    setShowReplies((prev) => !prev);
  };

  const displayNickname = comment.deleted
    ? "삭제된 사용자"
    : comment.nickname || "알 수 없는 사용자";

  const handleEdit = async () => {
    if (!isLogin || !isMyComment) return;
    if (comment.deleted) {
      alert("삭제된 댓글이여서 수정할 수 없습니다.");
      return;
    }

    try {
      const res = await fetch(
        `http://localhost:8090/api/comments/boards/${boardId}/${comment.id}`,
        {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
          },
          credentials: "include",
          body: JSON.stringify({
            comment: editedComment,
          }),
        }
      );

      if (!res.ok) throw new Error("댓글 수정 실패");

      const json = await res.json();

      if (json.success) {
        setComment((prevComment) => ({
          ...prevComment,
          comment: editedComment,
          updatedDate: new Date().toISOString(),
        }));
        setIsEditing(false);
      }
    } catch (err) {
      console.error("❌ 댓글 수정 실패:", err);
      alert("댓글 수정에 실패했습니다.");
    }
  };

  const handleDelete = async () => {
    if (!isLogin || !isMyComment) return;

    if (!window.confirm("정말로 이 댓글을 삭제하시겠습니까?")) return;

    try {
      const res = await fetch(
        `http://localhost:8090/api/comments/boards/${boardId}/${comment.id}`,
        {
          method: "DELETE",
          credentials: "include",
        }
      );

      if (!res.ok) throw new Error("댓글 삭제 실패");

      setComment((prevComment) => ({
        ...prevComment,
        deleted: true,
        comment: "삭제된 댓글입니다.",
        nickname: "삭제된 사용자",
      }));

      onDelete(comment.id);
    } catch (err) {
      console.error("❌ 댓글 삭제 실패:", err);
      alert("댓글 삭제에 실패했습니다.");
    }
  };

  const handleReplySuccess = async () => {
    setShowReplyForm(false);
    await fetchReplies();
    setShowReplies(true);
  };

  return (
    <div className="ml-2 border-l border-gray-200 pl-4 my-4">
      {/* 작성자 및 날짜 */}
      <div className="text-sm text-gray-500 mb-1">
        <span className="font-medium">{displayNickname}</span>
        <span className="mx-1">•</span>
        <span>{new Date(comment.createdDate).toLocaleString()}</span>
        {comment.updatedDate && !comment.deleted && (
          <span className="ml-1 text-xs text-gray-400">(수정됨)</span>
        )}
      </div>

      {comment.deleted ? (
        // 삭제된 댓글은 본문과 대댓글만 표시
        <div>
          <p className="text-gray-400 italic mb-1">삭제된 댓글입니다.</p>

          {/* 대댓글 목록 */}
          {replyCount > 0 && (
            <>
              <button
                className="text-xs text-gray-400 mt-2 hover:text-gray-600 transition-colors"
                onClick={handleToggleReplies}
              >
                {showReplies ? "대댓글 숨기기" : `대댓글 ${replyCount}개 보기`}
              </button>

              {showReplies && (
                <div className="pl-4 mt-2">
                  {loadingReplies ? (
                    <p className="text-xs text-gray-400">
                      대댓글 불러오는 중...
                    </p>
                  ) : (
                    <div className="space-y-4">
                      {replies.map((reply) => (
                        <Comment
                          key={reply.id}
                          comment={reply}
                          boardId={boardId}
                          onRefresh={onRefresh}
                          onDelete={onDelete}
                        />
                      ))}
                    </div>
                  )}
                </div>
              )}
            </>
          )}
        </div>
      ) : (
        // 삭제되지 않은 댓글은 모든 기능 표시
        <>
          <div className="flex justify-between items-start mb-1">
            {isEditing ? (
              <div className="w-full">
                <textarea
                  value={editedComment}
                  onChange={(e) => setEditedComment(e.target.value)}
                  className="w-full p-2 border rounded-lg resize-none focus:outline-none focus:ring-2 focus:ring-rose-500 focus:border-transparent"
                  rows={3}
                />
                <div className="flex justify-end gap-2 mt-2">
                  <button
                    onClick={() => setIsEditing(false)}
                    className="px-3 py-1 text-sm text-gray-600 hover:bg-gray-100 rounded-lg transition-colors"
                  >
                    취소
                  </button>
                  <button
                    onClick={handleEdit}
                    className="px-3 py-1 text-sm text-white bg-rose-500 hover:bg-rose-600 rounded-lg transition-colors"
                  >
                    수정
                  </button>
                </div>
              </div>
            ) : (
              <>
                <p>{comment.comment}</p>
                {/* 수정/삭제 메뉴 */}
                {isMyComment && !comment.deleted && (
                  <div className="relative group">
                    <button
                      className="p-1 hover:bg-gray-100 rounded-full"
                      title="더보기"
                    >
                      <span className="text-gray-400 hover:text-gray-600">
                        ⋮
                      </span>
                    </button>
                    <div className="absolute right-0 mt-1 w-24 bg-white border border-gray-200 rounded-lg shadow-lg opacity-0 invisible group-hover:opacity-100 group-hover:visible transition-all duration-200 z-10">
                      <button
                        onClick={() => setIsEditing(true)}
                        className="w-full px-4 py-2 text-sm text-left text-gray-700 hover:bg-gray-50 rounded-t-lg"
                      >
                        수정
                      </button>
                      <button
                        onClick={handleDelete}
                        className="w-full px-4 py-2 text-sm text-left text-red-600 hover:bg-gray-50 rounded-b-lg"
                      >
                        삭제
                      </button>
                    </div>
                  </div>
                )}
              </>
            )}
          </div>

          {/* 액션 버튼들 */}
          <div className="flex items-center gap-2 text-sm text-gray-500">
            {/* 좋아요/싫어요 그룹 */}
            {!comment.deleted && (
              <div className="flex items-center gap-1">
                <button
                  className={`flex items-center gap-1 px-2 py-1 rounded-full hover:bg-rose-50 hover:text-rose-500 transition-colors ${
                    comment.likedByMe === true
                      ? "text-rose-500 bg-rose-50 font-medium"
                      : ""
                  }`}
                  onClick={() => handleReaction(true)}
                  title={
                    !isLogin
                      ? "로그인이 필요합니다"
                      : comment.likedByMe === true
                      ? "좋아요 취소"
                      : "좋아요"
                  }
                >
                  <span>{comment.likedByMe === true ? "❤️" : "👍"}</span>
                  {comment.likeCount > 0 && (
                    <span
                      className={`text-xs ${
                        comment.likedByMe === true ? "font-medium" : ""
                      }`}
                    >
                      {comment.likeCount}
                    </span>
                  )}
                </button>

                <button
                  className={`flex items-center gap-1 px-2 py-1 rounded-full hover:bg-blue-50 hover:text-blue-500 transition-colors ${
                    comment.likedByMe === false
                      ? "text-blue-500 bg-blue-50 font-medium"
                      : ""
                  }`}
                  onClick={() => handleReaction(false)}
                  title={
                    !isLogin
                      ? "로그인이 필요합니다"
                      : comment.likedByMe === false
                      ? "싫어요 취소"
                      : "싫어요"
                  }
                >
                  <span>{comment.likedByMe === false ? "💔" : "👎"}</span>
                  {comment.dislikeCount > 0 && (
                    <span
                      className={`text-xs ${
                        comment.likedByMe === false ? "font-medium" : ""
                      }`}
                    >
                      {comment.dislikeCount}
                    </span>
                  )}
                </button>

                <button
                  className="flex items-center gap-1 px-2 py-1 rounded-full hover:bg-gray-100 text-gray-500 hover:text-gray-700 transition-colors"
                  onClick={() => {
                    handleReplyClick();
                    setShowReplies(true);
                  }}
                  title={!isLogin ? "로그인이 필요합니다" : "답글 달기"}
                >
                  <span>💬</span>
                </button>
              </div>
            )}
          </div>

          {/* 대댓글 관련 기능 */}
          {replyCount > 0 && (
            <button
              className="text-xs text-gray-400 mt-2 hover:text-gray-600 transition-colors"
              onClick={handleToggleReplies}
            >
              {showReplies ? "대댓글 숨기기" : `대댓글 ${replyCount}개 보기`}
            </button>
          )}

          {/* 대댓글 목록 */}
          {showReplies && (
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
                      onRefresh={onRefresh}
                      onDelete={onDelete}
                    />
                  ))}
                </div>
              )}
            </div>
          )}

          {/* 대댓글 작성 폼 */}
          {showReplyForm && showReplies && (
            <div className="pl-4 mt-2">
              <CommentForm
                boardId={boardId}
                parentId={comment.id}
                onSuccess={handleReplySuccess}
              />
            </div>
          )}
        </>
      )}
    </div>
  );
}
