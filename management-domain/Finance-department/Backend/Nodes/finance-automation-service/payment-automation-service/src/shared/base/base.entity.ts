import { v4 as uuidv4 } from 'uuid';

export abstract class BaseEntity {
  id: string;
  createdAt: Date;
  updatedAt: Date;
  tenantId: string;
  version: number;

  protected constructor(tenantId?: string) {
    this.id = uuidv4();
    this.createdAt = new Date();
    this.updatedAt = new Date();
    this.tenantId = tenantId || 'default';
    this.version = 1;
  }

  updateTimestamp(): void {
    this.updatedAt = new Date();
    this.version++;
  }

  equals(other: BaseEntity): boolean {
    if (other === this) return true;
    if (other === null || other === undefined) return false;
    return this.id === other.id;
  }
}
