export interface ICachePort {
  get(key: string): Promise<string | null>;
  set(key: string, value: string, expirySeconds?: number): Promise<boolean>;
  del(key: string): Promise<boolean>;
  exists(key: string): Promise<boolean>;
  incr(key: string): Promise<number | null>;
  expire(key: string, seconds: number): Promise<boolean>;
}
