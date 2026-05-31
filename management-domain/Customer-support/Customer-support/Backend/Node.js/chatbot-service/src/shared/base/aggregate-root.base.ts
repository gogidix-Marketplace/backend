import { BaseEntity } from './entity.base';

export abstract class AggregateRoot extends BaseEntity {
  private _domainEvents: any[] = [];

  constructor(id: string, createdAt?: Date, updatedAt?: Date) {
    super(id, createdAt, updatedAt);
  }

  get domainEvents(): any[] {
    return [...this._domainEvents];
  }

  protected addDomainEvent(event: any): void {
    this._domainEvents.push(event);
  }

  clearDomainEvents(): void {
    this._domainEvents = [];
  }
}
