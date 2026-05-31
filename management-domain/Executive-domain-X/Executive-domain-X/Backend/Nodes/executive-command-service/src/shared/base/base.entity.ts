import { v4 as uuidv4 } from 'uuid';

export abstract class BaseEntity {
  readonly id: string;
  readonly createdAt: Date;
  readonly updatedAt: Date;

  constructor(props?: { id?: string; createdAt?: Date; updatedAt?: Date }) {
    this.id = props?.id || uuidv4();
    this.createdAt = props?.createdAt || new Date();
    this.updatedAt = props?.updatedAt || new Date();
  }
}

export abstract class AggregateRoot extends BaseEntity {
  private _domainEvents: any[] = [];

  get domainEvents(): any[] {
    return this._domainEvents;
  }

  addDomainEvent(event: any): void {
    this._domainEvents.push(event);
  }

  clearDomainEvents(): void {
    this._domainEvents = [];
  }
}

export abstract class ValueObject {
  public equals(other?: ValueObject): boolean {
    if (other === null || other === undefined) {
      return false;
    }
    if (!(other instanceof this.constructor)) {
      return false;
    }
    return JSON.stringify(this) === JSON.stringify(other);
  }
}
