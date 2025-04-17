import React from "react";
import Header from "./Header";
import Footer from "./Footer";

type LayoutProps = {
  children: React.ReactNode;
};

export default function Layout({ children }: LayoutProps) {
  return (
    <div className="container mx-auto px-2 py-8">
      <Header />
      {children}
      <Footer />
    </div>
  );
}
