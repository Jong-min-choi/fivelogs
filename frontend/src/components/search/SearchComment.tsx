import { useState, useEffect } from 'react';
import { useSearchParams } from 'react-router-dom';

interface CommentWithBoardDto {
  boardTitle: string;
  comment: string;
}

interface PageDto {
  startPage: number;
  endPage: number;
  currentPage: number;
  totalPages: number;
}

interface SearchResponse {
  comments: {
    content: CommentWithBoardDto[];
    totalPages: number;
    number: number;
  };
  page: PageDto;
}

export default function SearchComment() {
  const [searchParams] = useSearchParams();
  const [comments, setComments] = useState<CommentWithBoardDto[]>([]);
  const [pageInfo, setPageInfo] = useState<PageDto | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const keyword = searchParams.get('keyword') || '';
  const page = parseInt(searchParams.get('page') || '1');
  const size = parseInt(searchParams.get('size') || '10');

  useEffect(() => {
    const fetchComments = async () => {
      if (!keyword) return;

      setIsLoading(true);
      setError(null);

      try {
        const response = await fetch(
          `http://localhost:8090/api/boards/search-by-comment?keyword=${encodeURIComponent(
            keyword
          )}&page=${page}&size=${size}`
        );

        if (!response.ok) {
          throw new Error('검색 중 오류가 발생했습니다.');
        }

        const data = await response.json();
        if (data.success) {
          setComments(data.data.comments.content);
          setPageInfo(data.data.page);
        } else {
          throw new Error(data.message || '검색 중 오류가 발생했습니다.');
        }
      } catch (err) {
        setError(err instanceof Error ? err.message : '검색 중 오류가 발생했습니다.');
      } finally {
        setIsLoading(false);
      }
    };

    fetchComments();
  }, [keyword, page, size]);

  const handlePageChange = (newPage: number) => {
    const currentParams = new URLSearchParams(searchParams);
    currentParams.set('page', newPage.toString());
    window.history.pushState(null, '', `?${currentParams.toString()}`);
  };

  if (isLoading) {
    return <div className="text-center py-8">검색 중...</div>;
  }

  if (error) {
    return <div className="text-red-500 text-center py-8">{error}</div>;
  }

  if (!comments.length) {
    return <div className="text-center py-8">검색 결과가 없습니다.</div>;
  }

  return (
    <div className="space-y-6">
      {comments.map((item, index) => (
        <div key={index} className="bg-white rounded-lg shadow-sm p-4 border border-gray-200">
          <h3 className="text-lg font-semibold text-rose-500 mb-2">{item.boardTitle}</h3>
          <p className="text-gray-700">{item.comment}</p>
        </div>
      ))}

      {pageInfo && (
        <div className="flex justify-center gap-2 mt-6">
          {Array.from({ length: pageInfo.endPage - pageInfo.startPage + 1 }, (_, i) => {
            const pageNum = pageInfo.startPage + i;
            return (
              <button
                key={pageNum}
                onClick={() => handlePageChange(pageNum)}
                className={`px-3 py-1 rounded ${
                  pageNum === pageInfo.currentPage
                    ? 'bg-rose-500 text-white'
                    : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
                }`}
              >
                {pageNum}
              </button>
            );
          })}
        </div>
      )}
    </div>
  );
} 