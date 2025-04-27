export interface CommentType {
    id: number;
    comment: string;
    nickname: string;
    createdDate: string;
    updatedDate?: string | null;
    likeCount: number;
    dislikeCount: number;
    likedByUser?: boolean;
    deleted?: boolean;
    replies: CommentType[];
    likedByMe?: boolean | null;
  }