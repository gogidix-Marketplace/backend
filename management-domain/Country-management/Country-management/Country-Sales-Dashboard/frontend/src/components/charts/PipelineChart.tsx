"use client";

import { useQuery } from "@tanstack/react-query";
import { salesApi } from "@/services/api";
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, Cell } from "recharts";
import { formatCurrency } from "@/lib/utils";

const STAGE_COLORS: Record<string, string> = {
  PROSPECTING: "#94a3b8",
  QUALIFICATION: "#3b82f6",
  "NEED ANALYSIS": "#06b6d4",
  "VALUE PROPOSITION": "#14b8a6",
  PROPOSAL: "#f59e0b",
  NEGOTIATION: "#f97316",
  "CLOSED WON": "#22c55e",
  "CLOSED LOST": "#ef4444",
};

export default function PipelineChart() {
  const countryCode = process.env.NEXT_PUBLIC_COUNTRY_CODE || "US";

  const { data: metricsData } = useQuery({
    queryKey: ["sales-metrics", countryCode],
    queryFn: () => salesApi.getSalesMetrics(countryCode),
  });

  const dealsByStage = metricsData?.data?.pipeline?.dealsByStage || {};

  const chartData = Object.entries(dealsByStage).map(([stage, count]) => ({
    name: stage.replace(/_/g, " "),
    value: count as number,
    stage,
  }));

  return (
    <div className="rounded-xl border bg-card p-6">
      <h3 className="text-lg font-semibold mb-4">Pipeline by Stage</h3>
      <ResponsiveContainer width="100%" height={250}>
        <BarChart data={chartData}>
          <CartesianGrid strokeDasharray="3 3" className="stroke-muted" />
          <XAxis
            dataKey="name"
            tick={{ fill: "hsl(var(--muted-foreground))", fontSize: 12 }}
            axisLine={{ stroke: "hsl(var(--border))" }}
          />
          <YAxis
            tick={{ fill: "hsl(var(--muted-foreground))", fontSize: 12 }}
            axisLine={{ stroke: "hsl(var(--border))" }}
          />
          <Tooltip
            contentStyle={{
              backgroundColor: "hsl(var(--card))",
              border: "1px solid hsl(var(--border))",
              borderRadius: "8px",
            }}
            formatter={(value: number) => [value, "Deals"]}
          />
          <Bar dataKey="value" radius={[4, 4, 0, 0]}>
            {chartData.map((entry) => (
              <Cell key={entry.name} fill={STAGE_COLORS[entry.stage] || "#3b82f6"} />
            ))}
          </Bar>
        </BarChart>
      </ResponsiveContainer>
    </div>
  );
}
