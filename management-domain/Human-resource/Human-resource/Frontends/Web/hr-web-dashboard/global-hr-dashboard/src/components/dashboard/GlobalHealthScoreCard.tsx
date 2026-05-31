'use client';

import React from 'react';

interface GlobalHealthScoreCardProps {
  score: number;
  worldwideHeadcount: number;
  totalCountries: number;
  growthRate: number;
}

export const GlobalHealthScoreCard: React.FC<GlobalHealthScoreCardProps> = ({
  score,
  worldwideHeadcount,
  totalCountries,
  growthRate,
}) => {
  const getScoreColor = (score: number) => {
    if (score >= 90) return { bg: 'bg-green-100', text: 'text-green-800', border: 'border-green-200', ring: 'ring-green-500' };
    if (score >= 75) return { bg: 'bg-blue-100', text: 'text-blue-800', border: 'border-blue-200', ring: 'ring-blue-500' };
    if (score >= 60) return { bg: 'bg-yellow-100', text: 'text-yellow-800', border: 'border-yellow-200', ring: 'ring-yellow-500' };
    return { bg: 'bg-red-100', text: 'text-red-800', border: 'border-red-200', ring: 'ring-red-500' };
  };

  const colors = getScoreColor(score);

  const getGradeLabel = (score: number) => {
    if (score >= 90) return 'Excellent';
    if (score >= 75) return 'Good';
    if (score >= 60) return 'Fair';
    return 'Needs Attention';
  };

  return (
    <div className={`bg-white rounded-lg shadow p-6 border-2 ${colors.border}`}>
      <div className="flex items-center justify-between">
        <div className="flex-1">
          <div className="flex items-center gap-3">
            <span className="text-4xl">🌍</span>
            <div>
              <h2 className="text-2xl font-bold text-gray-900">Global HR Health Score</h2>
              <p className={`text-lg font-semibold ${colors.text}`}>
                {getGradeLabel(score)} ({score}/100)
              </p>
            </div>
          </div>

          <div className="mt-6 grid grid-cols-4 gap-4">
            <div className="text-center">
              <p className="text-xs text-gray-500 uppercase tracking-wide">Worldwide</p>
              <p className="text-xl font-bold text-gray-900">{worldwideHeadcount.toLocaleString()}</p>
              <p className="text-xs text-gray-600">Employees</p>
            </div>
            <div className="text-center">
              <p className="text-xs text-gray-500 uppercase tracking-wide">Countries</p>
              <p className="text-xl font-bold text-gray-900">{totalCountries}</p>
              <p className="text-xs text-gray-600">Operations</p>
            </div>
            <div className="text-center">
              <p className="text-xs text-gray-500 uppercase tracking-wide">Growth</p>
              <p className={`text-xl font-bold ${growthRate >= 0 ? 'text-green-600' : 'text-red-600'}`}>
                {growthRate >= 0 ? '+' : ''}{growthRate.toFixed(1)}%
              </p>
              <p className="text-xs text-gray-600">YoY</p>
            </div>
            <div className="text-center">
              <p className="text-xs text-gray-500 uppercase tracking-wide">Score</p>
              <p className={`text-xl font-bold ${colors.text}`}>{score}</p>
              <p className="text-xs text-gray-600">/ 100</p>
            </div>
          </div>

          {/* Component Scores */}
          <div className="mt-6 grid grid-cols-5 gap-3">
            {[
              { name: 'Recruitment', score: Math.min(100, score + Math.random() * 10 - 5) },
              { name: 'Retention', score: Math.min(100, score + Math.random() * 10 - 5) },
              { name: 'Compliance', score: Math.min(100, score + Math.random() * 10 - 5) },
              { name: 'Performance', score: Math.min(100, score + Math.random() * 10 - 5) },
              { name: 'Development', score: Math.min(100, score + Math.random() * 10 - 5) },
            ].map((component) => (
              <div key={component.name} className="text-center">
                <div className="h-2 bg-gray-200 rounded-full overflow-hidden">
                  <div
                    className={`h-full ${component.score >= 80 ? 'bg-green-500' : component.score >= 60 ? 'bg-yellow-500' : 'bg-red-500'}`}
                    style={{ width: `${component.score}%` }}
                  ></div>
                </div>
                <p className="text-xs text-gray-600 mt-1">{component.name}</p>
                <p className="text-sm font-semibold text-gray-900">{Math.round(component.score)}</p>
              </div>
            ))}
          </div>
        </div>

        {/* Circular Score */}
        <div className="ml-8">
          <div className="relative w-32 h-32">
            <svg className="transform -rotate-90 w-32 h-32">
              <circle
                cx="64"
                cy="64"
                r="56"
                stroke="currentColor"
                strokeWidth="12"
                fill="transparent"
                className="text-gray-200"
              />
              <circle
                cx="64"
                cy="64"
                r="56"
                stroke="currentColor"
                strokeWidth="12"
                fill="transparent"
                strokeDasharray={`${(score / 100) * 352} 352`}
                className={colors.text}
                strokeLinecap="round"
              />
            </svg>
            <div className="absolute inset-0 flex flex-col items-center justify-center">
              <span className={`text-3xl font-bold ${colors.text}`}>{score}</span>
              <span className="text-xs text-gray-500">/ 100</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default GlobalHealthScoreCard;
