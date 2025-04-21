export interface CommentType {
    id: number;
    comment: string;
    createdDate: string;
    updatedDate?: string;
    likeCount: number;
    dislikeCount: number;
    likedByUser?: boolean;
    replies: CommentType[];
  }