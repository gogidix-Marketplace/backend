import {
  KPIMetric,
  Region,
  Country,
  RegionalMetrics,
  FinancialReport,
  CustomerMetrics,
  OperationalMetrics,
  ComplianceMetrics,
  ApiResponse,
  FilterOptions,
  ExportOptions,
} from '../types';
import {
  mockData,
  simulateApiResponse,
  delay,
} from '../utils/mock-data';

// Mock API Service - Used when backend is not available
// Returns realistic data with simulated network delays

class MockAPIService {
  private delayMs: number = 300;

  setDelay(ms: number) {
    this.delayMs = ms;
  }

  // Dashboard APIs
  async getKPIMetrics(filters?: FilterOptions): Promise<ApiResponse<KPIMetric[]>> {
    await delay(this.delayMs);
    let metrics = [...mockData.kpiMetrics];

    if (filters?.categories && filters.categories.length > 0) {
      metrics = metrics.filter(m => filters.categories!.includes(m.category));
    }

    return {
      success: true,
      data: metrics,
    };
  }

  async getKPIMetric(id: string): Promise<ApiResponse<KPIMetric>> {
    await delay(this.delayMs);
    const metric = mockData.kpiMetrics.find(m => m.id === id);

    if (!metric) {
      return {
        success: false,
        data: {} as KPIMetric,
        error: 'KPI metric not found',
      };
    }

    return {
      success: true,
      data: metric,
    };
  }

  async getRegions(filters?: FilterOptions): Promise<ApiResponse<Region[]>> {
    await delay(this.delayMs);
    return {
      success: true,
      data: mockData.regions,
    };
  }

  async getRegion(id: string): Promise<ApiResponse<Region>> {
    await delay(this.delayMs);
    const region = mockData.regions.find(r => r.id === id);

    if (!region) {
      return {
        success: false,
        data: {} as Region,
        error: 'Region not found',
      };
    }

    return {
      success: true,
      data: region,
    };
  }

  async getCountries(filters?: FilterOptions): Promise<ApiResponse<Country[]>> {
    await delay(this.delayMs);
    let countries = [...mockData.countries];

    if (filters?.regions && filters.regions.length > 0) {
      countries = countries.filter(c => filters.regions!.includes(c.regionId));
    }

    return {
      success: true,
      data: countries,
    };
  }

  async getCountry(id: string): Promise<ApiResponse<Country>> {
    await delay(this.delayMs);
    const country = mockData.countries.find(c => c.id === id);

    if (!country) {
      return {
        success: false,
        data: {} as Country,
        error: 'Country not found',
      };
    }

    return {
      success: true,
      data: country,
    };
  }

  async getRegionalMetrics(filters?: FilterOptions): Promise<ApiResponse<RegionalMetrics[]>> {
    await delay(this.delayMs);
    return {
      success: true,
      data: mockData.regionalMetrics,
    };
  }

  async getRevenueTrend(period: string = '12m'): Promise<ApiResponse<any[]>> {
    await delay(this.delayMs);
    const months = parseInt(period) || 12;
    const data: any[] = [];

    const now = new Date();
    for (let i = months - 1; i >= 0; i--) {
      const date = new Date(now.getFullYear(), now.getMonth() - i, 1);
      const baseValue = 2500000 + Math.random() * 500000;
      data.push({
        period: date.toLocaleDateString('en-US', { month: 'short', year: '2-digit' }),
        value: baseValue,
        target: baseValue * 1.1,
      });
    }

    return {
      success: true,
      data,
    };
  }

  async getRegionalPerformance(): Promise<ApiResponse<any[]>> {
    await delay(this.delayMs);
    return {
      success: true,
      data: mockData.regionalPerformance,
    };
  }

  async getMarketDistribution(): Promise<ApiResponse<any[]>> {
    await delay(this.delayMs);
    return {
      success: true,
      data: mockData.marketDistribution,
    };
  }

  async exportData(options: ExportOptions): Promise<Blob> {
    await delay(1000);

    const data = {
      exportDate: new Date().toISOString(),
      format: options.format,
      scope: options.scope,
      data: mockData,
    };

    const jsonString = JSON.stringify(data, null, 2);
    return new Blob([jsonString], { type: 'application/json' });
  }

  // Financial APIs
  async getFinancialReports(filters?: FilterOptions): Promise<ApiResponse<FinancialReport[]>> {
    await delay(this.delayMs);
    return {
      success: true,
      data: mockData.financialReports,
    };
  }

  async getFinancialReport(id: string): Promise<ApiResponse<FinancialReport>> {
    await delay(this.delayMs);
    const report = mockData.financialReports.find(r => r.id === id);

    if (!report) {
      return {
        success: false,
        data: {} as FinancialReport,
        error: 'Report not found',
      };
    }

    return {
      success: true,
      data: report,
    };
  }

  async generateFinancialReport(data: Partial<FinancialReport>): Promise<ApiResponse<FinancialReport>> {
    await delay(1500);

    const newReport: FinancialReport = {
      id: `report-${Date.now()}`,
      period: data.period || 'Q1 2024',
      type: data.type || 'quarterly',
      status: 'pending',
      revenue: mockData.financialReports[0].revenue,
      expenses: mockData.financialReports[0].expenses,
      profit: Math.random() * 10000000,
      profitMargin: Math.random() * 10 + 20,
      metrics: mockData.financialReports[0].metrics,
      generatedAt: new Date().toISOString(),
      createdBy: 'Current User',
    };

    return {
      success: true,
      data: newReport,
    };
  }

  // Customer APIs
  async getCustomerMetrics(filters?: FilterOptions): Promise<ApiResponse<CustomerMetrics>> {
    await delay(this.delayMs);
    return {
      success: true,
      data: mockData.customerMetrics,
    };
  }

  async getCustomerSegments(): Promise<ApiResponse<any[]>> {
    await delay(this.delayMs);
    return {
      success: true,
      data: mockData.customerSegments,
    };
  }

  // Operations APIs
  async getOperationalMetrics(filters?: FilterOptions): Promise<ApiResponse<OperationalMetrics>> {
    await delay(this.delayMs);
    return {
      success: true,
      data: mockData.operationalMetrics,
    };
  }

  // Compliance APIs
  async getComplianceMetrics(filters?: FilterOptions): Promise<ApiResponse<ComplianceMetrics>> {
    await delay(this.delayMs);
    return {
      success: true,
      data: mockData.complianceMetrics,
    };
  }

  async refreshData(): Promise<ApiResponse<{ success: boolean }>> {
    await delay(1000);
    return {
      success: true,
      data: { success: true },
    };
  }

  // Search functionality
  async search(query: string): Promise<ApiResponse<any>> {
    await delay(this.delayMs);

    const results = {
      regions: mockData.regions.filter(r =>
        r.name.toLowerCase().includes(query.toLowerCase())
      ),
      countries: mockData.countries.filter(c =>
        c.name.toLowerCase().includes(query.toLowerCase())
      ),
    };

    return {
      success: true,
      data: results,
    };
  }
}

export const mockApiService = new MockAPIService();
export default mockApiService;
