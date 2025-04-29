"use client";

import { useEffect, useState } from "react";
import { useParams, useRouter } from "next/navigation";
import { useGlobalLoginUser } from "@/stores/auth/loginUser";

type FollowerDto = {
  email: string;
  nickname: string;
  introduce: string;
  profileImageUrl?: string;
  id: number;
};

export default function FollowersPage() {
  const params = useParams();
  const router = useRouter();
  const blogUserNickname = decodeURIComponent(params.nickname as string);

  const [followers, setFollowers] = useState<FollowerDto[]>([]);
  const [myFollowings, setMyFollowings] = useState<FollowerDto[]>([]);
  const [loading, setLoading] = useState(true);

  // 로그인 유저 정보 전역 상태에서 가져오기
  const { isLogin, loginUser } = useGlobalLoginUser();

  // 이니셜 추출 함수
  const getInitial = (name: string) => (name ? name[0].toUpperCase() : "?");

  // 블로그 주인 팔로워 목록 요청
  useEffect(() => {
    const fetchFollowers = async () => {
      setLoading(true);
      try {
        const response = await fetch(
          `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/followerList/${blogUserNickname}`,
          { credentials: "include" }
        );
        const data = await response.json();
        console.log(data.data);
        setFollowers(data.data || []);
      } catch {
        setFollowers([]);
      } finally {
        setLoading(false);
      }
    };
    fetchFollowers();
  }, [blogUserNickname]);

  // 내 팔로잉 목록 요청 (로그인한 경우만)
  useEffect(() => {
    const fetchMyFollowings = async () => {
      if (!isLogin || !loginUser?.nickname) {
        setMyFollowings([]);
        return;
      }
      try {
        const followRes = await fetch(
          `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/followingList/${loginUser.nickname}`,
          { credentials: "include" }
        );
        const followData = await followRes.json();
        setMyFollowings(followData.data || []);
      } catch {
        setMyFollowings([]);
      }
    };
    fetchMyFollowings();
  }, [isLogin, loginUser?.nickname]);

  // 내 팔로잉 목록에 있는지 확인
  const isMyFollowing = (nickname: string) =>
    isLogin && myFollowings.some((f) => f.nickname === nickname);

  return (
    <div className="max-w-2xl mx-auto py-10">
      <h2 className="text-3xl font-bold mb-6">
        <span className="ml-2 text-gray-800">{blogUserNickname} </span>
        <span className="text-sm ml-2 text-gray-800">
          {">"} {followers.length}명이 팔로워
        </span>
      </h2>
      {loading ? (
        <div className="py-10 text-center text-gray-400">로딩 중...</div>
      ) : (
        <ul className="space-y-6 mt-6">
          {followers.map((user) => (
            <li key={user.nickname} className="flex items-center gap-4">
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
                  <span className="text-gray-500">{user.email}</span>
                </div>
                <div className="text-gray-500 text-sm truncate">
                  {user.introduce}
                </div>
              </div>
              {isLogin && isMyFollowing(user.nickname) ? (
                <button
                  className="border border-rose-400 text-rose-500 px-5 py-1.5 rounded-full font-semibold hover:bg-rose-50 transition"
                  type="button"
                  onClick={async () => {
                    try {
                      const res = await fetch(
                        `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/unfollow/${user.id}`,
                        {
                          method: "DELETE",
                          credentials: "include",
                        }
                      );
                      if (!res.ok) throw new Error("언팔로우 실패");
                      setMyFollowings((prev) =>
                        prev.filter((f) => f.nickname !== user.nickname)
                      );
                    } catch {
                      alert("언팔로우에 실패했습니다.");
                    }
                  }}
                >
                  언팔로우
                </button>
              ) : (
                <button
                  className="border border-emerald-500 text-emerald-600 px-5 py-1.5 rounded-full font-semibold hover:bg-emerald-50 transition"
                  type="button"
                  onClick={async () => {
                    if (!isLogin) {
                      alert("로그인 후 이용해주세요.");
                      router.push("/users/login");
                      return;
                    }
                    try {
                      console.log(`${user.id} user id`);
                      const res = await fetch(
                        `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/follow/${user.id}`,
                        {
                          method: "POST",
                          credentials: "include",
                        }
                      );
                      if (!res.ok) throw new Error("팔로우 실패");
                      setMyFollowings((prev) => [...prev, user]);
                    } catch {
                      alert("팔로우에 실패했습니다.");
                    }
                  }}
                >
                  팔로우
                </button>
              )}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
