export interface IPubSubService {
  subscribe(channel: string, handler: (message: any, channel: string) => void): Promise<void>;
  unsubscribe(channel: string): Promise<void>;
  publish(channel: string, message: any): Promise<void>;
}
