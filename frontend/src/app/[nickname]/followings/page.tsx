"use client";

import { useEffect, useState } from "react";

type FollowingUser = {
  id: number;
  nickname: string;
  username: string;
  introduce: string;
  profileImageUrl?: string;
};

export default function FollowingsPage() {
  const [followings, setFollowings] = useState<FollowingUser[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    setFollowings([
      {
        id: 1,
        nickname: "Yeonghwan",
        username: "jghff700",
        introduce: "Non-volatile Memory",
        profileImageUrl: "https://avatars.githubusercontent.com/u/1?v=4",
      },
      {
        id: 2,
        nickname: "미소",
        username: "reum107",
        introduce: "https://blog.areumsheep.vercel.app/ 으로 이동 중 🏃‍♂️",
        profileImageUrl: "https://avatars.githubusercontent.com/u/2?v=4",
      },
      {
        id: 3,
        nickname: "최정민",
        username: "cjm2021401",
        introduce: "DevOps Engineer",
        profileImageUrl: "https://avatars.githubusercontent.com/u/3?v=4",
      },
    ]);
    setLoading(false);
  }, []);

  // 이니셜 추출 함수
  const getInitial = (name: string) => (name ? name[0].toUpperCase() : "?");

  // computerphilosopher 프로필 예시 (이미지 없으면 이니셜)
  const profileImageUrl = ""; // 이미지가 없으면 "" 또는 undefined
  const profileName = "computerphilosopher";

  return (
    <div className="max-w-2xl mx-auto py-10">
      <div className="flex items-center gap-2 mb-6">
        {profileImageUrl ? (
          <img
            src={profileImageUrl}
            alt="profile"
            className="w-7 h-7 rounded-full"
          />
        ) : (
          <div className="w-7 h-7 rounded-full bg-gray-200 flex items-center justify-center text-base font-bold text-gray-600">
            {getInitial(profileName)}
          </div>
        )}
        <span className="font-bold text-lg text-rose-500">{profileName}</span>
        <span className="text-gray-400">{">"}</span>
        <span className="font-semibold text-gray-700">팔로우</span>
      </div>
      <h2 className="text-3xl font-bold mb-6">
        <span className="text-rose-500">{followings.length}명</span>
        <span className="ml-2 text-gray-800">을 팔로우 중</span>
      </h2>
      {loading ? (
        <div className="py-10 text-center text-gray-400">로딩 중...</div>
      ) : (
        <ul className="space-y-6 mt-6">
          {followings.map((user) => (
            <li key={user.id} className="flex items-center gap-4">
              {user.profileImageUrl ? (
                <img
                  src={user.profileImageUrl}
                  alt={user.nickname}
                  className="w-12 h-12 rounded-full object-cover border"
                />
              ) : (
                <div className="w-12 h-12 rounded-full bg-gray-200 flex items-center justify-center text-xl font-bold text-gray-600 border">
                  {getInitial(user.nickname)}
                </div>
              )}
              <div className="flex-1 min-w-0">
                <div className="font-semibold text-gray-900">
                  {user.nickname}{" "}
                  <span className="text-gray-500">@{user.username}</span>
                </div>
                <div className="text-gray-500 text-sm truncate">
                  {user.introduce}
                </div>
              </div>
              <button
                className="border border-emerald-500 text-emerald-600 px-5 py-1.5 rounded-full font-semibold hover:bg-emerald-50 transition"
                type="button"
              >
                팔로우
              </button>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
