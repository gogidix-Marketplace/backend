export interface PostMetrics {
  likes: number;
  comments: number;
  shares: number;
  impressions: number;
  clicks: number;
  engagementRate: number;
}

export interface ScheduledPostProps {
  id?: string;
  tenantId: string;
  accountId: string;
  platform: 'twitter' | 'linkedin' | 'facebook' | 'instagram';
  content: string;
  mediaUrls?: string[];
  hashtags?: string[];
  scheduledAt: Date;
  publishedAt?: Date;
  status: 'draft' | 'scheduled' | 'publishing' | 'published' | 'failed';
  platformPostId?: string;
  platformPostUrl?: string;
  failureReason?: string;
  metrics?: PostMetrics;
  metadata?: Record<string, any>;
  retryCount: number;
  maxRetries: number;
  createdAt?: Date;
  updatedAt?: Date;
}

export class ScheduledPost {
  private readonly props: ScheduledPostProps;

  constructor(props: ScheduledPostProps) {
    this.props = {
      ...props,
      status: props.status ?? 'draft',
      retryCount: props.retryCount ?? 0,
      maxRetries: props.maxRetries ?? 3,
      mediaUrls: props.mediaUrls ?? [],
      hashtags: props.hashtags ?? [],
      createdAt: props.createdAt ?? new Date(),
      updatedAt: new Date(),
    };
  }

  get id(): string | undefined { return this.props.id; }
  get tenantId(): string { return this.props.tenantId; }
  get accountId(): string { return this.props.accountId; }
  get platform(): string { return this.props.platform; }
  get content(): string { return this.props.content; }
  get mediaUrls(): string[] | undefined { return this.props.mediaUrls; }
  get hashtags(): string[] | undefined { return this.props.hashtags; }
  get scheduledAt(): Date { return this.props.scheduledAt; }
  get publishedAt(): Date | undefined { return this.props.publishedAt; }
  get status(): string { return this.props.status; }
  get platformPostId(): string | undefined { return this.props.platformPostId; }
  get platformPostUrl(): string | undefined { return this.props.platformPostUrl; }
  get failureReason(): string | undefined { return this.props.failureReason; }
  get metrics(): PostMetrics | undefined { return this.props.metrics; }
  get metadata(): Record<string, any> | undefined { return this.props.metadata; }
  get retryCount(): number { return this.props.retryCount; }
  get maxRetries(): number { return this.props.maxRetries; }
  get createdAt(): Date | undefined { return this.props.createdAt; }
  get updatedAt(): Date | undefined { return this.props.updatedAt; }

  schedule(): void { this.props.status = 'scheduled'; this.props.updatedAt = new Date(); }
  markAsPublishing(): void { this.props.status = 'publishing'; this.props.updatedAt = new Date(); }
  markAsPublished(platformPostId: string, platformPostUrl?: string): void { this.props.status = 'published'; this.props.platformPostId = platformPostId; this.props.platformPostUrl = platformPostUrl; this.props.publishedAt = new Date(); this.props.updatedAt = new Date(); }
  markAsFailed(reason: string): void { this.props.retryCount += 1; this.props.failureReason = reason; this.props.status = this.props.retryCount >= this.props.maxRetries ? 'failed' : 'scheduled'; this.props.updatedAt = new Date(); }
  updateMetrics(metrics: PostMetrics): void { this.props.metrics = metrics; this.props.updatedAt = new Date(); }
  isReadyToPublish(): boolean { return this.props.status === 'scheduled' && this.props.scheduledAt <= new Date(); }
  canRetry(): boolean { return this.props.retryCount < this.props.maxRetries; }

  toPlainObject(): ScheduledPostProps { return { ...this.props }; }
}
