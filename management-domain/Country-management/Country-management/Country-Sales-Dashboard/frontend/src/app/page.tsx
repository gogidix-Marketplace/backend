import { redirect } from "next/navigation";
import DashboardPage from "./dashboard/page";

export default function HomePage() {
  // Redirect to dashboard
  redirect("/dashboard");
}
