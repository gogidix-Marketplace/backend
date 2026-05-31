// CountriesTable Component
// Displays country comparison table with sorting and filtering

import { useState, useMemo, type React } from 'react';
import type { CountrySummary, CountryInfo } from '@domain/types';
import { formatCurrency } from '@shared/utils';
import { StatusBadge } from '../common/StatusBadge';
import './CountriesTable.css';

export interface CountriesTableProps {
  countries: CountrySummary[];
  onCountryClick?: (countryCode: string) => void;
  className?: string;
  showRegionalGrouping?: boolean;
}

export function CountriesTable({
  countries,
  onCountryClick,
  className = '',
  showRegionalGrouping = true,
}: CountriesTableProps): React.ReactElement {
  const [sortColumn, setSortColumn] = useState<string>('revenue');
  const [sortDirection, setSortDirection] = useState<'asc' | 'desc'>('desc');
  const [filterRegion, setFilterRegion] = useState<string>('all');

  // Group by region
  const groupedCountries = useMemo(() => {
    if (!showRegionalGrouping) {
      return { 'All Countries': countries };
    }
    return countries.reduce((acc, country) => {
      const region = country.country.region || 'Other';
      if (!acc[region]) acc[region] = [];
      acc[region].push(country);
      return acc;
    }, {} as Record<string, CountrySummary[]>);
  }, [countries, showRegionalGrouping]);

  const regions = Object.keys(groupedCountries);

  // Sort countries
  const sortedCountries = useMemo(() => {
    return [...countries].sort((a, b) => {
      const aValue = sortColumn === 'revenue' ? a.metrics.revenue
        : sortColumn === 'quota' ? a.metrics.attainment
        : sortColumn === 'deals' ? a.metrics.dealsClosed
        : sortColumn === 'pipeline' ? a.metrics.pipelineValue
        : sortColumn === 'winRate' ? a.metrics.winRate
        : sortColumn === 'growth' ? a.metrics.growth
        : a.metrics.revenue;

      const bValue = sortColumn === 'revenue' ? b.metrics.revenue
        : sortColumn === 'quota' ? b.metrics.attainment
        : sortColumn === 'deals' ? b.metrics.dealsClosed
        : sortColumn === 'pipeline' ? b.metrics.pipelineValue
        : sortColumn === 'winRate' ? b.metrics.winRate
        : sortColumn === 'growth' ? b.metrics.growth
        : b.metrics.revenue;

      if (sortDirection === 'asc') {
        return aValue - bValue;
      }
      return bValue - aValue;
    });
  }, [countries, sortColumn, sortDirection]);

  const handleSort = (column: string) => {
    if (sortColumn === column) {
      setSortDirection(sortDirection === 'asc' ? 'desc' : 'asc');
    } else {
      setSortColumn(column);
      setSortDirection('desc');
    }
  };

  const renderSortIcon = (column: string) => {
    if (sortColumn !== column) return null;
    return sortDirection === 'asc' ? '↑' : '↓';
  };

  return (
    <div className={`countries-table ${className}`}>
      {/* Filters */}
      <div className="countries-table-filters">
        <select
          value={filterRegion}
          onChange={(e) => setFilterRegion(e.target.value)}
          className="region-filter"
        >
          <option value="all">All Regions</option>
          {regions.map(region => (
            <option key={region} value={region}>{region}</option>
          ))}
        </select>
      </div>

      {/* Table */}
      <div className="countries-table-container">
        <table className="countries-data-table">
          <thead>
            <tr>
              <th>Country</th>
              <th className="sortable" onClick={() => handleSort('revenue')}>
                Revenue {renderSortIcon('revenue')}
              </th>
              <th className="sortable" onClick={() => handleSort('quota')}>
                Quota {renderSortIcon('quota')}
              </th>
              <th className="sortable" onClick={() => handleSort('deals')}>
                Deals {renderSortIcon('deals')}
              </th>
              <th className="sortable" onClick={() => handleSort('pipeline')}>
                Pipeline {renderSortIcon('pipeline')}
              </th>
              <th className="sortable" onClick={() => handleSort('winRate')}>
                Win Rate {renderSortIcon('winRate')}
              </th>
              <th className="sortable" onClick={() => handleSort('growth')}>
                Growth {renderSortIcon('growth')}
              </th>
              <th>Status</th>
              <th>Director</th>
            </tr>
          </thead>
          <tbody>
            {sortedCountries
              .filter(c => filterRegion === 'all' || c.country.region === filterRegion)
              .map((country) => (
                <tr
                  key={country.country.code}
                  className={onCountryClick ? 'clickable' : ''}
                  onClick={() => onCountryClick?.(country.country.code)}
                >
                  <td className="country-cell">
                    <span className="country-flag">{country.country.flag}</span>
                    <span className="country-name">{country.country.name}</span>
                    <span className="country-code">{country.country.code}</span>
                  </td>
                  <td className="revenue-cell">
                    <span className="revenue-value">{formatCurrency(country.metrics.revenue)}</span>
                  </td>
                  <td className="quota-cell">
                    <div className="quota-attainment">
                      <div className="quota-bar">
                        <div
                          className={`quota-fill quota-fill-${country.status}`}
                          style={{ width: `${Math.min(country.metrics.attainment, 100)}%` }}
                        ></div>
                      </div>
                      <span className="quota-value">{country.metrics.attainment}%</span>
                    </div>
                  </td>
                  <td className="deals-cell">{country.metrics.dealsClosed}</td>
                  <td className="pipeline-cell">{formatCurrency(country.metrics.pipelineValue)}</td>
                  <td className="winrate-cell">{country.metrics.winRate}%</td>
                  <td className="growth-cell">
                    <span className={`growth-indicator growth-${country.trend}`}>
                      {country.trend === 'up' && '↑'}
                      {country.trend === 'down' && '↓'}
                      {country.metrics.growth}%
                    </span>
                  </td>
                  <td className="status-cell">
                    <StatusBadge status={country.status} size="sm" />
                  </td>
                  <td className="director-cell">{country.country.directorName}</td>
                </tr>
              ))}
          </tbody>
        </table>
      </div>

      {/* Summary */}
      <div className="countries-table-summary">
        <span>{sortedCountries.length} countries</span>
        <span>Total Revenue: {formatCurrency(sortedCountries.reduce((sum, c) => sum + c.metrics.revenue, 0))}</span>
        <span>Avg Quota Attainment: {Math.round(sortedCountries.reduce((sum, c) => sum + c.metrics.attainment, 0) / sortedCountries.length)}%</span>
      </div>
    </div>
  );
}

export default CountriesTable;
