'use client';

import React from 'react';

interface PipelineStage {
  name: string;
  count: number;
  color: string;
}

interface RecruitmentPipelineProps {
  pipeline?: {
    applied?: number;
    screening?: number;
    interview?: number;
    offer?: number;
    hired?: number;
  };
}

export const RecruitmentPipeline: React.FC<RecruitmentPipelineProps> = ({ pipeline }) => {
  const stages: PipelineStage[] = [
    { name: 'Applied', count: pipeline?.applied || 0, color: 'bg-blue-500' },
    { name: 'Screening', count: pipeline?.screening || 0, color: 'bg-indigo-500' },
    { name: 'Interview', count: pipeline?.interview || 0, color: 'bg-purple-500' },
    { name: 'Offer', count: pipeline?.offer || 0, color: 'bg-pink-500' },
    { name: 'Hired', count: pipeline?.hired || 0, color: 'bg-green-500' },
  ];

  const total = stages.reduce((sum, stage) => sum + stage.count, 0);

  return (
    <div className="bg-white rounded-lg shadow p-6">
      <div className="flex items-center justify-between mb-6">
        <h2 className="text-lg font-semibold text-gray-900">Global Recruitment Pipeline</h2>
        <a href="/recruitment" className="text-blue-600 hover:text-blue-700 text-sm font-medium">
          Details →
        </a>
      </div>

      <div className="space-y-4">
        {stages.map((stage) => {
          const percentage = total > 0 ? (stage.count / total) * 100 : 0;
          return (
            <div key={stage.name}>
              <div className="flex items-center justify-between mb-1">
                <span className="text-sm font-medium text-gray-700">{stage.name}</span>
                <span className="text-sm font-semibold text-gray-900">{stage.count}</span>
              </div>
              <div className="w-full bg-gray-200 rounded-full h-3">
                <div
                  className={`${stage.color} h-3 rounded-full transition-all duration-500`}
                  style={{ width: `${percentage}%` }}
                />
              </div>
            </div>
          );
        })}
      </div>

      <div className="mt-6 pt-6 border-t border-gray-200">
        <div className="grid grid-cols-2 gap-4">
          <div className="text-center">
            <p className="text-xs text-gray-500">Total Candidates</p>
            <p className="text-xl font-bold text-gray-900">{total}</p>
          </div>
          <div className="text-center">
            <p className="text-xs text-gray-500">Conversion Rate</p>
            <p className="text-xl font-bold text-green-600">
              {total > 0 ? ((pipeline?.hired || 0) / total * 100).toFixed(1) : 0}%
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default RecruitmentPipeline;
