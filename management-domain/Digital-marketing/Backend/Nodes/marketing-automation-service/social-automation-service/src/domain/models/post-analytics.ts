export interface DailyMetrics {
  date: Date;
  likes: number;
  comments: number;
  shares: number;
  impressions: number;
  clicks: number;
  followersGained: number;
  followersLost: number;
  engagementRate: number;
}

export interface PostAnalyticsProps {
  id?: string;
  tenantId: string;
  accountId: string;
  postId?: string;
  date: Date;
  platform: 'twitter' | 'linkedin' | 'facebook' | 'instagram';
  metrics: DailyMetrics;
  createdAt?: Date;
  updatedAt?: Date;
}

export class PostAnalytics {
  private readonly props: PostAnalyticsProps;

  constructor(props: PostAnalyticsProps) {
    this.props = { ...props, metrics: props.metrics ?? { date: props.date, likes: 0, comments: 0, shares: 0, impressions: 0, clicks: 0, followersGained: 0, followersLost: 0, engagementRate: 0 }, createdAt: props.createdAt ?? new Date(), updatedAt: new Date() };
  }

  get id(): string | undefined { return this.props.id; }
  get tenantId(): string { return this.props.tenantId; }
  get accountId(): string { return this.props.accountId; }
  get postId(): string | undefined { return this.props.postId; }
  get date(): Date { return this.props.date; }
  get platform(): string { return this.props.platform; }
  get metrics(): DailyMetrics { return this.props.metrics; }
  get createdAt(): Date | undefined { return this.props.createdAt; }
  get updatedAt(): Date | undefined { return this.props.updatedAt; }

  updateMetrics(metrics: Partial<DailyMetrics>): void {
    this.props.metrics = { ...this.props.metrics, ...metrics };
    this.props.updatedAt = new Date();
  }

  aggregate(other: PostAnalytics): PostAnalytics {
    const combined = { ...this.props.metrics };
    combined.likes += other.metrics.likes;
    combined.comments += other.metrics.comments;
    combined.shares += other.metrics.shares;
    combined.impressions += other.metrics.impressions;
    combined.clicks += other.metrics.clicks;
    combined.followersGained += other.metrics.followersGained;
    combined.followersLost += other.metrics.followersLost;
    combined.engagementRate = combined.impressions > 0 ? ((combined.likes + combined.comments + combined.shares + combined.clicks) / combined.impressions) * 100 : 0;
    return new PostAnalytics({ ...this.props, metrics: combined });
  }

  toPlainObject(): PostAnalyticsProps { return { ...this.props }; }
}
