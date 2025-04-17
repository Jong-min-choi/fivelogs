import Boards from "@/components/Boards";
import { boards, categories } from "@/data/boards";

export default function Home() {
  return <Boards boards={boards} categories={categories} />;
}
