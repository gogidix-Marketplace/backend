export class SchedulePostDto {
  accountId!: string;
  platform!: 'twitter' | 'linkedin' | 'facebook' | 'instagram';
  content!: string;
  mediaUrls?: string[];
  hashtags?: string[];
  scheduledAt!: Date;
  metadata?: Record<string, any>;
}
export class UpdatePostDto {
  content?: string;
  mediaUrls?: string[];
  hashtags?: string[];
  scheduledAt?: Date;
  status?: string;
}
