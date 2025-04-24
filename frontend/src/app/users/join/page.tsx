"use client";
import { useState } from "react";
import Layout from "@/app/ClientLayout";
import Link from "next/link";
import Image from "next/image";
import { useRouter } from "next/navigation";
import { env } from "process";

export default function JoinPage() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [passwordConfirm, setPasswordConfirm] = useState("");
  const [nickname, setNickname] = useState("");
  const [isVerifying, setIsVerifying] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState("");
  const router = useRouter();

  // 이메일 인증 관련 상태
  const [isCodeSent, setIsCodeSent] = useState(false);
  const [isEmailVerified, setIsEmailVerified] = useState(false);
  const [code, setCode] = useState("");
  const [isCodeVerifying, setIsCodeVerifying] = useState(false);

  // 닉네임 중복 확인 관련 상태
  const [isNicknameChecking, setIsNicknameChecking] = useState(false);
  const [isNicknameAvailable, setIsNicknameAvailable] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    // 이메일 인증 확인
    if (!isEmailVerified) {
      setError("이메일 인증이 필요합니다.");
      return;
    }

    // 닉네임 중복 확인
    if (!isNicknameAvailable) {
      setError("닉네임 중복 확인이 필요합니다.");
      return;
    }

    // 비밀번호 확인
    if (password !== passwordConfirm) {
      setError("비밀번호가 일치하지 않습니다.");
      return;
    }

    try {
      setIsLoading(true);
      setError("");

      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/users/join`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            email,
            password,
            nickname,
          }),
        }
      );

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "회원가입에 실패했습니다.");
      }

      const data = await response.json();
      console.log("회원가입 성공:", data);

      // 회원가입 성공 후 로그인 페이지로 이동
      alert("회원가입이 완료되었습니다. 로그인 페이지로 이동합니다.");
      router.push("/users/login");
    } catch (err) {
      console.error("회원가입 오류:", err);
      setError(
        err instanceof Error ? err.message : "회원가입 중 오류가 발생했습니다."
      );
    } finally {
      setIsLoading(false);
    }
  };

  // 인증코드 발송 함수
  const sendCode = async () => {
    if (!email || !email.includes("@")) {
      setError("유효한 이메일을 입력해주세요.");
      return;
    }

    try {
      setIsLoading(true);
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/email/send`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({ email }),
        }
      );

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "인증 코드 발송에 실패했습니다.");
      }

      setIsCodeSent(true);
      setError("");
      alert("인증 코드가 이메일로 전송되었습니다.");
    } catch (err) {
      console.log(err.message);
      if (err.message === "이미 사용 중인 이메일입니다.") {
        setError("이미 사용 중인 이메일입니다.");
      } else {
        setError("인증 코드 발송에 실패했습니다. 다시 시도해주세요.");
      }
    } finally {
      setIsLoading(false);
    }
  };

  // 인증코드 검증 함수
  const verifyCode = async () => {
    if (!code) {
      setError("인증 코드를 입력해주세요.");
      return;
    }

    try {
      setIsCodeVerifying(true);
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/email/verify`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({ email, code }),
        }
      );

      if (!response.ok) {
        throw new Error("인증 코드가 잘못되었습니다.");
      }

      setIsEmailVerified(true);
      setError("");
      alert("이메일 인증이 완료되었습니다!");
    } catch (err) {
      console.error("인증 실패:", err);
      setError("인증 코드가 잘못되었습니다. 다시 확인해주세요.");
    } finally {
      setIsCodeVerifying(false);
    }
  };

  // 닉네임 중복 확인 함수
  const checkNicknameAvailability = async () => {
    if (!nickname) {
      setError("닉네임을 입력해주세요.");
      return;
    }

    try {
      setIsNicknameChecking(true);
      setError("");

      const url = `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/users/nickname-exists?nickname=${nickname}`;
      const response = await fetch(url);

      if (!response.ok) {
        throw new Error("닉네임 중복 확인에 실패했습니다.");
      }

      const data = await response.json();

      if (data.success) {
        const exists = data.data.exists;

        if (exists) {
          setIsNicknameAvailable(false);
          setError("이미 사용 중인 닉네임입니다. 다른 닉네임을 입력해주세요.");
        } else {
          setIsNicknameAvailable(true);
          setError("");
          alert("사용 가능한 닉네임입니다!");
        }
      } else {
        throw new Error(data.message || "닉네임 중복 확인에 실패했습니다.");
      }
    } catch (err) {
      console.error("닉네임 중복 확인 오류:", err);
      setError("닉네임 중복 확인에 실패했습니다. 다시 시도해주세요.");
      setIsNicknameAvailable(false);
    } finally {
      setIsNicknameChecking(false);
    }
  };

  // 닉네임 변경 시 중복 확인 초기화
  const handleNicknameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setNickname(e.target.value);
    setIsNicknameAvailable(false); // 닉네임이 변경되면 중복 확인 상태 초기화
  };

  return (
    <>
      <div className="max-w-md mx-auto my-8 p-6 bg-white rounded-lg shadow-sm">
        <h1 className="text-2xl font-bold text-center mb-8">회원가입</h1>

        {error && (
          <div className="mb-4 p-2 bg-red-100 text-red-700 rounded">
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit}>
          <div className="mb-4">
            <label htmlFor="email" className="block mb-2 text-sm font-medium">
              이메일
            </label>
            <div className="flex space-x-2">
              <input
                type="email"
                id="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                className="flex-1 p-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-rose-300"
                placeholder="이메일을 입력하세요"
                required
                disabled={isEmailVerified}
              />
              <button
                type="button"
                className="px-4 py-2 bg-rose-400 text-white rounded hover:bg-rose-500 transition disabled:bg-gray-300"
                onClick={sendCode}
                disabled={isLoading || !email || isEmailVerified}
              >
                {isLoading ? "전송 중..." : isCodeSent ? "재전송" : "인증 요청"}
              </button>
            </div>
          </div>

          {isCodeSent && !isEmailVerified && (
            <div className="mb-4">
              <label htmlFor="code" className="block mb-2 text-sm font-medium">
                인증 코드
              </label>
              <div className="flex space-x-2">
                <input
                  type="text"
                  id="code"
                  value={code}
                  onChange={(e) => setCode(e.target.value)}
                  className="flex-1 p-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-rose-300"
                  placeholder="인증 코드를 입력하세요"
                  required
                />
                <button
                  type="button"
                  className="px-4 py-2 bg-rose-400 text-white rounded hover:bg-rose-500 transition"
                  onClick={verifyCode}
                  disabled={isCodeVerifying || !code}
                >
                  {isCodeVerifying ? "확인 중..." : "확인"}
                </button>
              </div>
              <p className="mt-1 text-xs text-gray-500">
                이메일로 전송된 인증 코드를 입력해주세요.
              </p>
            </div>
          )}

          {isEmailVerified && (
            <div className="mb-4 p-2 bg-green-100 text-green-700 rounded flex items-center">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                className="h-5 w-5 mr-2"
                viewBox="0 0 20 20"
                fill="currentColor"
              >
                <path
                  fillRule="evenodd"
                  d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z"
                  clipRule="evenodd"
                />
              </svg>
              이메일 인증이 완료되었습니다.
            </div>
          )}

          <div className="mb-4">
            <label
              htmlFor="password"
              className="block mb-2 text-sm font-medium"
            >
              비밀번호
            </label>
            <input
              type="password"
              id="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="w-full p-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-rose-300"
              placeholder="비밀번호를 입력하세요"
              required
            />
          </div>

          <div className="mb-4">
            <label
              htmlFor="passwordConfirm"
              className="block mb-2 text-sm font-medium"
            >
              비밀번호 확인
            </label>
            <input
              type="password"
              id="passwordConfirm"
              value={passwordConfirm}
              onChange={(e) => setPasswordConfirm(e.target.value)}
              className="w-full p-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-rose-300"
              placeholder="비밀번호를 다시 입력하세요"
              required
            />
          </div>

          <div className="mb-6">
            <label
              htmlFor="nickname"
              className="block mb-2 text-sm font-medium"
            >
              닉네임
            </label>
            <div className="flex space-x-2">
              <input
                type="text"
                id="nickname"
                value={nickname}
                onChange={handleNicknameChange}
                className="flex-1 p-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-rose-300"
                placeholder="닉네임을 입력하세요"
                required
                disabled={isNicknameAvailable}
              />
              <button
                type="button"
                className="px-4 py-2 bg-rose-400 text-white rounded hover:bg-rose-500 transition disabled:bg-gray-300"
                onClick={checkNicknameAvailability}
                disabled={
                  isNicknameChecking || !nickname || isNicknameAvailable
                }
              >
                {isNicknameChecking ? "확인 중..." : "중복확인"}
              </button>
            </div>
            {isNicknameAvailable && (
              <div className="mt-2 p-2 bg-green-100 text-green-700 rounded flex items-center text-sm">
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  className="h-4 w-4 mr-2"
                  viewBox="0 0 20 20"
                  fill="currentColor"
                >
                  <path
                    fillRule="evenodd"
                    d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z"
                    clipRule="evenodd"
                  />
                </svg>
                사용 가능한 닉네임입니다.
              </div>
            )}
          </div>

          <button
            type="submit"
            className="w-full py-3 mt-4 bg-rose-400 text-white rounded hover:bg-rose-500 transition disabled:bg-gray-300"
            disabled={isLoading || !isEmailVerified || !isNicknameAvailable}
          >
            {isLoading ? "처리 중..." : "회원가입"}
          </button>
        </form>

        <div className="mt-4 text-center text-sm text-gray-500">
          이미 계정이 있으신가요?{" "}
          <Link href="/users/login" className="text-rose-500 hover:underline">
            로그인
          </Link>
        </div>
      </div>

      <div className="flex justify-center mt-4">
        <Link href="/" className="inline-block mx-auto">
          <Image
            src="/next.svg"
            alt="Five Guys Logo"
            width={120}
            height={80}
            className="object-contain"
          />
        </Link>
      </div>

      <div className="text-center text-sm text-gray-500 mt-4 mb-8">
        개발자들의 지식과 경험을 공유하는 공간
      </div>

      <div className="text-center text-xs text-gray-400 mt-8">
        © 2024 FIVE Log. All rights reserved.
      </div>
    </>
  );
}
