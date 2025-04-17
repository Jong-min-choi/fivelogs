"use client";
import { useEffect, useState } from "react";
import Layout from "@/app/ClientLayout";
import { useParams } from "next/navigation";
import Link from "next/link";

export default function BoardDetail() {
  const params = useParams();
  const boardName = params?.blogName || "";
  const [loading, setLoading] = useState(true);
  const [board, setBoard] = useState<any>(null);

  useEffect(() => {
    console.log("Board Name:", boardName); // 디버깅용 로그

    // 실제로는 boardName을 기반으로 API 호출하여 데이터를 가져올 수 있습니다
    // 여기서는 임시 데이터를 사용합니다
    const dummyBoards = {
      "1": {
        category: "기술",
        date: "2024.02.15",
        title: "효율적인 코드 작성을 위한 10가지 팁",
        content: `
          <h2>1. 일관된 코딩 스타일 유지하기</h2>
          <p>코드 스타일 가이드를 따르면 가독성이 향상됩니다. 팀 내에서 코드 스타일 가이드를 만들고 따르세요.</p>
          
          <h2>2. 적절한 주석 사용하기</h2>
          <p>코드가 '무엇'을 하는지가 아닌 '왜' 그렇게 하는지 설명하는 주석이 유용합니다.</p>
          
          <h2>3. 함수는 한 가지 일만 수행하도록 하기</h2>
          <p>함수가 한 가지 작업만 수행하도록 하면 테스트와 유지보수가 쉬워집니다.</p>
          
          <h2>4. DRY 원칙 지키기</h2>
          <p>Don't Repeat Yourself(반복하지 말 것). 코드 중복을 피하고 재사용 가능한 함수나 클래스로 만드세요.</p>
          
          <h2>5. 변수명과 함수명에 신경 쓰기</h2>
          <p>의미 있는 이름을 사용하면 코드의 이해도가 높아집니다.</p>
          
          <h2>6. 에러 처리하기</h2>
          <p>발생할 수 있는 예외 상황을 고려하고 적절하게 처리하세요.</p>
          
          <h2>7. 코드 리팩토링 주기적으로 하기</h2>
          <p>기능 추가보다 코드 품질 향상에 집중하는 시간을 가지세요.</p>
          
          <h2>8. 테스트 코드 작성하기</h2>
          <p>자동화된 테스트는 버그를 조기에 발견하고 코드 품질을 높여줍니다.</p>
          
          <h2>9. 버전 관리 시스템 활용하기</h2>
          <p>Git과 같은 버전 관리 도구를 사용하여 코드 변경 사항을 체계적으로 관리하세요.</p>
          
          <h2>10. 지속적인 학습과 개선</h2>
          <p>새로운 기술과 방법론을 배우고 적용하여 코드 품질을 지속적으로 향상시키세요.</p>
        `,
        author: "김개발",
        viewCount: 1234,
        relatedBoards: [
          { id: "2", title: "서울의 숨은 카페 명소 TOP 10" },
          { id: "5", title: "신기술 트렌드 분석" },
          { id: "11", title: "React 18 신기능 완벽 가이드" },
        ],
      },
      "2": {
        category: "라이프스타일",
        date: "2024.02.14",
        title: "서울의 숨은 카페 명소 TOP 10",
        content: `
          <h2>1. 연남동 '카페 골목'</h2>
          <p>힙스터들의 성지로 알려진 연남동에 위치한 이 카페는 특별한 분위기와 맛있는 커피를 자랑합니다.</p>
          
          <h2>2. 성수동 '인더스트리얼 카페'</h2>
          <p>공장 지대였던 성수동의 특징을 살린 인더스트리얼 감성의 카페입니다.</p>
          
          <h2>3. 이태원 '루프탑 뷰'</h2>
          <p>서울 시내를 한눈에 내려다볼 수 있는 루프탑 카페입니다.</p>
          
          <h2>4. 북촌 '한옥 카페'</h2>
          <p>전통 한옥의 매력과 현대적인 카페 문화가 어우러진 곳입니다.</p>
          
          <h2>5. 홍대 '아트 카페'</h2>
          <p>독특한 예술 작품들로 가득한 갤러리 같은 카페입니다.</p>
          
          <h2>6. 경리단길 '숨은 골목 카페'</h2>
          <p>좁은 골목 안에 숨어있는 아늑한 카페입니다.</p>
          
          <h2>7. 삼청동 '정원 카페'</h2>
          <p>아름다운 정원 뷰를 자랑하는 카페입니다.</p>
          
          <h2>8. 을지로 '레트로 카페'</h2>
          <p>옛날 감성을 되살린 레트로풍 인테리어의 카페입니다.</p>
          
          <h2>9. 망원동 '동네 카페'</h2>
          <p>로컬들이 사랑하는 아늑한 동네 카페입니다.</p>
          
          <h2>10. 가로수길 '디저트 카페'</h2>
          <p>맛있는 디저트와 커피를 함께 즐길 수 있는 카페입니다.</p>
        `,
        author: "박여행",
        viewCount: 987,
        relatedBoards: [
          { id: "3", title: "한국의 전통 사찰 여행기" },
          { id: "7", title: "클라우드 서비스 비교" },
          { id: "1", title: "효율적인 코드 작성을 위한 10가지 팁" },
        ],
      },
      "3": {
        category: "여행",
        date: "2024.02.13",
        title: "한국의 전통 사찰 여행기",
        content: `
          <h2>불국사</h2>
          <p>경상북도 경주시에 위치한 대표적인 불교 사찰로, 석가탑과 다보탑 등 유네스코 세계문화유산이 있습니다.</p>
          
          <h2>해인사</h2>
          <p>경상남도 합천군에 위치한 사찰로, 팔만대장경을 보관하고 있는 장경판전이 유명합니다.</p>
          
          <h2>송광사</h2>
          <p>전라남도 순천시에 있는 사찰로, 16국사를 배출한 승보사찰입니다.</p>
          
          <h2>통도사</h2>
          <p>경상남도 양산시에 위치하며, 불보사찰로 석가모니의 진신사리를 모시고 있습니다.</p>
        `,
        author: "이여행",
        viewCount: 756,
        relatedBoards: [
          { id: "2", title: "서울의 숨은 카페 명소 TOP 10" },
          { id: "5", title: "신기술 트렌드 분석" },
          { id: "8", title: "마이크로서비스 아키텍처 설계" },
        ],
      },
      "0320kangk": {
        category: "개발",
        date: "2024.03.20",
        title: "Next.js로 블로그 만들기",
        content: `
          <h2>Next.js 소개</h2>
          <p>Next.js는 React 기반의 프레임워크로, 서버 사이드 렌더링과 정적 사이트 생성을 지원합니다.</p>
          
          <h2>프로젝트 설정</h2>
          <p>create-next-app을 사용하여 쉽게 프로젝트를 시작할 수 있습니다.</p>
          
          <h2>페이지 라우팅</h2>
          <p>파일 기반 라우팅 시스템으로 페이지를 쉽게 구성할 수 있습니다.</p>
          
          <h2>데이터 불러오기</h2>
          <p>getServerSideProps 또는 getStaticProps를 사용하여 데이터를 불러올 수 있습니다.</p>
          
          <h2>스타일링</h2>
          <p>CSS Modules, Tailwind CSS 등 다양한 스타일링 방법을 지원합니다.</p>
        `,
        author: "강개발",
        viewCount: 420,
        relatedBoards: [
          { id: "1", title: "효율적인 코드 작성을 위한 10가지 팁" },
          { id: "6", title: "AI 개발 시작하기" },
          { id: "11", title: "React 18 신기능 완벽 가이드" },
        ],
      },
    };

    // 데이터 로딩 시뮬레이션
    setTimeout(() => {
      // 블로그 ID에 해당하는 게시글 찾기
      const foundBoard =
        dummyBoards[boardName as keyof typeof dummyBoards] || dummyBoards["1"];
      setBoard(foundBoard);
      setLoading(false);
    }, 500);
  }, [boardName]);

  if (loading) {
    return (
      <Layout>
        <div className="flex justify-center items-center h-96">
          <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-rose-500"></div>
        </div>
      </Layout>
    );
  }

  return (
    <main className="py-6">
      {/* 블로그 헤더 */}
      <div className="mb-8">
        <div className="flex items-center text-sm text-gray-500 mb-2">
          <span>{board.category}</span>
          <span className="mx-2">•</span>
          <span>{board.date}</span>
        </div>
        <h1 className="text-3xl font-bold mb-4">{board.title}</h1>
        <div className="flex justify-between items-center">
          <div className="flex items-center">
            <div className="w-10 h-10 rounded-full bg-gray-200 flex items-center justify-center text-gray-600 mr-3">
              {board.author.charAt(0)}
            </div>
            <span>{board.author}</span>
          </div>
          <div className="text-gray-500">조회수 {board.viewCount}회</div>
        </div>
      </div>

      {/* 블로그 콘텐츠 */}
      <div
        className="prose prose-rose max-w-none mb-12"
        dangerouslySetInnerHTML={{ __html: board.content }}
      ></div>

      {/* 추천 게시글 */}
      <div className="border-t pt-8">
        <h3 className="text-xl font-bold mb-4">관련 게시글</h3>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          {board.relatedBoards.map((relatedBoard: any) => (
            <Link
              key={relatedBoard.id}
              href={`/board/${relatedBoard.id}`}
              className="p-4 border rounded-lg hover:shadow-md transition"
            >
              <h4 className="font-medium">{relatedBoard.title}</h4>
            </Link>
          ))}
        </div>
      </div>

      {/* 이전/다음 버튼 */}
      <div className="flex justify-between mt-12">
        <Link href="/board" className="flex items-center text-rose-500">
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
              d="M15 19l-7-7 7-7"
            />
          </svg>
          목록으로
        </Link>
        <div className="flex gap-4">
          <button className="px-4 py-2 border rounded-md hover:bg-gray-50 transition">
            이전 글
          </button>
          <button className="px-4 py-2 border rounded-md hover:bg-gray-50 transition">
            다음 글
          </button>
        </div>
      </div>
    </main>
  );
}
