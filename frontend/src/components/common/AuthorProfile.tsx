import React from "react";

interface AuthorProfileProps {
  nickName: string;
  profileImageLink?: string;
  myIntroduce?: string;
}

export default function AuthorProfile({
  nickName,
  profileImageLink,
  myIntroduce,
}: AuthorProfileProps) {
  return (
    <div className="bg-gray-50 p-6 rounded-lg mb-8 border">
      <div className="flex items-center">
        <div className="w-16 h-16 rounded-full bg-gray-200 flex items-center justify-center text-gray-600 mr-4 overflow-hidden">
          {profileImageLink ? (
            <img
              src={profileImageLink}
              alt={nickName}
              className="w-full h-full object-cover"
            />
          ) : (
            <span className="text-2xl font-bold">{nickName.charAt(0)}</span>
          )}
        </div>
        <div>
          <div className="text-lg font-bold mb-1">{nickName}</div>
          {myIntroduce && <div className="text-gray-600">{myIntroduce}</div>}
        </div>
      </div>
    </div>
  );
}
