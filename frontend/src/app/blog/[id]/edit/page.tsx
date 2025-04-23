"use client";

import { useRouter } from "next/navigation";
import { useState, useEffect } from "react";

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

export default function EditPost({ params }: { params: { id: string } }) {
  const router = useRouter();
  const [isSaving, setIsSaving] = useState(false);
  const [post, setPost] = useState<BlogPost | null>(null);
  const [formData, setFormData] = useState({
    title: "",
    content: "",
    category: "",
  });

  useEffect(() => {
    const postData = getBlogPost(params.id);
    if (postData) {
      setPost(postData);
      setFormData({
        title: postData.title,
        content: postData.content,
        category: postData.category,
      });
    }
  }, [params.id]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsSaving(true);

    try {
      // TODO: Replace with actual API call
      // await fetch(`/api/posts/${params.id}`, {
      //   method: 'PUT',
      //   headers: {
      //     'Content-Type': 'application/json',
      //   },
      //   body: JSON.stringify(formData),
      // });
      router.push(`/blog/${params.id}`);
    } catch (error) {
      console.error("Failed to update post:", error);
      alert("Failed to update post. Please try again.");
    } finally {
      setIsSaving(false);
    }
  };

  const handleChange = (
    e: React.ChangeEvent<
      HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement
    >
  ) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
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
    <div className="max-w-3xl mx-auto">
      <h1 className="text-3xl font-bold text-gray-900 mb-8">Edit Post</h1>

      <form onSubmit={handleSubmit} className="space-y-6">
        <div>
          <label
            htmlFor="title"
            className="block text-sm font-medium text-gray-700"
          >
            Title
          </label>
          <input
            type="text"
            id="title"
            name="title"
            value={formData.title}
            onChange={handleChange}
            className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
            required
          />
        </div>

        <div>
          <label
            htmlFor="category"
            className="block text-sm font-medium text-gray-700"
          >
            Category
          </label>
          <select
            id="category"
            name="category"
            value={formData.category}
            onChange={handleChange}
            className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
            required
          >
            <option value="Development">Development</option>
            <option value="Technology">Technology</option>
            <option value="Design">Design</option>
            <option value="Business">Business</option>
          </select>
        </div>

        <div>
          <label
            htmlFor="content"
            className="block text-sm font-medium text-gray-700"
          >
            Content
          </label>
          <textarea
            id="content"
            name="content"
            value={formData.content}
            onChange={handleChange}
            rows={15}
            className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
            required
          />
        </div>

        <div className="flex justify-end space-x-3">
          <button
            type="button"
            onClick={() => router.back()}
            className="px-4 py-2 text-sm font-medium text-gray-700 bg-white border border-gray-300 rounded-md hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
          >
            Cancel
          </button>
          <button
            type="submit"
            disabled={isSaving}
            className="px-4 py-2 text-sm font-medium text-white bg-blue-600 rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 disabled:opacity-50"
          >
            {isSaving ? "Saving..." : "Save Changes"}
          </button>
        </div>
      </form>
    </div>
  );
}
