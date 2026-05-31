export interface ICacheService {
  invalidatePattern(pattern: string): Promise<void>;
  set(key: string, value: unknown, ttl?: number): Promise<void>;
  get(key: string): Promise<unknown | null>;
  del(key: string): Promise<void>;
}
