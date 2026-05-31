'use client';

import React from 'react';
import { CountryComparison } from '../../api/globalHrApi';

interface WorldMapCardProps {
  countries: CountryComparison[];
  onCountryClick: (countryCode: string) => void;
}

export const WorldMapCard: React.FC<WorldMapCardProps> = ({ countries, onCountryClick }) => {
  const getCountryFillColor = (healthScore: number) => {
    if (healthScore >= 90) return '#22c55e';
    if (healthScore >= 75) return '#3b82f6';
    if (healthScore >= 60) return '#eab308';
    return '#ef4444';
  };

  const regionData = [
    { region: 'Africa', countries: countries.filter(c => ['NG', 'KE', 'ZA', 'GH', 'EG'].includes(c.countryCode)) },
    { region: 'Europe', countries: countries.filter(c => ['GB', 'IE', 'DE', 'FR', 'NL', 'SE', 'NO', 'DK'].includes(c.countryCode)) },
    { region: 'Asia', countries: countries.filter(c => ['JP', 'SG', 'IN', 'MY', 'TH', 'PH', 'VN', 'HK'].includes(c.countryCode)) },
    { region: 'Americas', countries: countries.filter(c => ['US', 'CA', 'BR', 'MX', 'AR', 'CL', 'CO'].includes(c.countryCode)) },
  ];

  return (
    <div className="bg-white rounded-lg shadow p-6">
      <h3 className="text-lg font-semibold text-gray-900 mb-4">Global Operations Map</h3>

      {/* Simplified World Map Visualization */}
      <div className="bg-gradient-to-b from-blue-50 to-blue-100 rounded-lg p-4 mb-4">
        <div className="grid grid-cols-2 gap-4">
          {regionData.map((region) => (
            <div key={region.region} className="bg-white rounded-lg p-3">
              <p className="text-sm font-medium text-gray-700 mb-2">{region.region}</p>
              <div className="space-y-2">
                {region.countries.slice(0, 4).map((country) => (
                  <div
                    key={country.countryCode}
                    onClick={() => onCountryClick(country.countryCode)}
                    className="flex items-center justify-between cursor-pointer hover:bg-gray-50 rounded p-1"
                  >
                    <div className="flex items-center gap-2">
                      <div
                        className="w-3 h-3 rounded-full"
                        style={{ backgroundColor: getCountryFillColor(country.healthScore) }}
                      ></div>
                      <span className="text-xs text-gray-600">{country.countryCode}</span>
                    </div>
                    <span className="text-xs font-medium text-gray-900">{country.healthScore}</span>
                  </div>
                ))}
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Legend */}
      <div className="flex items-center justify-center gap-4 text-xs text-gray-600">
        <span>Health Score:</span>
        <div className="flex items-center gap-1">
          <div className="w-3 h-3 rounded-full bg-green-500"></div>
          <span>90+</span>
        </div>
        <div className="flex items-center gap-1">
          <div className="w-3 h-3 rounded-full bg-blue-500"></div>
          <span>75-89</span>
        </div>
        <div className="flex items-center gap-1">
          <div className="w-3 h-3 rounded-full bg-yellow-500"></div>
          <span>60-74</span>
        </div>
        <div className="flex items-center gap-1">
          <div className="w-3 h-3 rounded-full bg-red-500"></div>
          <span>&lt;60</span>
        </div>
      </div>
    </div>
  );
};

export default WorldMapCard;
