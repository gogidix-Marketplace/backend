'use client';

import React from 'react';
import { GlobalRecruitmentStats } from '../../api/globalHrApi';

interface GlobalPipelineProps {
  recruitment?: GlobalRecruitmentStats;
}

export const GlobalPipeline: React.FC<GlobalPipelineProps> = ({ recruitment }) => {
  const pipeline = recruitment?.globalPipeline || [];

  const total = pipeline.reduce((sum, stage) => sum + stage.count, 0);

  const getStageColor = (stage: string) => {
    switch (stage) {
      case 'applied': return 'bg-blue-500';
      case 'screened': return 'bg-indigo-500';
      case 'interview': return 'bg-purple-500';
      case 'offer': return 'bg-pink-500';
      case 'hired': return 'bg-green-500';
      default: return 'bg-gray-500';
    }
  };

  return (
    <div className="bg-white rounded-lg shadow p-6">
      <div className="flex items-center justify-between mb-6">
        <h2 className="text-lg font-semibold text-gray-900">Global Recruitment Pipeline</h2>
        <a href="/recruitment" className="text-blue-600 hover:text-blue-700 text-sm font-medium">
          Details →
        </a>
      </div>

      {/* Pipeline Visualization */}
      <div className="space-y-4 mb-6">
        {pipeline.map((stage) => {
          const percentage = total > 0 ? (stage.count / total) * 100 : 0;
          return (
            <div key={stage.stage}>
              <div className="flex items-center justify-between mb-1">
                <span className="text-sm font-medium text-gray-700 capitalize">{stage.stage}</span>
                <span className="text-sm font-semibold text-gray-900">{stage.count}</span>
              </div>
              <div className="w-full bg-gray-200 rounded-full h-4">
                <div
                  className={`${getStageColor(stage.stage)} h-4 rounded-full transition-all duration-500`}
                  style={{ width: `${percentage}%` }}
                />
              </div>
            </div>
          );
        })}
      </div>

      {/* Metrics */}
      <div className="grid grid-cols-2 gap-4 pt-4 border-t border-gray-200">
        <div className="text-center">
          <p className="text-xs text-gray-500">Total Candidates</p>
          <p className="text-xl font-bold text-gray-900">{recruitment?.totalApplicants || 0}</p>
        </div>
        <div className="text-center">
          <p className="text-xs text-gray-500">Conversion Rate</p>
          <p className="text-xl font-bold text-green-600">
            {total > 0 ? ((pipeline.find(s => s.stage === 'hired')?.count || 0) / total * 100).toFixed(1) : 0}%
          </p>
        </div>
        <div className="text-center">
          <p className="text-xs text-gray-500">Time to Fill</p>
          <p className="text-xl font-bold text-gray-900">{recruitment?.averageTimeToFill || 0} days</p>
        </div>
        <div className="text-center">
          <p className="text-xs text-gray-500">Cost per Hire</p>
          <p className="text-xl font-bold text-gray-900">${recruitment?.costPerHire || 0}</p>
        </div>
      </div>
    </div>
  );
};

export default GlobalPipeline;
