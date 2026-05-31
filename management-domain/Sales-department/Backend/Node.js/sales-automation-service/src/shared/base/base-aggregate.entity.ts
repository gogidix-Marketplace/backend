import { BaseEntity } from './base.entity';

export abstract class BaseAggregateRoot extends BaseEntity {
  private _domainEvents: Array<any> = [];

  get domainEvents(): Array<any> {
    return [...this._domainEvents];
  }

  addDomainEvent(event: any): void {
    this._domainEvents.push(event);
  }

  clearDomainEvents(): void {
    this._domainEvents = [];
  }
}
