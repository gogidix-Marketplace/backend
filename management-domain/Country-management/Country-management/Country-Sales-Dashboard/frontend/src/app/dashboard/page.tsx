"use client";

import { useEffect } from "react";
import { useQuery } from "@tanstack/react-query";
import DashboardLayout from "@/components/layouts/DashboardLayout";
import MetricCard from "@/components/dashboard/MetricCard";
import PipelineChart from "@/components/charts/PipelineChart";
import RevenueChart from "@/components/charts/RevenueChart";
import DealsTable from "@/components/tables/DealsTable";
import { salesApi } from "@/services/api";
import { formatCurrency, formatNumber } from "@/lib/utils";

export default function DashboardPage() {
  const countryCode = process.env.NEXT_PUBLIC_COUNTRY_CODE || "US";

  const { data: summaryData, isLoading: summaryLoading } = useQuery({
    queryKey: ["dashboard-summary", countryCode],
    queryFn: () => salesApi.getDashboardSummary(countryCode),
    refetchInterval: 60000, // Refresh every minute
  });

  const { data: metricsData, isLoading: metricsLoading } = useQuery({
    queryKey: ["sales-metrics", countryCode],
    queryFn: () => salesApi.getSalesMetrics(countryCode),
    refetchInterval: 300000, // Refresh every 5 minutes
  });

  const { data: pipelineData } = useQuery({
    queryKey: ["pipeline-summary", countryCode],
    queryFn: () => salesApi.getPipelineSummary(countryCode),
    refetchInterval: 60000,
  });

  const { data: overdueDeals } = useQuery({
    queryKey: ["overdue-deals", countryCode],
    queryFn: () => salesApi.getOverdueDeals(countryCode),
    refetchInterval: 120000,
  });

  if (summaryLoading || metricsLoading) {
    return (
      <DashboardLayout>
        <div className="flex items-center justify-center h-screen">
          <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-primary"></div>
        </div>
      </DashboardLayout>
    );
  }

  const summary = summaryData?.data || {};
  const metrics = metricsData?.data;

  return (
    <DashboardLayout>
      <div className="space-y-6">
        {/* Header */}
        <div className="flex items-center justify-between">
          <div>
            <h1 className="text-3xl font-bold tracking-tight">Sales Dashboard</h1>
            <p className="text-muted-foreground">
              Country: {countryName(countryCode)} | {summary?.asOfDate && new Date(summary.asOfDate).toLocaleDateString()}
            </p>
          </div>
        </div>

        {/* KPI Cards */}
        <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
          <MetricCard
            title="Total Revenue (MTD)"
            value={formatCurrency(summary?.revenueThisMonth || 0)}
            trend="+12.5%"
            trendUp={true}
            icon="dollar-sign"
          />
          <MetricCard
            title="Open Pipeline"
            value={formatCurrency(pipelineData?.data?.totalPipelineValue || 0)}
            description={`${formatNumber(pipelineData?.data?.totalOpenDeals || 0)} deals`}
            icon="trending-up"
          />
          <MetricCard
            title="Total Customers"
            value={formatNumber(summary?.totalCustomers || 0)}
            description="Active accounts"
            icon="users"
          />
          <MetricCard
            title="Overdue Deals"
            value={formatNumber(summary?.overdueDeals || 0)}
            trend={summary?.overdueDeals > 0 ? "Needs attention" : "All on track"}
            trendUp={summary?.overdueDeals === 0}
            icon="alert-circle"
            variant={summary?.overdueDeals > 0 ? "warning" : "default"}
          />
        </div>

        {/* Charts Row */}
        <div className="grid gap-4 md:grid-cols-2">
          <PipelineChart />
          <RevenueChart />
        </div>

        {/* Target Progress */}
        {metrics?.target && (
          <div className="rounded-xl border bg-card p-6">
            <h3 className="text-lg font-semibold mb-4">Target Progress</h3>
            <div className="space-y-4">
              <div>
                <div className="flex justify-between text-sm mb-1">
                  <span>Monthly Target</span>
                  <span>
                    {formatCurrency(metrics.target.monthlyAchieved || 0)} / {formatCurrency(metrics.target.monthlyTarget || 0)}
                  </span>
                </div>
                <div className="w-full bg-secondary rounded-full h-2">
                  <div
                    className="bg-primary h-2 rounded-full transition-all"
                    style={{ width: `${Math.min(metrics.target.monthlyProgress || 0, 100)}%` }}
                  />
                </div>
                <p className="text-xs text-muted-foreground mt-1">
                  {metrics.target.monthlyProgress?.toFixed(1)}% achieved • {metrics.target.daysRemaining} days remaining
                </p>
              </div>
            </div>
          </div>
        )}

        {/* Recent Deals Table */}
        <div className="rounded-xl border bg-card">
          <div className="p-6">
            <h3 className="text-lg font-semibold mb-4">Overdue Deals</h3>
            {overdueDeals?.data && overdueDeals.data.length > 0 ? (
              <DealsTable deals={overdueDeals.data} />
            ) : (
              <p className="text-muted-foreground text-center py-8">No overdue deals</p>
            )}
          </div>
        </div>
      </div>
    </DashboardLayout>
  );
}

function countryName(code: string): string {
  const names: Record<string, string> = {
    US: "United States",
    UK: "United Kingdom",
    DE: "Germany",
    FR: "France",
    JP: "Japan",
    AU: "Australia",
  };
  return names[code] || code;
}
