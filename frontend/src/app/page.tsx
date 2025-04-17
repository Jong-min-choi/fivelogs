import Layout from "@/components/layout/Layout";
import { boards, categories } from "@/data/boards";
import Boards from "@/components/Boards";

export default function Home() {
<<<<<<< HEAD
  const posts = [
    {
      id: 1,
      title: "Getting Started with Next.js",
      excerpt: "Learn how to build modern web applications with Next.js",
      date: "2024-04-16",
      author: "John Doe",
      category: "Development",
    },
    {
      id: 2,
      title: "The Future of Web Development",
      excerpt:
        "Exploring the latest trends and technologies in web development",
      date: "2024-04-15",
      author: "Jane Smith",
      category: "Technology",
    },
    // Add more posts as needed
  ];

  return (
    <div className="space-y-8">
      <div className="text-center py-12">
        <h1 className="text-4xl font-bold text-gray-900 mb-4">
          Welcome to FiveLogs
        </h1>
        <p className="text-xl text-gray-600">
          A modern blog platform for sharing ideas and knowledge
        </p>
      </div>

      <div className="grid gap-8 md:grid-cols-2 lg:grid-cols-3">
        {posts.map((post) => (
          <article
            key={post.id}
            className="bg-white rounded-lg shadow-md overflow-hidden"
          >
            <div className="p-6">
              <div className="flex items-center text-sm text-gray-500 mb-2">
                <span>{post.date}</span>
                <span className="mx-2">•</span>
                <span>{post.author}</span>
              </div>
              <h2 className="text-xl font-semibold text-gray-900 mb-2">
                <a href={`/blog/${post.id}`} className="hover:text-blue-600">
                  {post.title}
                </a>
              </h2>
              <p className="text-gray-600 mb-4">{post.excerpt}</p>
              <div className="flex items-center justify-between">
                <span className="inline-flex items-center px-3 py-1 rounded-full text-sm font-medium bg-blue-100 text-blue-800">
                  {post.category}
                </span>
                <a
                  href={`/blog/${post.id}`}
                  className="text-blue-600 hover:text-blue-800 font-medium"
                >
                  Read more →
                </a>
              </div>
            </div>
          </article>
        ))}
      </div>
    </div>
=======
  return (
    <Layout>
      <main>
        <Boards boards={boards} categories={categories} />
      </main>
    </Layout>
>>>>>>> 16a8d542087a276c301e689bceb2f3589b648306
  );
}
