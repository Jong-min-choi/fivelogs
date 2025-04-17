"use client";

import { useEffect } from "react";
import React from "react";
import Header from "@/components/layout/Header";
import Footer from "@/components/layout/Footer";
import { LoginUserContext, useLoginUser } from "@/stores/auth/loginUser";

type LayoutProps = {
  children: React.ReactNode;
};

export default function ClientLayout({ children }: LayoutProps) {
  const fetchUser = useEffect(() => {
    fetch("http://localhost:8090/api/users/me", {
      method: "GET",
      credentials: "include",
    })
      .then((response) => response.json())
      .then((data) => {
        console.log(data);
        setLoginUser(data.data);
      })
      .catch((error) => {
        setNoLoginUser();
      });
  }, []);
  const {
    loginUser,
    setLoginUser,
    isLoginUserPending,
    isLogin,
    setNoLoginUser,
    logout,
    logoutAndHome,
  } = useLoginUser();
  //전역 관리를 위한 Store 등록
  const loginUserContextValue = {
    loginUser,
    setLoginUser,
    setNoLoginUser,
    isLoginUserPending,
    isLogin,
    logout,
    logoutAndHome,
  };
  return (
    <LoginUserContext.Provider value={loginUserContextValue}>
      <main className="container mx-auto px-2 py-8">
        <Header />
        {children}
        <Footer />
      </main>
    </LoginUserContext.Provider>
  );
}
