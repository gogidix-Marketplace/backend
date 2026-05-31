import { KpiCategory } from '../enums/kpi-category.enum';
import { KpiStatus } from '../enums/kpi-status.enum';
import { ExecutiveLevel } from '../enums/executive-level.enum';
import { KpiTrend } from '../enums/kpi-trend.enum';

export interface KpiProps {
  id?: string;
  tenantId: string;
  name: string;
  category: KpiCategory;
  executiveLevel: ExecutiveLevel;
  value: number;
  unit: string;
  period: string;
  target?: number;
  previousValue?: number;
  percentChange?: number;
  status: KpiStatus;
  trend?: KpiTrend;
  dataSources: string[];
  metadata: Record<string, unknown>;
  visible: boolean;
  isCalculated: boolean;
  lastCalculatedAt: Date;
  createdAt: Date;
  updatedAt: Date;
}

export class Kpi {
  readonly props: KpiProps;
  constructor(props: KpiProps) {
    this.props = {
      ...props,
      id: props.id || crypto.randomUUID(),
      executiveLevel: props.executiveLevel || ExecutiveLevel.ALL,
      status: props.status || KpiStatus.ON_TRACK,
      dataSources: props.dataSources || [],
      metadata: props.metadata || {},
      visible: props.visible !== false,
      isCalculated: props.isCalculated !== false,
      lastCalculatedAt: props.lastCalculatedAt || new Date(),
      createdAt: props.createdAt || new Date(),
      updatedAt: props.updatedAt || new Date(),
    };
  }
  get id(): string { return this.props.id!; }
  get tenantId(): string { return this.props.tenantId; }
  get name(): string { return this.props.name; }
  get status(): KpiStatus { return this.props.status; }
  toJSON() {
    return { id: this.props.id, tenantId: this.props.tenantId, name: this.props.name, category: this.props.category, executiveLevel: this.props.executiveLevel, value: this.props.value, unit: this.props.unit, period: this.props.period, target: this.props.target, previousValue: this.props.previousValue, percentChange: this.props.percentChange, status: this.props.status, trend: this.props.trend, dataSources: this.props.dataSources, metadata: this.props.metadata, visible: this.props.visible, isCalculated: this.props.isCalculated, lastCalculatedAt: this.props.lastCalculatedAt, createdAt: this.props.createdAt, updatedAt: this.props.updatedAt };
  }
}
