
/**
 * Base entity class for all domain entities
 * Provides common properties and methods
 */
export abstract class BaseEntity {
  public _id?: string;
  protected createdAt: Date;
  protected updatedAt: Date;
  protected version: number;
  protected deletedAt?: Date;

  constructor() {
    this.createdAt = new Date();
    this.updatedAt = new Date();
    this.version = 0;
  }

  get id(): string | undefined {
    return this._id;
  }

  set id(value: string | undefined) {
    this._id = value;
  }

  get createdAtDate(): Date {
    return this.createdAt;
  }

  get updatedAtDate(): Date {
    return this.updatedAt;
  }

  get getVersion(): number {
    return this.version;
  }

  getVersionValue(): number {
    return this.version;
  }

  markAsUpdated(): void {
    this.updatedAt = new Date();
    this.version++;
  }

  softDelete(): void {
    this.deletedAt = new Date();
    this.markAsUpdated();
  }

  isDeleted(): boolean {
    return this.deletedAt !== undefined;
  }
}
