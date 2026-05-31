// BudgetChart Component
// Displays budget allocation and utilization

import React from 'react';
import { ChartCard } from './ChartCard';
import { PieChart, PieDataPoint } from './PieChart';
import { BarChart, BarDataPoint } from './BarChart';
import { formatCurrency, formatPercentage } from '../../utils/formatters';

export interface BudgetAllocation {
  category: string;
  allocated: number;
  spent: number;
  remaining: number;
  percentage: number;
}

export interface BudgetChartProps {
  allocations: BudgetAllocation[];
  currency?: string;
  className?: string;
  view?: 'pie' | 'bar';
}

export const BudgetChart: React.FC<BudgetChartProps> = ({
  allocations,
  currency = 'USD',
  className = '',
  view = 'pie',
}) => {
  const totalBudget = allocations.reduce((sum, a) => sum + a.allocated, 0);
  const totalSpent = allocations.reduce((sum, a) => sum + a.spent, 0);
  const totalRemaining = totalBudget - totalSpent;
  const utilizationRate = (totalSpent / totalBudget) * 100;

  const pieData: PieDataPoint[] = allocations.map((a) => ({
    label: a.category,
    value: a.allocated,
  }));

  const barData: BarDataPoint[] = allocations.map((a) => ({
    label: a.category,
    value: a.spent,
  }));

  const getUtilizationColor = (percentage: number) => {
    if (percentage >= 90) return '#EF4444';
    if (percentage >= 75) return '#F59E0B';
    if (percentage >= 50) return '#10B981';
    return '#3B82F6';
  };

  return (
    <ChartCard
      title="Budget Allocation"
      subtitle={`${formatCurrency(totalBudget, currency)} total • ${formatPercentage(utilizationRate)} utilized`}
      className={className}
    >
      <div style={{ display: 'flex', flexDirection: 'column', gap: '1.5rem' }}>
        {view === 'pie' ? (
          <div style={{ display: 'flex', justifyContent: 'center' }}>
            <PieChart
              data={pieData}
              donut
              innerRadius={0.6}
              showLegend
              colors={['#3B82F6', '#8B5CF6', '#EC4899', '#10B981', '#F59E0B']}
            />
          </div>
        ) : (
          <BarChart
            data={barData}
            horizontal
            showGrid
            showValues
            colors={['#3B82F6', '#8B5CF6', '#EC4899', '#10B981', '#F59E0B']}
          />
        )}

        {/* Utilization Bars */}
        <div className="budget-utilization-list">
          {allocations.map((allocation, i) => (
            <div key={i} className="budget-utilization-item">
              <div className="budget-item-header">
                <span className="budget-item-label">{allocation.category}</span>
                <span className="budget-item-amount">
                  {formatCurrency(allocation.spent, currency)} / {formatCurrency(allocation.allocated, currency)}
                </span>
              </div>
              <div className="budget-progress-bar">
                <div
                  className="budget-progress-fill"
                  style={{
                    width: `${Math.min((allocation.spent / allocation.allocated) * 100, 100)}%`,
                    backgroundColor: getUtilizationColor((allocation.spent / allocation.allocated) * 100),
                  }}
                />
              </div>
              <div className="budget-item-footer">
                <span className="budget-item-remaining">
                  {formatCurrency(allocation.remaining, currency)} remaining
                </span>
                <span
                  className="budget-item-percentage"
                  style={{ color: getUtilizationColor(allocation.percentage) }}
                >
                  {formatPercentage(allocation.percentage)}
                </span>
              </div>
            </div>
          ))}
        </div>
      </div>
    </ChartCard>
  );
};

export default BudgetChart;
