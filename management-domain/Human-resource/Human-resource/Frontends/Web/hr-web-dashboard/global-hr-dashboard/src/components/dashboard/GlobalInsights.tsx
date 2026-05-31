'use client';

import React from 'react';
import { GlobalInsight } from '../../api/globalHrApi';

interface GlobalInsightsProps {
  insights: GlobalInsight[];
}

export const GlobalInsights: React.FC<GlobalInsightsProps> = ({ insights }) => {
  const getInsightStyles = (type: string, severity: string) => {
    const baseStyles = {
      opportunity: 'bg-blue-50 border-blue-200',
      risk: 'bg-red-50 border-red-200',
      achievement: 'bg-green-50 border-green-200',
      alert: 'bg-yellow-50 border-yellow-200',
    };
    return baseStyles[type as keyof typeof baseStyles] || baseStyles.alert;
  };

  const getIcon = (category: string) => {
    const icons: Record<string, string> = {
      workforce: '👥',
      recruitment: '💼',
      payroll: '💰',
      performance: '📊',
      compliance: '⚖️',
    };
    return icons[category] || '📌';
  };

  const priorityInsights = insights.slice(0, 4);

  return (
    <div className="bg-white rounded-lg shadow p-6">
      <div className="flex items-center justify-between mb-6">
        <h2 className="text-lg font-semibold text-gray-900">Global Insights</h2>
        <a href="/analytics" className="text-blue-600 hover:text-blue-700 text-sm font-medium">
          View all →
        </a>
      </div>

      <div className="space-y-3">
        {priorityInsights.map((insight) => (
          <div
            key={insight.id}
            className={`p-4 rounded-lg border ${getInsightStyles(insight.type, insight.severity)} hover:shadow-md transition cursor-pointer`}
          >
            <div className="flex items-start">
              <span className="text-2xl mr-3">{getIcon(insight.category)}</span>
              <div className="flex-1">
                <div className="flex items-center justify-between mb-1">
                  <h4 className="font-medium text-gray-900">{insight.title}</h4>
                  <span className={`px-2 py-0.5 text-xs font-medium rounded-full ${
                    insight.severity === 'high' ? 'bg-red-200 text-red-800' :
                    insight.severity === 'medium' ? 'bg-yellow-200 text-yellow-800' :
                    'bg-blue-200 text-blue-800'
                  }`}>
                    {insight.severity}
                  </span>
                </div>
                <p className="text-sm text-gray-700">{insight.description}</p>
                {insight.affectedCountries && insight.affectedCountries.length > 0 && (
                  <div className="mt-2 flex flex-wrap gap-1">
                    {insight.affectedCountries.slice(0, 3).map((code) => (
                      <span key={code} className="px-2 py-0.5 bg-white bg-opacity-60 rounded text-xs text-gray-600">
                        {code}
                      </span>
                    ))}
                    {insight.affectedCountries.length > 3 && (
                      <span className="px-2 py-0.5 bg-white bg-opacity-60 rounded text-xs text-gray-600">
                        +{insight.affectedCountries.length - 3} more
                      </span>
                    )}
                  </div>
                )}
                {insight.recommendation && (
                  <p className="mt-2 text-xs text-gray-600 italic">
                    💡 {insight.recommendation}
                  </p>
                )}
              </div>
            </div>
          </div>
        ))}
      </div>

      {insights.length === 0 && (
        <div className="text-center py-8 text-gray-500">
          <svg className="h-12 w-12 mx-auto mb-2 text-gray-300" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9.663 17h4.673M12 3v1m6.364 1.636a2 2 0 01-1.414 1.436L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
          </svg>
          <p>No insights at this time</p>
        </div>
      )}
    </div>
  );
};

export default GlobalInsights;
