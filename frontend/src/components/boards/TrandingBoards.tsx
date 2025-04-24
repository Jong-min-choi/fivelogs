import Link from "next/link";

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
          className="border rounded-lg overflow-hidden shadow-sm hover:shadow-md transition"
        >
          <div className="p-4 flex flex-col h-full">
            <div className="flex items-center justify-between text-sm text-gray-500 mb-2">
              <span>{board.created}</span>
              <span className="flex items-center">
                조회 {board.views?.toLocaleString()}
              </span>
            </div>
            <h2 className="text-lg font-bold mb-2">{board.title}</h2>
            <p className="text-gray-600 mb-4">
              {board.content.length > 100
                ? board.content.substring(0, 100) + "..."
                : board.content}
            </p>
            <div className="mt-auto">
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
              <Link
                href={`/${board.nickname}/${board.id}`}
                className="text-rose-500 flex items-center group"
              >
                자세히 보기
              </Link>
            </div>
          </div>
        </div>
      ))}
    </div>
  );
}
