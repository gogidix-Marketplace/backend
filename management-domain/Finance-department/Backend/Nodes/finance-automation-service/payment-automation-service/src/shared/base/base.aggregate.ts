import { BaseEntity } from './base.entity';

export abstract class BaseAggregate<T extends BaseEntity> {
  protected root: T;
  protected events: any[] = [];

  protected constructor(root: T) {
    this.root = root;
  }

  getId(): string {
    return this.root.id;
  }

  getVersion(): number {
    return this.root.version;
  }

  getTenantId(): string {
    return this.root.tenantId;
  }

  pullEvents(): any[] {
    const events = [...this.events];
    this.events = [];
    return events;
  }

  protected addEvent(event: any): void {
    this.events.push(event);
  }

  getRoot(): T {
    return this.root;
  }
}
