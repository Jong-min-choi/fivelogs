"use client";

import { useEffect, useState } from "react";

type FollowerUser = {
  id: number;
  nickname: string;
  username: string;
  introduce: string;
  profileImageUrl?: string;
};

export default function FollowersPage() {
  const [followers, setFollowers] = useState<FollowerUser[]>([
    {
      id: 1,
      nickname: "김소연",
      username: "soyeon58",
      introduce: "studying AI",
      profileImageUrl: "https://avatars.githubusercontent.com/u/100?v=4",
    },
    {
      id: 2,
      nickname: "CodeWisp",
      username: "codewisp",
      introduce: "진화를 택하지 못한 이브이같은 프로그래머",
      profileImageUrl: "https://avatars.githubusercontent.com/u/101?v=4",
    },
    {
      id: 3,
      nickname: "사리미",
      username: "sarimi",
      introduce: "",
      profileImageUrl: "",
    },
    {
      id: 4,
      nickname: "2400",
      username: "espada105",
      introduce: "시즌 2의 공부기록 · Artificial Intelligence & AeroSpace",
      profileImageUrl: "https://avatars.githubusercontent.com/u/102?v=4",
    },
    {
      id: 5,
      nickname: "고양이 개발 공화국",
      username: "wjdhgcks6735",
      introduce: "부진은 있어도 몰락은 없다",
      profileImageUrl: "https://avatars.githubusercontent.com/u/103?v=4",
    },
  ]);
  const followerCount = 29; // 실제로는 followers.length 또는 API 값 사용

  // 이니셜 추출 함수
  const getInitial = (name: string) => (name ? name[0].toUpperCase() : "?");

  // computerphilosopher 프로필 이미지 (없으면 이니셜)
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
        <span className="font-semibold text-gray-700">팔로워</span>
      </div>
      <h2 className="text-3xl font-bold mb-2">
        <span className="text-rose-500">{followerCount}명</span>
        <span className="ml-2 text-gray-800">의 팔로워</span>
      </h2>
      <ul className="space-y-6 mt-6">
        {followers.map((user) => (
          <li key={user.id} className="flex items-center gap-4">
            {user.profileImageUrl && user.profileImageUrl !== "" ? (
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
    </div>
  );
}
