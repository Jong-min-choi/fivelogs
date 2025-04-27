import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  /* config options here */
  images: {
    domains: ["localhost", "fivelogs-bucket.s3.ap-northeast-2.amazonaws.com"], // 👈 이거 추가!
  },
};

export default nextConfig;
