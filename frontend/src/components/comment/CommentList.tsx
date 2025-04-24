"use client";

import { useEffect, useState } from "react";
import CommentForm from "./CommentForm";
import Comment, { CommentType } from "./Comment";
import Pagination from "../common/Pagination";
import { PageDto } from "@/types/board";

interface CommentListProps {
  boardId: number;
}

interface CommentResponse {
  success: boolean;
  data: {
    comments: CommentType[];
    pageInfo: PageDto;
  };
  message: string;
}

type SortType = "createdDate,desc" | "createdDate,asc" | "likeCount,desc";

export default function CommentList({ boardId }: CommentListProps) {
  const [comments, setComments] = useState<CommentType[]>([]);
  const [loading, setLoading] = useState(false);
  const [showCommentForm, setShowCommentForm] = useState(false);
  const [showComments, setShowComments] = useState(false);
  const [pageInfo, setPageInfo] = useState<PageDto | null>(null);
  const [currentPage, setCurrentPage] = useState(1);
  const [sortBy, setSortBy] = useState<SortType>("createdDate,desc");

  const fetchComments = async (page: number = 1) => {
    setLoading(true);
    try {
      const url = new URL(
        `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/comments/boards/${boardId}/page`
      );
      url.searchParams.set("page", page.toString());
      url.searchParams.set("size", "10");
      url.searchParams.set("sort", sortBy);

      const res = await fetch(url.toString(), {
        method: "GET",
        credentials: "include",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
        },
      });

      if (!res.ok) {
        const errorText = await res.text();
        console.error("서버 응답:", errorText);
        throw new Error(`HTTP error! status: ${res.status}`);
      }

      const json: CommentResponse = await res.json();
      console.log(json.data.comments[0]);
      if (json.success) {
        const processedComments = json.data.comments.map((comment) => ({
          ...comment,
          deleted: comment.deleted,
          comment: comment.deleted ? "삭제된 댓글입니다." : comment.comment,
          nickname: comment.deleted ? "삭제된 사용자" : comment.nickname,
          likedByMe: comment.likedByMe,
        }));
        setComments(processedComments);
        setPageInfo(json.data.pageInfo);
      }
    } catch (err) {
      console.error("❌ 댓글 불러오기 실패:", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (boardId) {
      fetchComments(currentPage);
    }
  }, [boardId, currentPage, sortBy]);

  const handleCommentSuccess = () => {
    setShowCommentForm(false);
    fetchComments(1);
  };

  const handlePageChange = (page: number) => {
    setCurrentPage(page);
  };

  const handleSortChange = (newSort: SortType) => {
    setSortBy(newSort);
    setCurrentPage(1);
  };

  const handleCommentDelete = (deletedCommentId: number) => {
    setComments((prevComments) =>
      prevComments.map((comment) =>
        comment.id === deletedCommentId
          ? {
              ...comment,
              deleted: true,
              comment: "삭제된 댓글입니다.",
              nickname: "삭제된 사용자",
            }
          : comment
      )
    );
    // 삭제 후 현재 페이지 다시 불러오기
    fetchComments(currentPage);
  };

  const totalCommentCount = comments.length;

  return (
    <div className="mt-6">
      <div className="flex items-center justify-between mb-4">
        <div className="flex items-center gap-4">
          <button
            onClick={() => setShowComments(!showComments)}
            className="text-sm px-3 py-1 rounded-full border border-gray-300 text-gray-600 hover:bg-gray-50 transition-colors flex items-center gap-2"
          >
            <span>{showComments ? "댓글 숨기기" : "댓글 보기"}</span>
            {totalCommentCount > 0 && (
              <span className="bg-gray-100 px-2 py-0.5 rounded-full text-xs">
                {totalCommentCount}
              </span>
            )}
          </button>

          {/* 정렬 옵션 */}
          {showComments && (
            <div className="flex items-center gap-2 text-sm">
              <button
                onClick={() => handleSortChange("createdDate,desc")}
                className={`px-2 py-1 rounded-full transition-colors ${
                  sortBy === "createdDate,desc"
                    ? "text-rose-500 bg-rose-50"
                    : "text-gray-500 hover:bg-gray-50"
                }`}
              >
                최신순
              </button>
              <button
                onClick={() => handleSortChange("createdDate,asc")}
                className={`px-2 py-1 rounded-full transition-colors ${
                  sortBy === "createdDate,asc"
                    ? "text-rose-500 bg-rose-50"
                    : "text-gray-500 hover:bg-gray-50"
                }`}
              >
                오래된순
              </button>
              <button
                onClick={() => handleSortChange("likeCount,desc")}
                className={`px-2 py-1 rounded-full transition-colors ${
                  sortBy === "likeCount,desc"
                    ? "text-rose-500 bg-rose-50"
                    : "text-gray-500 hover:bg-gray-50"
                }`}
              >
                좋아요순
              </button>
            </div>
          )}
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
              <>
                {comments.map((comment) => (
                  <Comment
                    key={comment.id}
                    comment={comment}
                    boardId={boardId}
                    onRefresh={() => fetchComments(currentPage)}
                    onDelete={handleCommentDelete}
                  />
                ))}
                {pageInfo && pageInfo.totalPage > 1 && (
                  <div className="mt-8 flex justify-center">
                    <Pagination
                      pageInfo={pageInfo}
                      onPageChange={handlePageChange}
                    />
                  </div>
                )}
              </>
            )}
          </div>

          {/* 댓글 작성 폼 - 하단에 배치 */}
          <div className="mt-8 pt-6 border-t">
            <CommentForm boardId={boardId} onSuccess={handleCommentSuccess} />
          </div>
        </div>
      )}
    </div>
  );
}
