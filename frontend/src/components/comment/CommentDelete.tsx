interface CommentDeleteProps {
  onDelete: () => Promise<void>;
}

export default function CommentDelete({ onDelete }: CommentDeleteProps) {
  const handleDelete = async () => {
    if (!window.confirm("정말로 이 댓글을 삭제하시겠습니까?")) return;
    await onDelete();
  };

  return (
    <button
      onClick={handleDelete}
      className="w-full px-4 py-2 text-sm text-left text-red-600 hover:bg-gray-50 rounded-b-lg"
    >
      삭제
    </button>
  );
}
