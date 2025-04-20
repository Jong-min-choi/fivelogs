import { PageDto } from "./board";

export interface BlogOwnerDto {
  // 아직 미완성이므로 빈 인터페이스로 정의
  nickname?: string;
  introduce?: string;
  profileImageLink?: string;
  myIntroduce?: string;
  isMyBlog?: boolean;
  isFollowing?: boolean;
  followerCount?: number;
  followingCount?: number;
  boardCount?: number;
  viewCount?: number;
}

export interface BoardSummaryDto {
  id: number;
  title: string;
  content: string;
  views: number;
  hashtags: string[];
  created: string;
  updated: string;
  nickname: string;
}

export interface BoardPageResponseDto {
  boardDtoList: BoardSummaryDto[];
  pageDto: PageDto;
}

export interface BlogMainResponseDto {
  boardPageResponseDto: BoardPageResponseDto;
  blogOwnerDto: BlogOwnerDto;
}

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
}
