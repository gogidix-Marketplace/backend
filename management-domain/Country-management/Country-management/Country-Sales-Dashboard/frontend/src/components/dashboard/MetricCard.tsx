import { cn } from "@/lib/utils";
import { LucideIcon } from "lucide-react";
import {
  DollarSign,
  TrendingUp,
  Users,
  AlertCircle,
  Target,
  Activity,
  Zap,
} from "lucide-react";

interface MetricCardProps {
  title: string;
  value: string | number;
  description?: string;
  trend?: string;
  trendUp?: boolean;
  icon?: string;
  variant?: "default" | "warning" | "success" | "danger";
}

const iconMap: Record<string, LucideIcon> = {
  "dollar-sign": DollarSign,
  "trending-up": TrendingUp,
  users: Users,
  "alert-circle": AlertCircle,
  target: Target,
  activity: Activity,
  zap: Zap,
};

export default function MetricCard({
  title,
  value,
  description,
  trend,
  trendUp,
  icon = "activity",
  variant = "default",
}: MetricCardProps) {
  const Icon = iconMap[icon] || Activity;

  const variantStyles = {
    default: "bg-card border-border",
    warning: "bg-orange-50 border-orange-200 dark:bg-orange-950 dark:border-orange-900",
    success: "bg-green-50 border-green-200 dark:bg-green-950 dark:border-green-900",
    danger: "bg-red-50 border-red-200 dark:bg-red-950 dark:border-red-900",
  };

  const trendColor = trendUp === false ? "text-red-600" : "text-green-600";

  return (
    <div className={cn("rounded-xl border p-6", variantStyles[variant])}>
      <div className="flex items-center justify-between">
        <div className="flex-1">
          <p className="text-sm font-medium text-muted-foreground">{title}</p>
          <p className="text-2xl font-bold mt-1">{value}</p>
          {description && (
            <p className="text-sm text-muted-foreground mt-1">{description}</p>
          )}
          {trend && (
            <p className={cn("text-sm font-medium mt-1", trendColor)}>{trend}</p>
          )}
        </div>
        <div className="ml-4">
          <div className="w-12 h-12 rounded-lg bg-primary/10 flex items-center justify-center">
            <Icon className="w-6 h-6 text-primary" />
          </div>
        </div>
      </div>
    </div>
  );
}
