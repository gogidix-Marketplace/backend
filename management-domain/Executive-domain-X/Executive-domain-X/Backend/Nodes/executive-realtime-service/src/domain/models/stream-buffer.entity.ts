export interface StreamBufferProps {
  roomId: string;
  buffer: any[];
  lastFlush: number;
  size: number;
}

export class StreamBuffer {
  readonly props: StreamBufferProps;
  private batchSize: number;
  private batchTimeout: number;
  private maxBufferSize: number;
  private flushTimer: ReturnType<typeof setTimeout> | null = null;

  constructor(roomId: string, options: { batchSize?: number; batchTimeout?: number; maxBufferSize?: number } = {}) {
    this.props = { roomId, buffer: [], lastFlush: Date.now(), size: 0 };
    this.batchSize = options.batchSize || 100;
    this.batchTimeout = options.batchTimeout || 100;
    this.maxBufferSize = options.maxBufferSize || 1000;
  }

  add(data: any): boolean {
    if (this.props.buffer.length >= this.maxBufferSize) { this.flush(); }
    this.props.buffer.push(data);
    this.props.size++;
    if (this.props.buffer.length >= this.batchSize) { this.flush(); return true; }
    if (!this.flushTimer) { this.flushTimer = setTimeout(() => this.flush(), this.batchTimeout); }
    return false;
  }

  flush(): any[] | null {
    if (this.flushTimer) { clearTimeout(this.flushTimer); this.flushTimer = null; }
    if (this.props.buffer.length === 0) return null;
    const data = [...this.props.buffer];
    this.props.buffer = [];
    this.props.size = 0;
    this.props.lastFlush = Date.now();
    return data;
  }

  clear() {
    if (this.flushTimer) { clearTimeout(this.flushTimer); this.flushTimer = null; }
    this.props.buffer = [];
    this.props.size = 0;
  }

  getStatus() { return { roomId: this.props.roomId, size: this.props.size, lastFlush: this.props.lastFlush }; }
}
