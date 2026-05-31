'use client';

import React from 'react';
import { Country } from '../../api/hrApi';

interface CountryCardProps {
  country: Country;
  onClick?: () => void;
}

export const CountryCard: React.FC<CountryCardProps> = ({ country, onClick }) => {
  const getHealthScoreColor = (score: number) => {
    if (score >= 80) return 'text-green-600';
    if (score >= 60) return 'text-yellow-600';
    return 'text-red-600';
  };

  const getStatusColor = (status: string) => {
    switch (status.toLowerCase()) {
      case 'active':
        return 'bg-green-100 text-green-800';
      case 'warning':
        return 'bg-yellow-100 text-yellow-800';
      case 'critical':
        return 'bg-red-100 text-red-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  return (
    <div
      onClick={onClick}
      className="bg-gray-50 rounded-lg p-4 hover:bg-gray-100 cursor-pointer transition-colors border border-gray-200"
    >
      <div className="flex items-start justify-between">
        <div>
          <div className="flex items-center">
            <span className="text-lg">{getCountryFlag(country.countryCode)}</span>
            <h3 className="ml-2 font-semibold text-gray-900">{country.countryName}</h3>
          </div>
          <p className="text-sm text-gray-500 mt-1">{country.countryCode}</p>
        </div>
        <span className={`px-2 py-1 text-xs font-medium rounded-full ${getStatusColor(country.status)}`}>
          {country.status}
        </span>
      </div>

      <div className="mt-4 grid grid-cols-2 gap-4">
        <div>
          <p className="text-xs text-gray-500">Employees</p>
          <p className="text-lg font-semibold text-gray-900">{country.employeeCount}</p>
        </div>
        <div>
          <p className="text-xs text-gray-500">Health Score</p>
          <p className={`text-lg font-semibold ${getHealthScoreColor(country.healthScore)}`}>
            {country.healthScore}%
          </p>
        </div>
      </div>

      <div className="mt-4 pt-4 border-t border-gray-200">
        <div className="flex items-center justify-between">
          <span className="text-sm text-gray-500">Payroll</span>
          <span className="text-sm font-semibold text-gray-900">
            ${country.payrollAmount.toLocaleString()}
          </span>
        </div>
      </div>
    </div>
  );
};

function getCountryFlag(countryCode: string): string {
  const flags: Record<string, string> = {
    US: '🇺🇸',
    GB: '🇬🇧',
    DE: '🇩🇪',
    FR: '🇫🇷',
    ES: '🇪🇸',
    IT: '🇮🇹',
    NL: '🇳🇱',
    PL: '🇵🇱',
    SE: '🇸🇪',
    NO: '🇳🇴',
    DK: '🇩🇰',
    FI: '🇫🇮',
    CH: '🇨🇭',
    AT: '🇦🇹',
    BE: '🇧🇪',
    IE: '🇮🇪',
    PT: '🇵🇹',
    GR: '🇬🇷',
    CZ: '🇨🇿',
    HU: '🇭🇺',
    RO: '🇷🇴',
    BG: '🇧🇬',
    HR: '🇭🇷',
    SI: '🇸🇮',
    SK: '🇸🇰',
    LT: '🇱🇹',
    LV: '🇱🇻',
    EE: '🇪🇪',
    IS: '🇮🇸',
    LU: '🇱🇺',
    MT: '🇲🇹',
    CY: '🇨🇾',
    CA: '🇨🇦',
    AU: '🇦🇺',
    NZ: '🇳🇿',
    JP: '🇯🇵',
    SG: '🇸🇬',
    IN: '🇮🇳',
    BR: '🇧🇷',
    MX: '🇲🇽',
    AR: '🇦🇷',
    CL: '🇨🇱',
    CO: '🇨🇴',
    PE: '🇵🇪',
    ZA: '🇿🇦',
    AE: '🇦🇪',
    SA: '🇸🇦',
    IL: '🇮🇱',
    TR: '🇹🇷',
    RU: '🇷🇺',
    CN: '🇨🇳',
    KR: '🇰🇷',
    TH: '🇹🇭',
    MY: '🇲🇾',
    ID: '🇮🇩',
    PH: '🇵🇭',
    VN: '🇻🇳',
    HK: '🇭🇰',
    TW: '🇹🇼',
  };
  return flags[countryCode] || '🌍';
}

export default CountryCard;
