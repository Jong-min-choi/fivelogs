import React from "react";

type LoadingSpinnerProps = {
  size?: "sm" | "md" | "lg";
  color?: string;
  height?: string;
};

export default function LoadingSpinner({
  size = "md",
  color = "rose-500",
  height = "h-96",
}: LoadingSpinnerProps) {
  const spinnerSizes = {
    sm: "h-8 w-8",
    md: "h-12 w-12",
    lg: "h-16 w-16",
  };

  return (
    <div className={`flex justify-center items-center ${height}`}>
      <div
        className={`animate-spin rounded-full ${spinnerSizes[size]} border-t-2 border-b-2 border-${color}`}
      />
    </div>
  );
}
