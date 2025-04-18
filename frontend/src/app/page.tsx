import { boards } from "@/data/boards";
import Boards from "@/components/Boards";

export default function Home() {
  return (
    <main>
      <Boards boards={boards} />
    </main>
  );
}
