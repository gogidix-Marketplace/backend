export interface IScalingPolicyCommand {
  createPolicy(data: any): Promise<any>;
  updatePolicy(id: string, data: any): Promise<any>;
  deletePolicy(id: string): Promise<void>;
  enablePolicy(id: string): Promise<any>;
  disablePolicy(id: string): Promise<any>;
}
