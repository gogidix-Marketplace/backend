/**
 * Unit tests for Campaign Entity
 */

import { describe, it, expect, beforeEach } from 'vitest';
import {
  CampaignStatus,
  CampaignType,
  ChannelType,
  type Campaign,
  type CampaignMetrics,
  type CountryTarget
} from '../Campaign.entity';

describe('Campaign Entity', () => {
  let mockCampaign: Campaign;

  beforeEach(() => {
    mockCampaign = {
      id: 'campaign-123',
      name: 'Global Brand Awareness 2024',
      description: 'Multi-channel brand awareness campaign',
      status: CampaignStatus.ACTIVE,
      type: CampaignType.AWARENESS,
      channels: [ChannelType.EMAIL, ChannelType.SOCIAL, ChannelType.DISPLAY],
      countries: [
        {
          countryCode: 'US',
          countryName: 'United States',
          budget: 100000,
          targetAudience: 'Professionals 25-45'
        },
        {
          countryCode: 'UK',
          countryName: 'United Kingdom',
          budget: 75000,
          targetAudience: 'Young professionals 18-35'
        }
      ],
      budget: {
        total: 250000,
        spent: 125000,
        remaining: 125000,
        currency: 'USD'
      },
      dates: {
        start: new Date('2024-01-01'),
        end: new Date('2024-12-31'),
        createdAt: new Date('2023-12-01'),
        updatedAt: new Date('2024-01-15')
      },
      metrics: {
        impressions: 5000000,
        clicks: 250000,
        conversions: 5000,
        cost: 125000,
        revenue: 375000,
        leads: 5000,
        ctr: 5.0,
        cpc: 0.50,
        cpa: 25.0,
        roas: 3.0
      },
      owner: {
        id: 'user-456',
        name: 'John Doe',
        email: 'john.doe@example.com'
      },
      brand: {
        id: 'brand-789',
        name: 'Acme Corp'
      },
      tags: ['brand-awareness', 'q1-2024', 'global'],
      objectives: ['Increase brand recognition', 'Drive website traffic'],
      kpis: [
        { name: 'Impressions', target: 10000000, current: 5000000, unit: 'count' },
        { name: 'Conversions', target: 10000, current: 5000, unit: 'count' }
      ]
    };
  });

  describe('Campaign Properties', () => {
    test('should have required properties', () => {
      expect(mockCampaign.id).toBe('campaign-123');
      expect(mockCampaign.name).toBe('Global Brand Awareness 2024');
      expect(mockCampaign.status).toBe(CampaignStatus.ACTIVE);
      expect(mockCampaign.type).toBe(CampaignType.AWARENESS);
    });

    test('should have channels array', () => {
      expect(mockCampaign.channels.length).toBe(3);
      expect(mockCampaign.channels).toContain(ChannelType.EMAIL);
      expect(mockCampaign.channels).toContain(ChannelType.SOCIAL);
    });

    test('should have country targets', () => {
      expect(mockCampaign.countries.length).toBe(2);
      expect(mockCampaign.countries[0].countryCode).toBe('US');
      expect(mockCampaign.countries[0].budget).toBe(100000);
    });

    test('should have budget information', () => {
      expect(mockCampaign.budget.total).toBe(250000);
      expect(mockCampaign.budget.spent).toBe(125000);
      expect(mockCampaign.budget.remaining).toBe(125000);
      expect(mockCampaign.budget.currency).toBe('USD');
    });

    test('should have metrics', () => {
      expect(mockCampaign.metrics.impressions).toBe(5000000);
      expect(mockCampaign.metrics.clicks).toBe(250000);
      expect(mockCampaign.metrics.ctr).toBe(5.0);
      expect(mockCampaign.metrics.roas).toBe(3.0);
    });
  });

  describe('CampaignStatus Enum', () => {
    test('should have all status values', () => {
      expect(CampaignStatus.DRAFT).toBe('draft');
      expect(CampaignStatus.SCHEDULED).toBe('scheduled');
      expect(CampaignStatus.ACTIVE).toBe('active');
      expect(CampaignStatus.PAUSED).toBe('paused');
      expect(CampaignStatus.COMPLETED).toBe('completed');
      expect(CampaignStatus.CANCELLED).toBe('cancelled');
    });
  });

  describe('CampaignType Enum', () => {
    test('should have all type values', () => {
      expect(CampaignType.AWARENESS).toBe('awareness');
      expect(CampaignType.CONSIDERATION).toBe('consideration');
      expect(CampaignType.CONVERSION).toBe('conversion');
      expect(CampaignType.RETENTION).toBe('retention');
    });
  });

  describe('ChannelType Enum', () => {
    test('should have all channel values', () => {
      expect(ChannelType.EMAIL).toBe('email');
      expect(ChannelType.SOCIAL).toBe('social');
      expect(ChannelType.SEARCH).toBe('search');
      expect(ChannelType.DISPLAY).toBe('display');
      expect(ChannelType.VIDEO).toBe('video');
    });
  });

  describe('Utility Functions', () => {
    test('should calculate budget utilization', () => {
      const utilization = (mockCampaign.budget.spent / mockCampaign.budget.total) * 100;
      expect(utilization).toBe(50);
    });

    test('should calculate KPI progress', () => {
      const impressionsKpi = mockCampaign.kpis.find(k => k.name === 'Impressions');
      const progress = (impressionsKpi!.current / impressionsKpi!.target) * 100;
      expect(progress).toBe(50);
    });

    test('should calculate total budget across countries', () => {
      const totalCountryBudget = mockCampaign.countries.reduce((sum, country) => sum + country.budget, 0);
      expect(totalCountryBudget).toBe(175000);
    });

    test('should check if campaign is on track', () => {
      const conversionsKpi = mockCampaign.kpis.find(k => k.name === 'Conversions');
      const onTrack = conversionsKpi!.current >= conversionsKpi!.target * 0.8;
      expect(onTrack).toBe(true);
    });
  });

  describe('Date Calculations', () => {
    test('should calculate campaign duration in days', () => {
      const start = mockCampaign.dates.start.getTime();
      const end = mockCampaign.dates.end.getTime();
      const duration = (end - start) / (1000 * 60 * 60 * 24);
      expect(duration).toBe(366); // 2024 is a leap year
    });

    test('should calculate days elapsed', () => {
      const start = mockCampaign.dates.start.getTime();
      const now = new Date('2024-06-01').getTime();
      const daysElapsed = (now - start) / (1000 * 60 * 60 * 24);
      expect(daysElapsed).toBeGreaterThanOrEqual(150);
    });
  });

  describe('Metric Calculations', () => {
    test('should calculate CTR', () => {
      const ctr = (mockCampaign.metrics.clicks / mockCampaign.metrics.impressions) * 100;
      expect(ctr).toBe(5.0);
    });

    test('should calculate CPC', () => {
      const cpc = mockCampaign.metrics.cost / mockCampaign.metrics.clicks;
      expect(cpc).toBe(0.50);
    });

    test('should calculate CPA', () => {
      const cpa = mockCampaign.metrics.cost / mockCampaign.metrics.conversions;
      expect(cpa).toBe(25.0);
    });

    test('should calculate ROAS', () => {
      const roas = mockCampaign.metrics.revenue / mockCampaign.metrics.cost;
      expect(roas).toBe(3.0);
    });

    test('should calculate ROI', () => {
      const profit = mockCampaign.metrics.revenue - mockCampaign.metrics.cost;
      const roi = (profit / mockCampaign.metrics.cost) * 100;
      expect(roi).toBe(200);
    });
  });

  describe('Campaign Helpers', () => {
    test('should check if campaign is active', () => {
      const isActive = mockCampaign.status === CampaignStatus.ACTIVE;
      expect(isActive).toBe(true);
    });

    test('should check if campaign is within date range', () => {
      const now = new Date();
      const isWithinRange = now >= mockCampaign.dates.start && now <= mockCampaign.dates.end;
      expect(isWithinRange).toBe(true);
    });

    test('should check if campaign has remaining budget', () => {
      const hasBudget = mockCampaign.budget.remaining > 0;
      expect(hasBudget).toBe(true);
    });
  });

  describe('Multi-Country Support', () => {
    test('should handle multiple country targets', () => {
      expect(mockCampaign.countries).toBeDefined();
      expect(mockCampaign.countries.length).toBeGreaterThan(1);
    });

    test('should distribute budget across countries', () => {
      const usShare = mockCampaign.countries.find(c => c.countryCode === 'US')!.budget;
      const ukShare = mockCampaign.countries.find(c => c.countryCode === 'UK')!.budget;
      const total = usShare + ukShare;

      expect(total).toBeLessThan(mockCampaign.budget.total);
    });
  });

  describe('Tag and Objective Management', () => {
    test('should handle tags', () => {
      expect(mockCampaign.tags).toContain('brand-awareness');
      expect(mockCampaign.tags).toContain('q1-2024');
      expect(mockCampaign.tags).toContain('global');
    });

    test('should handle objectives', () => {
      expect(mockCampaign.objectives).toContain('Increase brand recognition');
      expect(mockCampaign.objectives).toContain('Drive website traffic');
    });
  });
});
