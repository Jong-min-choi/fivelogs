import Link from "next/link";
import removeMarkdown from "remove-markdown";

type BoardSummary = {
  id: number;
  title: string;
  content: string;
  views: number;
  hashtags: string[];
  created: string;
  updated: string;
  nickname: string;
};

type Props = {
  boards: BoardSummary[];
  loading: boolean;
  pageInfo: null;
  onPageChange: () => void;
};

export default function TrendingBoards({ boards, loading }: Props) {
  if (loading && boards.length === 0) return <div>로딩중...</div>;
  if (boards.length === 0)
    return (
      <div className="text-center py-10 text-gray-500">게시글이 없습니다.</div>
    );

  return (
    <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
      {boards.map((board) => (
        <div
          key={board.id}
          className="border rounded-lg overflow-hidden shadow-sm hover:shadow-md transition relative"
        >
          <div className="p-4 flex flex-col h-full">
            {/* 조회수: 오른쪽 위 모서리 */}
            <span className="absolute top-3 right-4 flex items-center text-xs text-gray-500">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                className="h-4 w-4 mr-1"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"
                />
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"
                />
              </svg>
              {board.views.toLocaleString()}
            </span>
            <h2 className="text-lg font-bold mb-2">{board.title}</h2>
            <p className="text-gray-600 mb-4">
              {removeMarkdown(
                board.content.length > 100
                  ? board.content.substring(0, 100) + "..."
                  : board.content
              )}
            </p>
            <div className="mt-auto flex flex-col h-full">
              <div className="flex flex-wrap gap-1 mb-3">
                {board.hashtags &&
                  board.hashtags.map((tag, idx) => (
                    <span
                      key={idx}
                      className="text-xs bg-gray-100 text-gray-600 px-2 py-1 rounded-full"
                    >
                      {tag.startsWith("#") ? tag : `#${tag}`}
                    </span>
                  ))}
              </div>
              <div className="flex justify-between items-end mt-auto">
                <div className="flex flex-col items-start">
                  <span className="text-xs text-gray-400 mb-1">
                    {new Date(board.created).toLocaleDateString()}
                    {" by "}
                    <span className="font-bold text-gray-600">
                      {board.nickname}
                    </span>
                  </span>
                </div>
                <div className="flex flex-col items-end">
                  <Link
                    href={`/${board.nickname}/${board.id}`}
                    className="text-rose-500 flex items-center group text-sm"
                  >
                    자세히 보기
                  </Link>
                </div>
              </div>
            </div>
          </div>
        </div>
      ))}
    </div>
  );
}
