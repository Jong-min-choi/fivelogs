"use client";

import { useRouter } from "next/navigation";
import { useState } from "react";

interface BlogPost {
  id: number;
  title: string;
  content: string;
  date: string;
  author: string;
  category: string;
}

// This would typically come from an API or database
const getBlogPost = (id: string): BlogPost | null => {
  const posts: Record<string, BlogPost> = {
    "1": {
      id: 1,
      title: "Getting Started with Next.js",
      content: `
        Next.js is a React framework that enables server-side rendering and generating static websites.
        It's a great choice for building modern web applications.
        
        ## Why Next.js?
        
        - Server-side rendering
        - Static site generation
        - API routes
        - File-based routing
        - Built-in optimizations
        
        ## Getting Started
        
        To create a new Next.js project, run:
        
        \`\`\`bash
        npx create-next-app@latest my-app
        \`\`\`
      `,
      date: "2024-04-16",
      author: "John Doe",
      category: "Development",
    },
    "2": {
      id: 2,
      title: "The Future of Web Development",
      content: `
        Web development is constantly evolving. Let's explore some of the latest trends and technologies.
        
        ## Key Trends
        
        - AI and Machine Learning
        - WebAssembly
        - Progressive Web Apps
        - Serverless Architecture
        
        ## What to Expect
        
        The future of web development is exciting and full of possibilities.
      `,
      date: "2024-04-15",
      author: "Jane Smith",
      category: "Technology",
    },
  };

  return posts[id] || null;
};

export default function BlogPost({ params }: { params: { id: string } }) {
  const router = useRouter();
  const [isDeleting, setIsDeleting] = useState(false);
  const post = getBlogPost(params.id);

  const handleEdit = () => {
    router.push(`/blog/${params.id}/edit`);
  };

  const handleDelete = async () => {
    if (window.confirm("Are you sure you want to delete this post?")) {
      setIsDeleting(true);
      try {
        // TODO: Replace with actual API call
        // await fetch(`/api/posts/${params.id}`, { method: 'DELETE' });
        router.push("/blog");
      } catch (error) {
        console.error("Failed to delete post:", error);
        alert("Failed to delete post. Please try again.");
      } finally {
        setIsDeleting(false);
      }
    }
  };

  if (!post) {
    return (
      <div className="text-center py-12">
        <h1 className="text-2xl font-bold text-gray-900">Post not found</h1>
        <p className="mt-4 text-gray-600">
          The blog post you're looking for doesn't exist.
        </p>
      </div>
    );
  }

  return (
    <article className="max-w-3xl mx-auto">
      <header className="mb-8">
        <div className="flex items-center text-sm text-gray-500 mb-4">
          <span>{post.date}</span>
          <span className="mx-2">•</span>
          <span>{post.author}</span>
          <span className="mx-2">•</span>
          <span className="inline-flex items-center px-3 py-1 rounded-full text-sm font-medium bg-blue-100 text-blue-800">
            {post.category}
          </span>
        </div>
        <div className="flex justify-between items-center">
          <h1 className="text-4xl font-bold text-gray-900">{post.title}</h1>
          <div className="flex space-x-2">
            <button
              onClick={handleEdit}
              className="px-4 py-2 text-sm font-medium text-white bg-blue-600 rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
            >
              Edit
            </button>
            <button
              onClick={handleDelete}
              disabled={isDeleting}
              className="px-4 py-2 text-sm font-medium text-white bg-red-600 rounded-md hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500 disabled:opacity-50"
            >
              {isDeleting ? "Deleting..." : "Delete"}
            </button>
          </div>
        </div>
      </header>

      <div className="prose prose-lg max-w-none">
        {post.content.split("\n").map((paragraph, index) => (
          <p key={index} className="mb-4 text-gray-700">
            {paragraph}
          </p>
        ))}
      </div>
    </article>
  );
}
