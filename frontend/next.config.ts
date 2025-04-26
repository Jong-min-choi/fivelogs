import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  /* config options here */
  images: {
    domains: ["localhost"], // 👈 이거 추가!
  },
};

export default nextConfig;
