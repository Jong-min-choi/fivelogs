import Layout from "@/components/layout/Layout";
import { boards, categories } from "@/data/boards";
import Boards from "@/components/Boards";

export default function Home() {
  return (
    <Layout>
      <main>
        <Boards boards={boards} categories={categories} />
      </main>
    </Layout>
  );
}
