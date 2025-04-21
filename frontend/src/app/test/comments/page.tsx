"use client";

import { useState, useEffect } from 'react';
import CommentList from '@/components/comment/CommentList';

interface User {
  id: number;
  nickname: string;
  email: string;
}

export default function CommentsTestPage() {
  const [boardId, setBoardId] = useState<number>(1);
  const [user, setUser] = useState<User | null>(null);

  useEffect(() => {
    // 페이지 로드시 자동 로그인
    const autoLogin = async () => {
      try {
        // 먼저 로그인 시도
        const loginRes = await fetch('http://localhost:8090/api/users/login', {
          method: 'POST',
          credentials: 'include',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            email: 'test@test.com',
            password: '1234'
          }),
        });

        if (loginRes.ok) {
          // 로그인 성공 후 사용자 정보 가져오기
          const userRes = await fetch('http://localhost:8090/api/users/me', {
            credentials: 'include',
            headers: {
              'Accept': 'application/json',
              'Content-Type': 'application/json',
            },
          });

          if (userRes.ok) {
            const userData = await userRes.json();
            setUser(userData);
            console.log('테스트 계정으로 자동 로그인 되었습니다.');
          }
        } else {
          console.error('테스트 계정 로그인 실패');
        }
      } catch (error) {
        console.error('자동 로그인 실패:', error);
      }
    };

    autoLogin();
  }, []);

  const handleLogout = async () => {
    try {
      const res = await fetch('http://localhost:8090/api/users/logout', {
        method: 'POST',
        credentials: 'include',
      });

      if (res.ok) {
        setUser(null);
      }
    } catch (error) {
      console.error('로그아웃 실패:', error);
    }
  };

  return (
    <div className="container mx-auto px-4 py-8 max-w-4xl">
      <div className="bg-white rounded-lg shadow-md p-6 mb-8">
        {/* 로그인 상태 표시 */}
        <div className="mb-6 flex justify-between items-center">
          <h1 className="text-2xl font-bold">댓글 테스트 페이지</h1>
          <div>
            {user && (
              <div className="flex items-center gap-4">
                <span className="text-sm text-gray-600">
                  <span className="font-medium">{user.nickname}</span>님 (테스트 계정)
                </span>
                <button
                  onClick={handleLogout}
                  className="text-sm text-rose-600 hover:text-rose-700"
                >
                  로그아웃
                </button>
              </div>
            )}
          </div>
        </div>
        
        {/* 테스트용 게시글 */}
        <div className="mb-8 p-4 bg-gray-50 rounded-lg">
          <h2 className="text-xl font-semibold mb-2">테스트 게시글</h2>
          <p className="text-gray-600 mb-4">
            이 게시글은 댓글 기능을 테스트하기 위한 샘플 게시글입니다.
            댓글, 대댓글, 좋아요/싫어요 기능을 자유롭게 테스트해보세요.
          </p>
          <div className="text-sm text-gray-500">
            게시글 ID: {boardId}
          </div>
        </div>

        {/* 게시글 ID 변경 */}
        <div className="mb-8">
          <label className="block text-sm font-medium text-gray-700 mb-2">
            테스트할 게시글 ID 변경
          </label>
          <div className="flex gap-2">
            <input
              type="number"
              value={boardId}
              onChange={(e) => setBoardId(Number(e.target.value))}
              className="border rounded px-3 py-1 w-32"
              min="1"
            />
            <span className="text-sm text-gray-500 self-center">
              * 다른 게시글의 댓글을 테스트하려면 ID를 변경하세요
            </span>
          </div>
        </div>

        {/* 댓글 컴포넌트 */}
        <div className="border-t pt-6">
          <h3 className="text-lg font-semibold mb-4">댓글</h3>
          <CommentList boardId={boardId} />
        </div>
      </div>
    </div>
  );
} 