export interface KpiCreatedEvent {
  kpiId: string;
  tenantId: string;
  name: string;
  category: string;
  executiveLevel: string;
  correlationId?: string;
}

export interface KpiUpdatedEvent {
  kpiId: string;
  tenantId: string;
  name: string;
  value: number;
  status: string;
  executiveLevel: string;
  category: string;
  correlationId?: string;
}

export interface KpiDeletedEvent {
  kpiId: string;
  tenantId: string;
  correlationId?: string;
}

export interface KpiRecalculatedEvent {
  kpiId: string;
  tenantId: string;
  name: string;
  value: number;
  previousValue: number;
  percentChange: number | null;
  status: string;
  executiveLevel: string;
  category: string;
  correlationId?: string;
}

export interface IEventPublisher {
  publishKpiCreated(event: KpiCreatedEvent): Promise<void>;
  publishKpiUpdated(event: KpiUpdatedEvent): Promise<void>;
  publishKpiDeleted(event: KpiDeletedEvent): Promise<void>;
  publishKpiRecalculated(event: KpiRecalculatedEvent): Promise<void>;
}
