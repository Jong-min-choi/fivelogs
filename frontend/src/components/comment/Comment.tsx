"use client";

import { useState, useEffect } from "react";
import { useGlobalLoginUser } from "@/stores/auth/loginUser";
import CommentEdit from "./CommentEdit";
import CommentDelete from "./CommentDelete";
import ReplyCommentForm from "./ReplyCommentForm";
import CommentReaction from "./CommentReaction";
import CommentHeader from "./CommentHeader";
import CommentReplies from "./CommentReplies";

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
  replies: CommentType[];
  parentId?: number | null;
}

interface Props {
  comment: CommentType;
  boardId: number;
  onRefresh: () => void;
  onDelete: (commentId: number) => void;
}

export default function Comment({ comment: initialComment, boardId, onRefresh, onDelete }: Props) {
  const [comment, setComment] = useState<CommentType>(initialComment);
  const [showReplyForm, setShowReplyForm] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [replyCount, setReplyCount] = useState(0);
  const [repliesKey, setRepliesKey] = useState(0);
  const [keepRepliesVisible, setKeepRepliesVisible] = useState(false);
  const { isLogin, loginUser } = useGlobalLoginUser();

  useEffect(() => {
    setComment(initialComment);
  }, [initialComment]);

  const refreshComment = async () => {
    try {
      const res = await fetch(`http://localhost:8090/api/comments/${comment.id}/replies`, {
        credentials: "include",
      });
      if (!res.ok) throw new Error("댓글 정보 가져오기 실패");

      const json = await res.json();
      if (json.success) {
        setComment(prevComment => ({
          ...prevComment,
          replies: json.data
        }));
      }
    } catch (err) {
      console.error("❌ 댓글 정보 가져오기 실패:", err);
    }
  };

  const isMyComment = loginUser?.nickname === comment.nickname;

  const handleReactionUpdate = (newLikeCount: number, newDislikeCount: number, newLikedByMe: boolean | null) => {
    setComment(prevComment => ({
      ...prevComment,
      likeCount: newLikeCount,
      dislikeCount: newDislikeCount,
      likedByMe: newLikedByMe
    }));
  };

  const handleReplyClick = () => {
    if (!isLogin) {
      alert("로그인이 필요한 기능입니다.");
      return;
    }
    setShowReplyForm(!showReplyForm);
    setKeepRepliesVisible(true);
    setRepliesKey(prev => prev + 1);
  };

  const handleReplySuccess = async () => {
    setShowReplyForm(false);
    try {
      const res = await fetch(`http://localhost:8090/api/comments/${comment.id}/replies`, {
        credentials: "include",
      });
      if (!res.ok) throw new Error("대댓글 수 불러오기 실패");

      const json = await res.json();
      setReplyCount(json.data.length || 0);
      setRepliesKey(prev => prev + 1);
      await refreshComment(); // 댓글 정보 새로고침
    } catch (err) {
      console.error("❌ 대댓글 수 불러오기 실패:", err);
    }
  };

  const handleEdit = async (editedComment: string) => {
    if (!isLogin || !isMyComment) return;
    if (comment.deleted) {
      alert("삭제된 댓글이여서 수정할 수 없습니다.");
      return;
    }
  
    try {
      const res = await fetch(`http://localhost:8090/api/comments/boards/${boardId}/${comment.id}`,
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
        setComment(prevComment => ({
          ...prevComment,
          comment: editedComment,
          updatedDate: new Date().toISOString()
        }));
        setIsEditing(false);
      }
    } catch (err) {
      console.error("❌ 댓글 수정 실패:", err);
      alert("댓글 수정에 실패했습니다.");
    }
  };

  const handleDelete = async () => {
    try {
      const res = await fetch(
        `http://localhost:8090/api/comments/boards/${boardId}/${comment.id}`,
        {
          method: "DELETE",
          credentials: "include",
        }
      );

      if (!res.ok) throw new Error("댓글 삭제 실패");

      setComment(prevComment => ({
        ...prevComment,
        deleted: true,
        comment: "삭제된 댓글입니다.",
        nickname: "삭제된 사용자"
      }));
      
      onDelete(comment.id);
    } catch (err) {
      console.error("❌ 댓글 삭제 실패:", err);
      throw new Error("댓글 삭제에 실패했습니다.");
    }
  };

  return (
    <div className="ml-2 border-l border-gray-200 pl-4 my-4">
      <CommentHeader
        nickname={comment.nickname}
        createdDate={comment.createdDate}
        updatedDate={comment.updatedDate}
        deleted={comment.deleted}
      />

      {comment.deleted ? (
        <div>
          <p className="text-gray-400 italic mb-1">삭제된 댓글입니다.</p>
          <CommentReplies
            key={repliesKey}
            commentId={comment.id}
            boardId={boardId}
            onRefresh={onRefresh}
            onDelete={onDelete}
            onReplyCountChange={setReplyCount}
            forceShowReplies={keepRepliesVisible}
          />
        </div>
      ) : (
        <>
          <div className="flex justify-between items-start mb-1">
            {isEditing ? (
              <CommentEdit
                initialComment={comment.comment}
                onSave={handleEdit}
                onCancel={() => setIsEditing(false)}
              />
            ) : (
              <>
                <p>{comment.comment}</p>
                {isMyComment && (
                  <div className="relative group">
                    <button
                      className="p-1 hover:bg-gray-100 rounded-full"
                      title="더보기"
                    >
                      <span className="text-gray-400 hover:text-gray-600">⋮</span>
                    </button>
                    <div className="absolute right-0 mt-1 w-24 bg-white border border-gray-200 rounded-lg shadow-lg opacity-0 invisible group-hover:opacity-100 group-hover:visible transition-all duration-200 z-10">
                      <button
                        onClick={() => setIsEditing(true)}
                        className="w-full px-4 py-2 text-sm text-left text-gray-700 hover:bg-gray-50 rounded-t-lg"
                      >
                        수정
                      </button>
                      <CommentDelete onDelete={handleDelete} />
                    </div>
                  </div>
                )}
              </>
            )}
          </div>

          <div className="flex items-center gap-2 text-sm text-gray-500">
            <div className="flex items-center gap-1">
              <CommentReaction
                boardId={boardId}
                commentId={comment.id}
                likeCount={comment.likeCount}
                dislikeCount={comment.dislikeCount}
                likedByMe={comment.likedByMe ?? null}
                onReactionUpdate={handleReactionUpdate}
              />

              <button
                className="flex items-center gap-1 px-2 py-1 rounded-full hover:bg-gray-100 text-gray-500 hover:text-gray-700 transition-colors"
                onClick={handleReplyClick}
                title={!isLogin ? "로그인이 필요합니다" : "답글 달기"}
              >
                <span>💬</span>
              </button>
            </div>
          </div>

          <CommentReplies
            key={repliesKey}
            commentId={comment.id}
            boardId={boardId}
            onRefresh={onRefresh}
            onDelete={onDelete}
            onReplyCountChange={setReplyCount}
            forceShowReplies={keepRepliesVisible}
          />

          {showReplyForm && (
            <div className="pl-4 mt-2">
              <ReplyCommentForm
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