'use client';

import React from 'react';
import { RegionalSummary } from '../../api/globalHrApi';

interface RegionalSummaryCardProps {
  regions: RegionalSummary[];
}

export const RegionalSummaryCard: React.FC<RegionalSummaryCardProps> = ({ regions }) => {
  const getRegionColor = (regionCode: string) => {
    const colors: Record<string, string> = {
      africa: 'bg-green-100 text-green-800 border-green-200',
      europe: 'bg-blue-100 text-blue-800 border-blue-200',
      asia: 'bg-purple-100 text-purple-800 border-purple-200',
      americas: 'bg-orange-100 text-orange-800 border-orange-200',
      middleeast: 'bg-yellow-100 text-yellow-800 border-yellow-200',
    };
    return colors[regionCode.toLowerCase()] || 'bg-gray-100 text-gray-800 border-gray-200';
  };

  const getRegionFlag = (regionCode: string) => {
    const flags: Record<string, string> = {
      africa: '🌍',
      europe: '🇪🇺',
      asia: '🌏',
      americas: '🌎',
      middleeast: '🕌',
    };
    return flags[regionCode.toLowerCase()] || '🌐';
  };

  return (
    <div className="bg-white rounded-lg shadow p-6">
      <div className="flex items-center justify-between mb-4">
        <h2 className="text-lg font-semibold text-gray-900">Regional Summary</h2>
        <a href="/regions" className="text-blue-600 hover:text-blue-700 text-sm font-medium">
          View all →
        </a>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        {regions.map((region) => (
          <div
            key={region.regionCode}
            className={`border rounded-lg p-4 ${getRegionColor(region.regionCode)}`}
          >
            <div className="flex items-center justify-between mb-3">
              <div className="flex items-center gap-2">
                <span className="text-2xl">{getRegionFlag(region.regionCode)}</span>
                <div>
                  <h3 className="font-semibold text-gray-900">{region.regionName}</h3>
                  <p className="text-xs text-gray-600">{region.countries.length} countries</p>
                </div>
              </div>
              <div className="text-right">
                <p className="text-2xl font-bold text-gray-900">{region.healthScore}</p>
                <p className="text-xs text-gray-600">Health Score</p>
              </div>
            </div>

            <div className="grid grid-cols-2 gap-2 mb-3">
              <div>
                <p className="text-xs text-gray-600">Headcount</p>
                <p className="text-sm font-semibold text-gray-900">{region.headcount.toLocaleString()}</p>
              </div>
              <div>
                <p className="text-xs text-gray-600">Payroll (USD)</p>
                <p className="text-sm font-semibold text-gray-900">
                  ${(region.payroll / 1000000).toFixed(1)}M
                </p>
              </div>
            </div>

            {region.topPerformers.length > 0 && (
              <div className="flex flex-wrap gap-1">
                {region.topPerformers.map((country) => (
                  <span key={country} className="px-2 py-0.5 bg-white bg-opacity-60 rounded text-xs text-green-700">
                    ⭐ {country}
                  </span>
                ))}
              </div>
            )}

            {region.concerns.length > 0 && (
              <div className="mt-2 flex flex-wrap gap-1">
                {region.concerns.map((country) => (
                  <span key={country} className="px-2 py-0.5 bg-white bg-opacity-60 rounded text-xs text-red-700">
                    ⚠️ {country}
                  </span>
                ))}
              </div>
            )}
          </div>
        ))}
      </div>
    </div>
  );
};

export default RegionalSummaryCard;
