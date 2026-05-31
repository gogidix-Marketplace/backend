export interface INotificationService {
  notifyScalingEvent(event: any): Promise<void>;
  notifyPolicyAlert(policyName: string, message: string): Promise<void>;
}
