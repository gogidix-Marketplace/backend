import { apiClient } from './api';
import { AnalyticsData, AnalyticsPeriod } from '@/types';

export const getAnalyticsOverview = async (period: AnalyticsPeriod): Promise<AnalyticsData> => {
  return apiClient.get('/analytics/overview', { params: { period } });
};

export const getVisitorAnalytics = async (period: AnalyticsPeriod): Promise<any> => {
  return apiClient.get('/analytics/visitors', { params: { period } });
};

export const getContentAnalytics = async (period: AnalyticsPeriod): Promise<any> => {
  return apiClient.get('/analytics/content', { params: { period } });
};

export const getConversionAnalytics = async (period: AnalyticsPeriod): Promise<any> => {
  return apiClient.get('/analytics/conversions', { params: { period } });
};

export const getSeoAnalytics = async (period: AnalyticsPeriod): Promise<any> => {
  return apiClient.get('/analytics/seo', { params: { period } });
};

export const getFunnelAnalytics = async (funnelId: string, period: AnalyticsPeriod): Promise<any> => {
  return apiClient.get(`/analytics/funnels/${funnelId}`, { params: { period } });
};
