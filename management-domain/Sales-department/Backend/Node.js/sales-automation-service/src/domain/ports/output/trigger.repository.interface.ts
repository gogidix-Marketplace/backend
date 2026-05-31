import { Trigger } from '../../models/trigger.entity';

export interface TriggerRepository {
  save(trigger: Trigger): Promise<Trigger>;
  findById(id: string): Promise<Trigger | null>;
  findByTenantId(tenantId: string): Promise<Trigger[]>;
  findActiveByTenantId(tenantId: string): Promise<Trigger[]>;
  findByType(tenantId: string, type: string): Promise<Trigger[]>;
  findAll(filters?: any, tenantId?: string): Promise<Trigger[]>;
  delete(id: string): Promise<void>;
  exists(id: string): Promise<boolean>;
}
