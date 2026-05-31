export interface IHealthQueryPort {
  healthCheck(): Promise<{
    mongodb: string;
    redis: string;
    knowledgeBase: string;
  }>;
  systemStatus(): Promise<any>;
  readinessCheck(): Promise<boolean>;
  livenessCheck(): boolean;
}
