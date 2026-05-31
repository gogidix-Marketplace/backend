export interface EntityProps {
  type: string;
  value: string;
  confidence: number;
  start: number;
  end: number;
  resolved?: boolean;
}
