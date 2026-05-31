
/**
 * Base entity class
 * Provides common properties and methods for all domain entities
 */
export abstract class BaseEntity {
  protected _id?: string;
  protected createdAt: Date;
  protected updatedAt: Date;
  protected version: number;

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

  /**
   * Mark entity as updated
   * Increments version and updates timestamp
   */
  protected markAsUpdated(): void {
    this.updatedAt = new Date();
    this.version++;
  }

  /**
   * Check if entity is new (not persisted)
   */
  isNew(): boolean {
    return !this._id;
  }

  /**
   * Get unique identifier as string
   */
  getIdAsString(): string {
    return this._id?.toString() || '';
  }

  /**
   * Check version for optimistic locking
   */
  hasVersion(expectedVersion: number): boolean {
    return this.version === expectedVersion;
  }
}
