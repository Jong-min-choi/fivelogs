export type Board = {
  id: number;
  title: string;
  content: string;
  views: number;
  hashtags: string[];
  created: string;
  updated: string | null;
};

export type PageDto = {
  startPage: number;
  endPage: number;
  presentPage: number;
  totalPage: number;
  first: boolean;
  last: boolean;
};

export type BoardResponseData = {
  boardDtoList: Board[];
  pageDto: PageDto;
};

export type BoardApiResponse = {
  success: boolean;
  message: string;
  data: BoardResponseData;
};
