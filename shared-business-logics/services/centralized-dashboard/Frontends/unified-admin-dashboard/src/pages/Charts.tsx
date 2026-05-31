import React from 'react'
import { useQuery } from '@tanstack/react-query'
import TimeSeriesChart from '@/components/TimeSeriesChart'
import { chartApi } from '@/lib/api'

export default function Charts() {
  const { data: chartSummary, isLoading: summaryLoading } = useQuery({
    queryKey: ['chart-summary'],
    queryFn: chartApi.getCharts,
  })

  const { data: metrics } = useQuery({
    queryKey: ['chart-metrics'],
    queryFn: chartApi.getMetrics,
    refetchInterval: 30000,
  })

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-secondary-900">Charts</h1>
        <p className="text-secondary-600">View and manage chart configurations</p>
      </div>

      {/* Chart Summary */}
      {chartSummary && (
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          <div className="card">
            <p className="text-sm font-medium text-secondary-600">Total Charts</p>
            <p className="mt-2 text-3xl font-semibold text-secondary-900">
              {chartSummary.totalCharts}
            </p>
          </div>
          <div className="card">
            <p className="text-sm font-medium text-secondary-600">Active Charts</p>
            <p className="mt-2 text-3xl font-semibold text-green-600">
              {chartSummary.activeCharts}
            </p>
          </div>
          <div className="card">
            <p className="text-sm font-medium text-secondary-600">Total Data Points</p>
            <p className="mt-2 text-3xl font-semibold text-blue-600">
              {chartSummary.totalDataPoints}
            </p>
          </div>
        </div>
      )}

      {/* Metrics */}
      {metrics && (
        <div className="card">
          <h3 className="text-lg font-semibold mb-4">Metrics Overview</h3>
          <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
            {Object.entries(metrics).slice(0, 8).map(([key, value]) => (
              <div key={key} className="p-3 bg-secondary-50 rounded-lg">
                <p className="text-xs text-secondary-500">{key}</p>
                <p className="text-lg font-semibold text-secondary-900">
                  {typeof value === 'number' ? value.toFixed(2) : String(value)}
                </p>
              </div>
            ))}
          </div>
        </div>
      )}

      {/* Sample Charts */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <TimeSeriesChart
          data={{
            chartId: 'sample-1',
            chartName: 'Request Throughput',
            chartType: 'line',
            data: generateSampleData('throughput'),
            generatedAt: new Date().toISOString(),
            tenantId: 'default',
          }}
        />
        <TimeSeriesChart
          data={{
            chartId: 'sample-2',
            chartName: 'Response Time',
            chartType: 'area',
            data: generateSampleData('latency'),
            generatedAt: new Date().toISOString(),
            tenantId: 'default',
          }}
        />
        <TimeSeriesChart
          data={{
            chartId: 'sample-3',
            chartName: 'Error Rate',
            chartType: 'bar',
            data: generateSampleData('errors'),
            generatedAt: new Date().toISOString(),
            tenantId: 'default',
          }}
        />
        <TimeSeriesChart
          data={{
            chartId: 'sample-4',
            chartName: 'Status Distribution',
            chartType: 'pie',
            data: [
              { label: 'Success', value: 450, timestamp: new Date().toISOString() },
              { label: 'Pending', value: 120, timestamp: new Date().toISOString() },
              { label: 'Failed', value: 30, timestamp: new Date().toISOString() },
            ],
            generatedAt: new Date().toISOString(),
            tenantId: 'default',
          }}
        />
      </div>
    </div>
  )
}

function generateSampleData(type: string) {
  const now = Date.now()
  const data = []
  for (let i = 0; i < 24; i++) {
    const timestamp = new Date(now - (23 - i) * 3600000).toISOString()
    let value = 0
    switch (type) {
      case 'throughput':
        value = Math.random() * 100 + 50
        break
      case 'latency':
        value = Math.random() * 200 + 50
        break
      case 'errors':
        value = Math.random() * 10
        break
    }
    data.push({ timestamp, value })
  }
  return data
}
