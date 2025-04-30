interface CommentHeaderProps {
  nickname: string;
  createdDate: string;
  updatedDate?: string | null;
  deleted: boolean;
}

export default function CommentHeader({
  nickname,
  createdDate,
  updatedDate,
  deleted,
}: CommentHeaderProps) {
  const displayNickname = deleted
    ? "삭제된 사용자"
    : nickname || "알 수 없는 사용자";

  return (
    <div className="text-sm text-gray-500 mb-1">
      <span className="font-medium">{displayNickname}</span>
      <span className="mx-1">•</span>
      <span>{new Date(createdDate).toLocaleString()}</span>
      {updatedDate && !deleted && (
        <span className="ml-1 text-xs text-gray-400">(수정됨)</span>
      )}
    </div>
  );
}
