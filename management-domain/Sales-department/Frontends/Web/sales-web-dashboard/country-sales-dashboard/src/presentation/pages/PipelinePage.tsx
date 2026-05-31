// PipelinePage Component
// Country-level pipeline stages and deal management

import React, { useState } from 'react';
import { useDealsStore } from '@infrastructure/stores';
import { mockDeals, mockPipelineData, DEAL_STAGES } from '@shared/mock-data';
import { DealsTable } from '../components/tables';
import { PipelineChart, PerformanceChart } from '../components/charts';
import { SearchBar, Button, Tabs, StatusBadge } from '../components/common';
import type { Deal, DealStage } from '@domain/types';
import { formatCurrency, formatPercentage } from '@shared';

export const PipelinePage: React.FC = () => {
  const { deals, isLoading } = useDealsStore();
  const [activeTab, setActiveTab] = useState('board');
  const [searchQuery, setSearchQuery] = useState('');
  const [stageFilter, setStageFilter] = useState<DealStage | 'ALL'>('ALL');

  const displayDeals = mockDeals;
  const displayPipeline = mockPipelineData;

  const activeDeals = displayDeals.filter(d => d.stage !== 'WON' && d.stage !== 'LOST');

  // Group deals by stage for kanban view
  const stageGroups = Object.keys(DEAL_STAGES)
    .filter(stage => stage !== 'WON' && stage !== 'LOST')
    .map(stage => ({
      stage: stage as DealStage,
      deals: activeDeals.filter(d => d.stage === stage),
      totalValue: activeDeals.filter(d => d.stage === stage).reduce((sum, d) => sum + d.value, 0),
    }));

  const pipelineChartData = Object.keys(DEAL_STAGES)
    .filter(stage => stage !== 'WON' && stage !== 'LOST')
    .map(stage => {
      const stageDeals = activeDeals.filter(d => d.stage === stage);
      return {
        stage: stage as DealStage,
        count: stageDeals.length,
        totalValue: stageDeals.reduce((sum, d) => sum + d.value, 0),
        percentage: Math.round((stageDeals.length / activeDeals.length) * 100),
      };
    });

  const conversionData = Object.entries(DEAL_STAGES).map(([stage, info]) => ({
    label: info.label,
    value: info.probability,
    target: 100,
    color: info.color,
  }));

  const handleStageChange = async (dealId: string, newStage: DealStage) => {
    // Stage change logic
  };

  const tabs = [
    {
      id: 'board',
      label: 'Kanban Board',
      content: (
        <div className="pipeline-board">
          {stageGroups.map(group => {
            const stageInfo = DEAL_STAGES[group.stage];
            return (
              <div key={group.stage} className="pipeline-column">
                <div className="column-header" style={{ borderBottomColor: stageInfo.color }}>
                  <span className="column-name">{stageInfo.label}</span>
                  <span className="column-count">{group.deals.length}</span>
                  <span className="column-value">{formatCurrency(group.totalValue, 'USD')}</span>
                </div>
                <div className="column-deals">
                  {group.deals.map(deal => (
                    <div key={deal.id} className="deal-card">
                      <div className="deal-card-header">
                        <span className="deal-card-name">{deal.name}</span>
                        <span className="deal-card-value">{formatCurrency(deal.value, 'USD')}</span>
                      </div>
                      <div className="deal-card-body">
                        <span className="deal-card-account">{deal.accountName}</span>
                        <span className="deal-card-owner">{deal.owner.name}</span>
                      </div>
                      <div className="deal-card-footer">
                        <span className="deal-card-probability">{deal.probability}%</span>
                        <span className="deal-card-close">
                          {new Date(deal.expectedCloseDate).toLocaleDateString()}
                        </span>
                      </div>
                    </div>
                  ))}
                  {group.deals.length === 0 && (
                    <div className="column-empty">No deals</div>
                  )}
                </div>
              </div>
            );
          })}
        </div>
      ),
    },
    {
      id: 'list',
      label: 'List View',
      content: (
        <DealsTable
          deals={displayDeals}
          isLoading={isLoading}
          onDealClick={(deal) => {}}
          onStageChange={handleStageChange}
        />
      ),
    },
    {
      id: 'analytics',
      label: 'Analytics',
      content: (
        <div className="pipeline-analytics">
          <div className="analytics-grid">
            <div className="analytics-card">
              <h4>Pipeline Overview</h4>
              <PipelineChart data={pipelineChartData} height={300} />
            </div>
            <div className="analytics-card">
              <h4>Stage Conversion</h4>
              <PerformanceChart
                data={conversionData}
                type="bar"
                height={300}
              />
            </div>
          </div>

          <div className="pipeline-metrics">
            <h4>Pipeline Metrics</h4>
            <div className="metrics-grid">
              <div className="metric-item">
                <span className="metric-label">Total Pipeline Value</span>
                <span className="metric-value">{formatCurrency(displayPipeline.totalValue, 'USD')}</span>
              </div>
              <div className="metric-item">
                <span className="metric-label">Weighted Pipeline</span>
                <span className="metric-value">{formatCurrency(displayPipeline.weightedValue, 'USD')}</span>
              </div>
              <div className="metric-item">
                <span className="metric-label">Avg Deal Size</span>
                <span className="metric-value">{formatCurrency(displayPipeline.avgDealSize, 'USD')}</span>
              </div>
              <div className="metric-item">
                <span className="metric-label">Sales Cycle</span>
                <span className="metric-value">{displayPipeline.velocity.overallCycle} days</span>
              </div>
            </div>
          </div>

          <div className="velocity-by-stage">
            <h4>Average Days in Stage</h4>
            {Object.entries(displayPipeline.velocity.avgDaysInStage).map(([stage, days]) => {
              if (stage === 'WON' || stage === 'LOST') return null;
              const stageInfo = DEAL_STAGES[stage as DealStage];
              return (
                <div key={stage} className="velocity-item">
                  <span className="velocity-stage" style={{ color: stageInfo.color }}>
                    {stageInfo.label}
                  </span>
                  <div className="velocity-bar">
                    <div
                      className="velocity-fill"
                      style={{
                        width: `${(days / 60) * 100}%`,
                        backgroundColor: stageInfo.color,
                      }}
                    />
                  </div>
                  <span className="velocity-days">{days} days</span>
                </div>
              );
            })}
          </div>
        </div>
      ),
    },
  ];

  return (
    <div className="page pipeline-page">
      <div className="page-header">
        <div className="page-title">
          <h1>Deal Pipeline</h1>
          <p className="page-subtitle">Track and manage deals through the sales process</p>
        </div>
        <div className="page-actions">
          <Button variant="secondary">Create View</Button>
          <Button variant="primary">Add Deal</Button>
        </div>
      </div>

      {/* Pipeline Summary */}
      <div className="pipeline-summary">
        <div className="summary-card">
          <span className="summary-label">Total Pipeline</span>
          <span className="summary-value">{formatCurrency(displayPipeline.totalValue, 'USD')}</span>
        </div>
        <div className="summary-card">
          <span className="summary-label">Weighted Pipeline</span>
          <span className="summary-value">{formatCurrency(displayPipeline.weightedValue, 'USD')}</span>
        </div>
        <div className="summary-card">
          <span className="summary-label">Active Deals</span>
          <span className="summary-value">{displayPipeline.totalDeals}</span>
        </div>
        <div className="summary-card">
          <span className="summary-label">Win Rate</span>
          <span className="summary-value">
            {formatPercentage((displayDeals.filter(d => d.stage === 'WON').length / displayDeals.length) * 100)}
          </span>
        </div>
      </div>

      {/* Search */}
      <div className="page-filters">
        <SearchBar
          placeholder="Search deals..."
          value={searchQuery}
          onChange={setSearchQuery}
        />
      </div>

      {/* Tabs */}
      <Tabs tabs={tabs} activeTab={activeTab} onChange={setActiveTab} />
    </div>
  );
};

export default PipelinePage;
