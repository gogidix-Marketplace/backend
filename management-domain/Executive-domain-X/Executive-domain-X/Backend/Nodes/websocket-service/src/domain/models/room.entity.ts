import { RoomType } from '../enums/room-type.enum';

export interface RoomProps {
  id: string;
  tenantId: string;
  name: string;
  type: RoomType;
  createdBy: string | null;
  maxUsers: number;
  currentUsers: number;
  persistent: boolean;
  metadata: Record<string, unknown>;
  createdAt: string;
}

export class Room {
  readonly props: RoomProps;
  constructor(props: RoomProps) { this.props = { ...props, metadata: props.metadata || {} }; }
  isFull(): boolean { return this.props.currentUsers >= this.props.maxUsers; }
  incrementUsers() { this.props.currentUsers++; }
  decrementUsers() { this.props.currentUsers = Math.max(0, this.props.currentUsers - 1); }
}
