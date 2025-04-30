import { useState } from "react";

interface CommentEditProps {
  initialComment: string;
  onSave: (editedComment: string) => Promise<void>;
  onCancel: () => void;
}

export default function CommentEdit({
  initialComment,
  onSave,
  onCancel,
}: CommentEditProps) {
  const [editedComment, setEditedComment] = useState(initialComment);

  return (
    <div className="w-full">
      <textarea
        value={editedComment}
        onChange={(e) => setEditedComment(e.target.value)}
        className="w-full p-2 border rounded-lg resize-none focus:outline-none focus:ring-2 focus:ring-rose-500 focus:border-transparent"
        rows={3}
      />
      <div className="flex justify-end gap-2 mt-2">
        <button
          onClick={onCancel}
          className="px-3 py-1 text-sm text-gray-600 hover:bg-gray-100 rounded-lg transition-colors"
        >
          취소
        </button>
        <button
          onClick={() => onSave(editedComment)}
          className="px-3 py-1 text-sm text-white bg-rose-500 hover:bg-rose-600 rounded-lg transition-colors"
        >
          수정
        </button>
      </div>
    </div>
  );
}
