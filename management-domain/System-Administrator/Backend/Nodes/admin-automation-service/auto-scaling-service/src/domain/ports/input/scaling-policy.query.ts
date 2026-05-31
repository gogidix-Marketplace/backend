export interface IScalingPolicyQuery {
  getPolicies(filters?: any): Promise<{ data: any[]; total: number }>;
  getPolicyById(id: string): Promise<any>;
  getEvents(policyId?: string, limit?: number): Promise<any[]>;
  getMetricsHistory(resourceId: string, hours?: number): Promise<any[]>;
  getCurrentMetrics(resourceId: string): Promise<any>;
}
