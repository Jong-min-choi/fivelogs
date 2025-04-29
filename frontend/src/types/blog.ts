import { PageDto } from "./board";

export interface BlogOwnerDto {
  id: number;
  nickname: string;
  introduction: string;
  profileImage: string;
  githubLink?: string;
  instagramLink?: string;
  twitterLink?: string;
  introduce?: string;
  profileImageLink?: string;
  myIntroduce?: string;
  isMyBlog?: boolean;
  followerCount?: number;
  followingCount?: number;
  boardCount?: number;
  viewCount?: number;
  profileImageUrl?: string;
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

export interface HashtagCountDto {
  name: string;
  count: number;
}

export interface SNSLinkResponseDto {
  githubLink: string;
  instagramLink: string;
  twitterLink: string;
}
