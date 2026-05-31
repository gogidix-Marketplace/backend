'use client';

import React from 'react';

interface HealthScoreCardProps {
  score: number;
}

export const HealthScoreCard: React.FC<HealthScoreCardProps> = ({ score }) => {
  const getScoreColor = (score: number) => {
    if (score >= 80) return { bg: 'bg-green-100', text: 'text-green-800', border: 'border-green-200', ring: 'ring-green-500' };
    if (score >= 60) return { bg: 'bg-yellow-100', text: 'text-yellow-800', border: 'border-yellow-200', ring: 'ring-yellow-500' };
    return { bg: 'bg-red-100', text: 'text-red-800', border: 'border-red-200', ring: 'ring-red-500' };
  };

  const colors = getScoreColor(score);

  return (
    <div className={`bg-white rounded-lg shadow p-6 border-2 ${colors.border}`}>
      <div className="flex items-center justify-between">
        <div>
          <h3 className="text-lg font-semibold text-gray-900">HR Health Score</h3>
          <p className="text-sm text-gray-600">
            {score >= 80 ? 'Excellent' : score >= 60 ? 'Good' : 'Needs attention'}
          </p>
        </div>
        <div className={`relative h-24 w-24`}>
          <svg className="transform -rotate-90 w-24 h-24">
            <circle
              cx="48"
              cy="48"
              r="40"
              stroke="currentColor"
              strokeWidth="8"
              fill="transparent"
              className="text-gray-200"
            />
            <circle
              cx="48"
              cy="48"
              r="40"
              stroke="currentColor"
              strokeWidth="8"
              fill="transparent"
              strokeDasharray={`${(score / 100) * 251.2} 251.2`}
              className={colors.text}
            />
          </svg>
          <div className="absolute inset-0 flex items-center justify-center">
            <span className={`text-2xl font-bold ${colors.text}`}>{score}</span>
          </div>
        </div>
      </div>

      <div className="mt-4 grid grid-cols-4 gap-4">
        <div className="text-center">
          <p className="text-xs text-gray-500">Recruitment</p>
          <p className="text-sm font-semibold text-gray-900">
            {score >= 80 ? 'A' : score >= 60 ? 'B' : 'C'}
          </p>
        </div>
        <div className="text-center">
          <p className="text-xs text-gray-500">Retention</p>
          <p className="text-sm font-semibold text-gray-900">
            {score >= 80 ? 'A' : score >= 60 ? 'B' : 'C'}
          </p>
        </div>
        <div className="text-center">
          <p className="text-xs text-gray-500">Compliance</p>
          <p className="text-sm font-semibold text-gray-900">
            {score >= 80 ? 'A' : score >= 60 ? 'B' : 'C'}
          </p>
        </div>
        <div className="text-center">
          <p className="text-xs text-gray-500">Performance</p>
          <p className="text-sm font-semibold text-gray-900">
            {score >= 80 ? 'A' : score >= 60 ? 'B' : 'C'}
          </p>
        </div>
      </div>
    </div>
  );
};

export default HealthScoreCard;
