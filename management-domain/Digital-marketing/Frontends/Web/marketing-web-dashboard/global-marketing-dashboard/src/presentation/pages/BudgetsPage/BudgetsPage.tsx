// BudgetsPage - Global Marketing Dashboard
// Global budget allocation and tracking page

import React, { useState } from 'react';
import { Budget, BudgetStatus } from '../../../domain/types';
import { useBudgetStore } from '../../../infrastructure/stores/budgetStore';
import { BudgetTable } from '../../../shared/components/tables/BudgetTable';
import { BudgetChart } from '../../../shared/components/charts/BudgetChart';
import { MetricCard } from '../../../shared/components/common/MetricCard';
import { Button } from '../../../shared/components/common/Button';
import { SearchBar } from '../../../shared/components/common/SearchBar';
import { FilterPanel, FilterGroup } from '../../../shared/components/common/FilterPanel';
import { Tabs, TabPanel } from '../../../shared/components/common/Tabs';
import { formatCurrency, formatPercentage } from '../../../shared/utils/formatters';
import './BudgetsPage.css';

const BudgetsPage: React.FC = () => {
  const budgets = useBudgetStore((state) => state.budgets);
  const { updateBudget, setFilters } = useBudgetStore();

  const [activeTab, setActiveTab] = useState('all');
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedView, setSelectedView] = useState<'table' | 'chart'>('table');

  // Calculate summary metrics
  const summaryMetrics = {
    totalBudget: budgets.reduce((sum, b) => sum + b.totalAmount, 0),
    totalSpent: budgets.reduce((sum, b) => sum + b.spentAmount, 0),
    totalRemaining: budgets.reduce((sum, b) => sum + b.remainingAmount, 0),
    utilization: budgets.length > 0
      ? (budgets.reduce((sum, b) => sum + b.spentAmount, 0) /
         budgets.reduce((sum, b) => sum + b.totalAmount, 0)) * 100
      : 0,
    activeBudgets: budgets.filter(b => b.status === 'active').length,
  };

  const handleTabChange = (tabId: string) => {
    setActiveTab(tabId);
    if (tabId === 'all') {
      setFilters({ status: undefined });
    } else {
      setFilters({ status: tabId as BudgetStatus });
    }
  };

  const tabs = [
    { id: 'all', label: 'All Budgets' },
    { id: 'active', label: 'Active' },
    { id: 'approved', label: 'Approved' },
    { id: 'draft', label: 'Draft' },
  ];

  const filterGroups: FilterGroup[] = [
    {
      id: 'fiscalYear',
      label: 'Fiscal Year',
      type: 'select',
      options: [
        { value: '2024', label: '2024' },
        { value: '2025', label: '2025' },
        { value: '2026', label: '2026' },
      ],
    },
    {
      id: 'quarter',
      label: 'Quarter',
      type: 'select',
      options: [
        { value: '1', label: 'Q1' },
        { value: '2', label: 'Q2' },
        { value: '3', label: 'Q3' },
        { value: '4', label: 'Q4' },
      ],
    },
  ];

  return (
    <div className="budgets-page">
      <div className="page__header">
        <div className="container">
          <div className="page__header-content">
            <div>
              <h1 className="page__title">Global Budgets</h1>
              <p className="page__subtitle">Track and manage marketing budgets across all regions</p>
            </div>
            <div className="page__actions">
              <Button variant="primary">+ Create Budget</Button>
            </div>
          </div>
        </div>
      </div>

      <div className="container page__content">
        {/* Summary Metrics */}
        <div className="metrics-grid">
          <MetricCard
            label="Total Budget"
            value={formatCurrency(summaryMetrics.totalBudget)}
            size="md"
          />
          <MetricCard
            label="Total Spent"
            value={formatCurrency(summaryMetrics.totalSpent)}
            size="md"
          />
          <MetricCard
            label="Remaining"
            value={formatCurrency(summaryMetrics.totalRemaining)}
            size="md"
          />
          <MetricCard
            label="Utilization"
            value={`${formatPercentage(summaryMetrics.utilization)}`}
            change={summaryMetrics.utilization > 75 ? -5 : 8}
            changeType={summaryMetrics.utilization > 75 ? 'negative' : 'positive'}
            size="md"
          />
        </div>

        {/* View Toggle and Filters */}
        <div className="budgets-controls">
          <div className="view-toggle">
            <button
              className={`toggle-btn ${selectedView === 'table' ? 'active' : ''}`}
              onClick={() => setSelectedView('table')}
              type="button"
            >
              Table View
            </button>
            <button
              className={`toggle-btn ${selectedView === 'chart' ? 'active' : ''}`}
              onClick={() => setSelectedView('chart')}
              type="button"
            >
              Chart View
            </button>
          </div>

          <Tabs
            tabs={tabs}
            activeTab={activeTab}
            onChange={handleTabChange}
            variant="pills"
          />

          <SearchBar
            value={searchQuery}
            onChange={setSearchQuery}
            placeholder="Search budgets..."
            size="sm"
          />
        </div>

        {/* Filter Panel */}
        <FilterPanel
          filters={filterGroups}
          onFilterChange={(id, value) => console.log('Filter', id, value)}
          onClear={() => console.log('Clear filters')}
          defaultExpanded={false}
        />

        {/* Content */}
        {selectedView === 'table' ? (
          <div className="budgets-table-container">
            <BudgetTable budgets={budgets} />
          </div>
        ) : (
          <div className="budgets-chart-container">
            <BudgetChart
              allocations={budgets[0]?.allocations || []}
              currency={budgets[0]?.currency || 'USD'}
              view="pie"
            />
          </div>
        )}
      </div>
    </div>
  );
};

export default BudgetsPage;
