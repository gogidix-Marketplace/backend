'use client';

import React from 'react';
import { CountryComparison } from '../../api/globalHrApi';

interface CountryComparisonCardProps {
  country: CountryComparison;
  onClick?: () => void;
}

export const CountryComparisonCard: React.FC<CountryComparisonCardProps> = ({ country, onClick }) => {
  const getHealthScoreColor = (score: number) => {
    if (score >= 90) return 'text-green-600 bg-green-50';
    if (score >= 75) return 'text-blue-600 bg-blue-50';
    if (score >= 60) return 'text-yellow-600 bg-yellow-50';
    return 'text-red-600 bg-red-50';
  };

  const getComplianceColor = (status: string) => {
    switch (status) {
      case 'compliant': return 'bg-green-100 text-green-800';
      case 'warning': return 'bg-yellow-100 text-yellow-800';
      case 'critical': return 'bg-red-100 text-red-800';
      default: return 'bg-gray-100 text-gray-800';
    }
  };

  const getTrendIcon = (trend: number[]) => {
    if (trend.length < 2) return null;
    const diff = trend[trend.length - 1] - trend[0];
    if (diff > 2) return <span className="text-green-600">↑</span>;
    if (diff < -2) return <span className="text-red-600">↓</span>;
    return <span className="text-gray-600">→</span>;
  };

  const flags: Record<string, string> = {
    NG: '🇳🇬', KE: '🇰🇪', ZA: '🇿🇦', GH: '🇬🇭', EG: '🇪🇬',
    GB: '🇬🇧', IE: '🇮🇪', DE: '🇩🇪', FR: '🇫🇷', NL: '🇳🇱', SE: '🇸🇪', NO: '🇳🇴', DK: '🇩🇰',
    US: '🇺🇸', CA: '🇨🇦', BR: '🇧🇷', MX: '🇲🇽', AR: '🇦🇷',
    JP: '🇯🇵', SG: '🇸🇬', IN: '🇮🇳', MY: '🇲🇾', TH: '🇹🇭', PH: '🇵🇭', VN: '🇻🇳', HK: '🇭🇰',
  };

  return (
    <div
      onClick={onClick}
      className="bg-gradient-to-br from-gray-50 to-gray-100 rounded-lg p-4 hover:shadow-md cursor-pointer transition-all border border-gray-200"
    >
      <div className="flex items-start justify-between mb-3">
        <div className="flex items-center gap-2">
          <span className="text-2xl">{flags[country.countryCode] || '🌍'}</span>
          <div>
            <h3 className="font-semibold text-gray-900">{country.countryName}</h3>
            <p className="text-xs text-gray-500">{country.region}</p>
          </div>
        </div>
        <div className="flex items-center gap-1">
          {getTrendIcon(country.trends.headcount)}
          <span className={`px-2 py-1 text-xs font-medium rounded-full ${getComplianceColor(country.complianceStatus)}`}>
            {country.complianceStatus}
          </span>
        </div>
      </div>

      <div className="grid grid-cols-2 gap-2 mb-3">
        <div>
          <p className="text-xs text-gray-500">Headcount</p>
          <p className="text-lg font-bold text-gray-900">{country.headcount.toLocaleString()}</p>
        </div>
        <div>
          <p className="text-xs text-gray-500">Growth</p>
          <p className={`text-sm font-semibold ${country.growth >= 0 ? 'text-green-600' : 'text-red-600'}`}>
            {country.growth >= 0 ? '+' : ''}{country.growth.toFixed(1)}%
          </p>
        </div>
      </div>

      <div className="grid grid-cols-2 gap-2">
        <div>
          <p className="text-xs text-gray-500">Health Score</p>
          <p className={`text-lg font-bold ${getHealthScoreColor(country.healthScore).split(' ')[0]}`}>
            {country.healthScore}/100
          </p>
        </div>
        <div>
          <p className="text-xs text-gray-500">Payroll</p>
          <p className="text-sm font-semibold text-gray-900">
            ${country.payroll.toLocaleString()}
          </p>
        </div>
      </div>
    </div>
  );
};

export default CountryComparisonCard;
