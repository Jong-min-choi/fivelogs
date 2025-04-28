import Image from "next/image";

export default function Footer() {
  return (
    <footer className="mt-16 pt-8 border-t bg-white">
      <div className="flex flex-col items-start px-8">
        <Image
          src="/logo2.png"
          alt="Five Guys 로고"
          width={80}
          height={80}
          className="mb-4"
        />
        <p className="text-gray-500 text-sm mb-4 text-left">
          Five log는 고객의 소중한 의견을 바탕으로 더 나은 서비스를 제공하기
          위해 노력합니다.
          <br />
          고객님의 소중한 의견을 언제든지 환영합니다.
        </p>
        <div className="text-gray-400 text-xs text-left mb-4 flex space-x-8">
          <p>
            팀장: 최종민 ㅣ 부팀장: 강준호 ㅣ 팀원 : 강성민 , 이호준 , 유광륜{" "}
          </p>
        </div>
        <p className="text-gray-400 text-sm">
          © 2025 FIVE Log. All rights reserved.
        </p>
      </div>
    </footer>
  );
}
