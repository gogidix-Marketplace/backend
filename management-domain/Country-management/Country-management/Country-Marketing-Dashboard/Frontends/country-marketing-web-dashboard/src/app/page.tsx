'use client';

import { useEffect, useState } from 'react';
import { BarChart3, Users, Target, TrendingUp, Calendar, DollarSign } from 'lucide-react';
import DashboardLayout from '@/components/DashboardLayout';
import MetricCard from '@/components/MetricCard';
import CampaignChart from '@/components/CampaignChart';
import LeadsTable from '@/components/LeadsTable';
import { dashboardApi } from '@/lib/api';

export default function DashboardPage() {
  const [loading, setLoading] = useState(true);
  const [quickStats, setQuickStats] = useState<any>(null);

  useEffect(() => {
    loadDashboardData();
  }, []);

  const loadDashboardData = async () => {
    try {
      const stats = await dashboardApi.getQuickStats();
      setQuickStats(stats);
    } catch (error) {
      console.error('Failed to load dashboard data:', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <DashboardLayout>
        <div className="flex items-center justify-center h-64">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600"></div>
        </div>
      </DashboardLayout>
    );
  }

  return (
    <DashboardLayout>
      <div className="space-y-6">
        {/* Page Header */}
        <div className="flex items-center justify-between">
          <div>
            <h1 className="text-2xl font-bold text-gray-900">Marketing Dashboard</h1>
            <p className="text-gray-600 mt-1">Overview of your country marketing operations</p>
          </div>
          <div className="flex items-center space-x-3">
            <button className="btn btn-secondary flex items-center gap-2">
              <Calendar className="w-4 h-4" />
              Last 30 Days
            </button>
            <button className="btn btn-primary">Export Report</button>
          </div>
        </div>

        {/* Quick Stats */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          <MetricCard
            title="Active Campaigns"
            value={quickStats?.activeCampaigns || 0}
            icon={<BarChart3 className="w-5 h-5" />}
            color="primary"
            change="+2 from last month"
          />
          <MetricCard
            title="New Leads"
            value={quickStats?.newLeads || 0}
            icon={<Users className="w-5 h-5" />}
            color="success"
            change="+15% from last month"
          />
          <MetricCard
            title="Spend This Period"
            value={`$${(quickStats?.spendThisPeriod || 0).toLocaleString()}`}
            icon={<DollarSign className="w-5 h-5" />}
            color="warning"
            change={`${quickStats?.budgetUtilization?.toFixed(1) || 0}% of budget used`}
          />
          <MetricCard
            title="Average ROI"
            value={`${quickStats?.averageROI || 0}%`}
            icon={<TrendingUp className="w-5 h-5" />}
            color="success"
            change="+5% from last period"
          />
        </div>

        {/* Charts Section */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          {/* Campaign Performance */}
          <div className="card">
            <div className="card-header">
              <h3 className="card-title">Campaign Performance</h3>
            </div>
            <CampaignChart />
          </div>

          {/* Lead Conversion Funnel */}
          <div className="card">
            <div className="card-header">
              <h3 className="card-title">Lead Conversion Funnel</h3>
            </div>
            <div className="space-y-4">
              <FunnelStage label="Impressions" value="125,000" percentage={100} color="bg-primary-500" />
              <FunnelStage label="Clicks" value="8,500" percentage={68} color="bg-primary-400" />
              <FunnelStage label="Leads" value="1,200" percentage={50} color="bg-primary-300" />
              <FunnelStage label="Qualified" value="350" percentage={28} color="bg-success-400" />
              <FunnelStage label="Converted" value="125" percentage={10} color="bg-success-500" />
            </div>
          </div>
        </div>

        {/* Recent Leads Table */}
        <div className="card">
          <div className="card-header">
            <h3 className="card-title">Recent Leads</h3>
            <a href="/leads" className="text-primary-600 hover:text-primary-700 text-sm font-medium">
              View all leads →
            </a>
          </div>
          <LeadsTable limit={5} />
        </div>

        {/* Budget Overview */}
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          <div className="card lg:col-span-2">
            <div className="card-header">
              <h3 className="card-title">Budget Utilization by Category</h3>
            </div>
            <div className="space-y-4">
              <BudgetBar category="Digital Advertising" spent={45000} budget={60000} />
              <BudgetBar category="Events & Webinars" spent={28000} budget={40000} />
              <BudgetBar category="Content Marketing" spent={15000} budget={25000} />
              <BudgetBar category="PR & Communications" spent={12000} budget={20000} />
              <BudgetBar category="Tools & Software" spent={8000} budget={15000} />
            </div>
          </div>

          <div className="card">
            <div className="card-header">
              <h3 className="card-title">Upcoming Events</h3>
            </div>
            <div className="space-y-4">
              <EventCard
                title="Product Launch Webinar"
                date="Feb 28, 2026"
                time="2:00 PM"
                attendees={156}
                status="Registration Open"
              />
              <EventCard
                title="Industry Conference"
                date="Mar 15, 2026"
                time="All Day"
                attendees={12}
                status="Planning"
              />
              <EventCard
                title="Customer Workshop"
                date="Mar 22, 2026"
                time="10:00 AM"
                attendees={45}
                status="Confirmed"
              />
            </div>
          </div>
        </div>
      </div>
    </DashboardLayout>
  );
}

function FunnelStage({ label, value, percentage, color }: { label: string; value: string; percentage: number; color: string }) {
  return (
    <div>
      <div className="flex items-center justify-between mb-1">
        <span className="text-sm font-medium text-gray-700">{label}</span>
        <span className="text-sm text-gray-500">{value}</span>
      </div>
      <div className="w-full bg-gray-200 rounded-full h-2">
        <div className={`${color} h-2 rounded-full`} style={{ width: `${percentage}%` }}></div>
      </div>
    </div>
  );
}

function BudgetBar({ category, spent, budget }: { category: string; spent: number; budget: number }) {
  const percentage = (spent / budget) * 100;
  const isOver = percentage > 100;

  return (
    <div>
      <div className="flex items-center justify-between mb-1">
        <span className="text-sm font-medium text-gray-700">{category}</span>
        <span className={`text-sm ${isOver ? 'text-danger-600' : 'text-gray-500'}`}>
          ${spent.toLocaleString()} / ${budget.toLocaleString()}
        </span>
      </div>
      <div className="w-full bg-gray-200 rounded-full h-2">
        <div
          className={`h-2 rounded-full ${isOver ? 'bg-danger-500' : percentage > 80 ? 'bg-warning-500' : 'bg-success-500'}`}
          style={{ width: `${Math.min(percentage, 100)}%` }}
        ></div>
      </div>
    </div>
  );
}

function EventCard({ title, date, time, attendees, status }: { title: string; date: string; time: string; attendees: number; status: string }) {
  return (
    <div className="flex items-start space-x-3 p-3 bg-gray-50 rounded-lg">
      <div className="flex-shrink-0">
        <Calendar className="w-5 h-5 text-primary-600" />
      </div>
      <div className="flex-1 min-w-0">
        <p className="text-sm font-medium text-gray-900 truncate">{title}</p>
        <p className="text-xs text-gray-500">{date} at {time}</p>
        <div className="flex items-center mt-1 space-x-2">
          <span className="badge badge-info">{status}</span>
          <span className="text-xs text-gray-500">{attendees} attending</span>
        </div>
      </div>
    </div>
  );
}
