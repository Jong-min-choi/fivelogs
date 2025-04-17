import Image from "next/image";

export default function Footer() {
  return (
    <footer className="mt-16 pt-8 border-t">
      <div className="flex flex-col items-center">
        <Image
          src="/next.svg"
          alt="Five Guys 로고"
          width={50}
          height={50}
          className="mb-4"
        />
        <p className="text-gray-500 text-sm mb-4">
          개발자들의 지식과 경험을 공유하는 공간
        </p>
        <p className="text-gray-400 text-sm">
          © 2024 FIVE Log. All rights reserved.
        </p>
      </div>
    </footer>
  );
}
