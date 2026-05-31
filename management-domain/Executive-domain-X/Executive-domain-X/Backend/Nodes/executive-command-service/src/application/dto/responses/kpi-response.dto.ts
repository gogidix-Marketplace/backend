export class KpiResponseDto {
  id: string;
  tenantId: string;
  name: string;
  category: string;
  executiveLevel: string;
  value: number;
  unit: string;
  period: string;
  target?: number;
  previousValue?: number;
  percentChange?: number;
  status: string;
  trend?: string;
  dataSources: string[];
  metadata: Record<string, unknown>;
  visible: boolean;
  isCalculated: boolean;
  lastCalculatedAt: Date;
  createdAt: Date;
  updatedAt: Date;
}
