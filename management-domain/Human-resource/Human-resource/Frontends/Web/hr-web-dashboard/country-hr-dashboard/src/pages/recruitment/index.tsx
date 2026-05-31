'use client';

import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { useRouter } from 'next/router';
import { hrApi } from '../../api/hrApi';
import { ProtectedRoute } from '../../components/common/ProtectedRoute';
import { DashboardLayout } from '../../components/layout/DashboardLayout';

interface Job {
  id: string;
  title: string;
  department: string;
  location: string;
  countryCode: string;
  type: 'full-time' | 'part-time' | 'contract';
  status: 'active' | 'closed' | 'on-hold';
  applicants: number;
  postedDate: string;
  hiringManager: string;
}

const RecruitmentPage: React.FC = () => {
  const router = useRouter();
  const [activeTab, setActiveTab] = useState<'jobs' | 'pipeline' | 'analytics'>('jobs');
  const [selectedStatus, setSelectedStatus] = useState<string>('all');

  const { data: jobs, isLoading: jobsLoading } = useQuery({
    queryKey: ['global-jobs'],
    queryFn: () => hrApi.getGlobalJobs(),
  });

  const { data: pipeline } = useQuery({
    queryKey: ['global-pipeline'],
    queryFn: () => hrApi.getGlobalPipeline(),
  });

  const filteredJobs = jobs?.filter((job: Job) => {
    if (selectedStatus === 'all') return true;
    return job.status === selectedStatus;
  });

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'active':
        return 'bg-green-100 text-green-800';
      case 'closed':
        return 'bg-gray-100 text-gray-800';
      case 'on-hold':
        return 'bg-yellow-100 text-yellow-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  const getTypeColor = (type: string) => {
    switch (type) {
      case 'full-time':
        return 'bg-blue-100 text-blue-800';
      case 'part-time':
        return 'bg-purple-100 text-purple-800';
      case 'contract':
        return 'bg-orange-100 text-orange-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  const getCountryFlag = (code: string) => {
    const flags: Record<string, string> = {
      US: '🇺🇸', GB: '🇬🇧', DE: '🇩🇪', FR: '🇫🇷', ES: '🇪🇸', IT: '🇮🇹',
      NL: '🇳🇱', PL: '🇵🇱', SE: '🇸🇪', NO: '🇳🇴', DK: '🇩🇰', FI: '🇫🇮',
    };
    return flags[code] || '🌍';
  };

  return (
    <ProtectedRoute>
      <DashboardLayout>
        <div className="space-y-6">
          {/* Header */}
          <div className="flex items-center justify-between">
            <div>
              <h1 className="text-2xl font-bold text-gray-900">Global Recruitment</h1>
              <p className="text-gray-600">Manage job postings and track candidates across all countries</p>
            </div>
            <button className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition">
              + Post New Job
            </button>
          </div>

          {/* Summary Metrics */}
          <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
            <div className="bg-white rounded-lg shadow p-4">
              <p className="text-sm text-gray-600">Active Jobs</p>
              <p className="text-2xl font-bold text-gray-900">
                {jobs?.filter((j: Job) => j.status === 'active').length || 0}
              </p>
            </div>
            <div className="bg-white rounded-lg shadow p-4">
              <p className="text-sm text-gray-600">Total Applicants</p>
              <p className="text-2xl font-bold text-gray-900">
                {jobs?.reduce((sum: number, j: Job) => sum + j.applicants, 0) || 0}
              </p>
            </div>
            <div className="bg-white rounded-lg shadow p-4">
              <p className="text-sm text-gray-600">In Pipeline</p>
              <p className="text-2xl font-bold text-blue-600">{pipeline?.interview || 0}</p>
            </div>
            <div className="bg-white rounded-lg shadow p-4">
              <p className="text-sm text-gray-600">Hired This Month</p>
              <p className="text-2xl font-bold text-green-600">{pipeline?.hired || 0}</p>
            </div>
          </div>

          {/* Tabs */}
          <div className="bg-white rounded-lg shadow">
            <div className="border-b border-gray-200">
              <nav className="flex -mb-px">
                <button
                  onClick={() => setActiveTab('jobs')}
                  className={`px-6 py-4 text-sm font-medium border-b-2 transition-colors ${
                    activeTab === 'jobs'
                      ? 'border-blue-500 text-blue-600'
                      : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
                  }`}
                >
                  Global Jobs
                </button>
                <button
                  onClick={() => setActiveTab('pipeline')}
                  className={`px-6 py-4 text-sm font-medium border-b-2 transition-colors ${
                    activeTab === 'pipeline'
                      ? 'border-blue-500 text-blue-600'
                      : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
                  }`}
                >
                  Pipeline Overview
                </button>
                <button
                  onClick={() => setActiveTab('analytics')}
                  className={`px-6 py-4 text-sm font-medium border-b-2 transition-colors ${
                    activeTab === 'analytics'
                      ? 'border-blue-500 text-blue-600'
                      : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
                  }`}
                >
                  Analytics
                </button>
              </nav>
            </div>

            <div className="p-6">
              {activeTab === 'jobs' && (
                <div>
                  {/* Filters */}
                  <div className="flex items-center gap-4 mb-6">
                    <select
                      value={selectedStatus}
                      onChange={(e) => setSelectedStatus(e.target.value)}
                      className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                    >
                      <option value="all">All Status</option>
                      <option value="active">Active</option>
                      <option value="closed">Closed</option>
                      <option value="on-hold">On Hold</option>
                    </select>
                  </div>

                  {/* Jobs List */}
                  <div className="space-y-4">
                    {filteredJobs?.map((job: Job) => (
                      <div
                        key={job.id}
                        className="border border-gray-200 rounded-lg p-4 hover:shadow-md transition cursor-pointer"
                      >
                        <div className="flex items-start justify-between">
                          <div className="flex-1">
                            <div className="flex items-center gap-3">
                              <h3 className="text-lg font-semibold text-gray-900">{job.title}</h3>
                              <span className={`px-2 py-1 text-xs font-medium rounded-full ${getStatusColor(job.status)}`}>
                                {job.status}
                              </span>
                              <span className={`px-2 py-1 text-xs font-medium rounded-full ${getTypeColor(job.type)}`}>
                                {job.type}
                              </span>
                            </div>
                            <div className="mt-2 flex items-center gap-4 text-sm text-gray-600">
                              <span className="flex items-center gap-1">
                                <span>{getCountryFlag(job.countryCode)}</span>
                                {job.location}
                              </span>
                              <span>{job.department}</span>
                              <span>Hiring Manager: {job.hiringManager}</span>
                            </div>
                          </div>
                          <div className="text-right">
                            <p className="text-sm text-gray-500">{job.applicants} applicants</p>
                            <p className="text-xs text-gray-400">
                              Posted {new Date(job.postedDate).toLocaleDateString()}
                            </p>
                          </div>
                        </div>
                      </div>
                    ))}
                  </div>
                </div>
              )}

              {activeTab === 'pipeline' && (
                <div className="space-y-6">
                  {/* Pipeline Stages */}
                  <div className="grid grid-cols-5 gap-4">
                    {[
                      { name: 'Applied', count: pipeline?.applied || 0, color: 'bg-blue-500' },
                      { name: 'Screening', count: pipeline?.screening || 0, color: 'bg-indigo-500' },
                      { name: 'Interview', count: pipeline?.interview || 0, color: 'bg-purple-500' },
                      { name: 'Offer', count: pipeline?.offer || 0, color: 'bg-pink-500' },
                      { name: 'Hired', count: pipeline?.hired || 0, color: 'bg-green-500' },
                    ].map((stage) => (
                      <div key={stage.name} className="bg-gray-50 rounded-lg p-4 text-center">
                        <div className={`h-2 w-full ${stage.color} rounded-full mb-2`}></div>
                        <p className="text-2xl font-bold text-gray-900">{stage.count}</p>
                        <p className="text-sm text-gray-600">{stage.name}</p>
                      </div>
                    ))}
                  </div>

                  {/* Time to Fill */}
                  <div className="bg-gray-50 rounded-lg p-6">
                    <h3 className="text-lg font-semibold text-gray-900 mb-2">Average Time to Fill</h3>
                    <p className="text-3xl font-bold text-blue-600">
                      {pipeline?.averageTimeToFill || 0} days
                    </p>
                    <p className="text-sm text-gray-600 mt-2">
                      {pipeline?.averageTimeToFill < 45 ? 'Below industry average' : 'Above industry average'}
                    </p>
                  </div>
                </div>
              )}

              {activeTab === 'analytics' && (
                <div>
                  <h3 className="text-lg font-semibold text-gray-900 mb-4">Recruitment Analytics</h3>
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <div className="bg-gray-50 rounded-lg p-6">
                      <h4 className="font-medium text-gray-700 mb-4">Hiring by Country</h4>
                      <div className="space-y-3">
                        {['United States', 'Germany', 'France', 'United Kingdom', 'Spain'].map((country) => (
                          <div key={country} className="flex items-center justify-between">
                            <span className="text-sm text-gray-600">{country}</span>
                            <div className="flex items-center gap-2">
                              <div className="w-32 bg-gray-200 rounded-full h-2">
                                <div className="bg-blue-600 h-2 rounded-full" style={{ width: `${Math.random() * 60 + 20}%` }}></div>
                              </div>
                              <span className="text-sm font-medium">{Math.floor(Math.random() * 50 + 10)}</span>
                            </div>
                          </div>
                        ))}
                      </div>
                    </div>

                    <div className="bg-gray-50 rounded-lg p-6">
                      <h4 className="font-medium text-gray-700 mb-4">Source of Hire</h4>
                      <div className="space-y-3">
                        {[
                          { source: 'LinkedIn', percentage: 35 },
                          { source: 'Company Website', percentage: 25 },
                          { source: 'Referrals', percentage: 20 },
                          { source: 'Recruitment Agency', percentage: 15 },
                          { source: 'Other', percentage: 5 },
                        ].map((item) => (
                          <div key={item.source} className="flex items-center justify-between">
                            <span className="text-sm text-gray-600">{item.source}</span>
                            <div className="flex items-center gap-2">
                              <div className="w-32 bg-gray-200 rounded-full h-2">
                                <div className="bg-green-600 h-2 rounded-full" style={{ width: `${item.percentage}%` }}></div>
                              </div>
                              <span className="text-sm font-medium">{item.percentage}%</span>
                            </div>
                          </div>
                        ))}
                      </div>
                    </div>
                  </div>
                </div>
              )}
            </div>
          </div>
        </div>
      </DashboardLayout>
    </ProtectedRoute>
  );
};

export default RecruitmentPage;
