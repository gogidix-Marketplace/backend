// Pipeline Page
// Pipeline analytics and management

import { useState, useMemo } from 'react';
import { MetricCard } from '../components/common/MetricCard';
import { Card, CardHeader, CardBody } from '../components/common/Card';
import { Button } from '../components/common/Button';
import {
  PipelineFunnel,
  PipelineDonut,
  PipelineStageChart,
  PipelineVelocityChart,
  PipelineCountryHeatmap,
} from '../components/charts';
import { FilterForm } from '../components/forms';
import { formatCurrency, formatNumber } from '@shared';
import { mockPipelineSummary, mockCountries } from '@shared/mock-data';
import './PipelinePage.css';

export function PipelinePage() {
  const [selectedCountry, setSelectedCountry] = useState<string>('all');
  const [selectedStage, setSelectedStage] = useState<string>('all');

  const filteredData = useMemo(() => {
    if (selectedCountry === 'all') return mockPipelineSummary;

    const countryData = mockPipelineSummary.byCountry.find(
      (c) => c.country.code === selectedCountry
    );
    if (!countryData) return mockPipelineSummary;

    return {
      ...mockPipelineSummary,
      totalValue: countryData.totalValue,
      totalDeals: countryData.dealCount,
      weightedValue: countryData.weightedValue,
      avgDealSize: countryData.avgDealSize,
      byCountry: [countryData],
    };
  }, [selectedCountry]);

  const stageData = selectedCountry === 'all'
    ? mockPipelineSummary.byStage
    : filteredData.byCountry[0]?.stageDistribution
      ? Object.entries(filteredData.byCountry[0].stageDistribution).map(([stage, count]) => ({
          stage,
          count: count as number,
          totalValue: filteredData.totalValue / 5,
          weightedValue: filteredData.weightedValue / 5,
          percentage: ((count as number) / filteredData.totalDeals) * 100,
          avgDaysInStage: 0,
        }))
      : mockPipelineSummary.byStage;

  return (
    <div className="pipeline-page">
      {/* Page Header */}
      <div className="page-header">
        <div>
          <h1>Global Pipeline</h1>
          <p className="page-subtitle">Track deals and sales cycle velocity</p>
        </div>
        <div className="page-actions">
          <Button variant="secondary" size="sm">
            <svg width="16" height="16" viewBox="0 0 16 16" fill="currentColor">
              <path d="M.5 9.9a.5.5 0 0 1 .5.5v2.5a1 1 0 0 0 1 1h12a1 1 0 0 0 1-1v-2.5a.5.5 0 0 1 1 0v2.5a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2v-2.5a.5.5 0 0 1 .5-.5z" />
              <path d="M7.646 11.854a.5.5 0 0 0 .708 0l3-3a.5.5 0 0 0-.708-.708L8.5 10.293V1.5a.5.5 0 0 0-1 0v8.793L5.354 8.146a.5.5 0 1 0-.708.708l3 3z" />
            </svg>
            Export Report
          </Button>
        </div>
      </div>

      {/* Key Metrics */}
      <section className="metrics-section">
        <div className="metrics-grid">
          <MetricCard
            title="Pipeline Value"
            value={formatCurrency(filteredData.totalValue)}
            change={12}
            changeType="increase"
            icon={
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M22 12h-4l-3 9L9 3l-3 9H2" />
              </svg>
            }
          />
          <MetricCard
            title="Weighted Pipeline"
            value={formatCurrency(filteredData.weightedValue)}
            icon={
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <circle cx="12" cy="12" r="10" />
                <path d="M12 6v6l4 2" />
              </svg>
            }
          />
          <MetricCard
            title="Total Deals"
            value={formatNumber(filteredData.totalDeals)}
            icon={
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M14.7 6.3a1 1 0 0 0 0 1.4l1.6 1.6a1 1 0 0 0 1.4 0l3.77-3.77a6 6 0 0 1-7.94 7.94l-6.91 6.91a2.12 2.12 0 0 1-3-3l6.91-6.91a6 6 0 0 1 7.94-7.94l3.76 3.76a1 1 0 0 0 1.4-0l1.6-1.6a1 1 0 0 0 0-1.4z" />
              </svg>
            }
          />
          <MetricCard
            title="Avg Deal Size"
            value={formatCurrency(filteredData.avgDealSize)}
            icon={
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <line x1="12" y1="20" x2="12" y2="10" />
                <line x1="18" y1="20" x2="18" y2="4" />
                <line x1="6" y1="20" x2="6" y2="16" />
              </svg>
            }
          />
        </div>
      </section>

      {/* Pipeline Overview */}
      <section className="pipeline-overview">
        <div className="pipeline-grid">
          {/* Pipeline Funnel */}
          <Card className="funnel-card">
            <CardHeader>
              <h3>Pipeline Funnel</h3>
            </CardHeader>
            <CardBody>
              <PipelineFunnel stages={stageData} height={240} />
            </CardBody>
          </Card>

          {/* Pipeline Donut */}
          <Card className="donut-card">
            <CardHeader>
              <h3>Deals by Stage</h3>
            </CardHeader>
            <CardBody className="donut-body">
              <PipelineDonut summary={filteredData} size={200} />
            </CardBody>
          </Card>
        </div>
      </section>

      {/* Velocity Chart */}
      <section className="velocity-section">
        <Card>
          <CardHeader>
            <h3>Sales Cycle Velocity</h3>
          </CardHeader>
          <CardBody>
            <PipelineVelocityChart
              velocity={filteredData.velocity}
              stages={['New', 'Qualified', 'Proposal', 'Negotiating', 'Closing']}
              height={140}
            />
          </CardBody>
        </Card>
      </section>

      {/* Country Heatmap */}
      <section className="heatmap-section">
        <Card>
          <CardHeader>
            <h3>Pipeline by Country & Stage</h3>
          </CardHeader>
          <CardBody>
            <PipelineCountryHeatmap
              byCountry={mockPipelineSummary.byCountry}
              stages={['New', 'Qualified', 'Proposal', 'Negotiating', 'Closing']}
            />
          </CardBody>
        </Card>
      </section>

      {/* Stage Breakdown Table */}
      <section className="table-section">
        <Card>
          <CardHeader>
            <h3>Stage Breakdown</h3>
          </CardHeader>
          <CardBody>
            <table className="stage-table">
              <thead>
                <tr>
                  <th>Stage</th>
                  <th>Deals</th>
                  <th>Value</th>
                  <th>Weighted</th>
                  <th>%</th>
                  <th>Avg Days</th>
                </tr>
              </thead>
              <tbody>
                {stageData.map((stage) => (
                  <tr key={stage.stage}>
                    <td>
                      <span className={`stage-badge stage-${stage.stage.toLowerCase()}`}>
                        {stage.stage}
                      </span>
                    </td>
                    <td>{stage.count}</td>
                    <td>{formatCurrency(stage.totalValue)}</td>
                    <td>{formatCurrency(stage.weightedValue)}</td>
                    <td>{stage.percentage.toFixed(1)}%</td>
                    <td>{stage.avgDaysInStage || '-'}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </CardBody>
        </Card>
      </section>
    </div>
  );
}
