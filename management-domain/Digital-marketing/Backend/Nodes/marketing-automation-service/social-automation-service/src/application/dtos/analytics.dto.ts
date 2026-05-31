export class GetAnalyticsDto {
  accountId?: string;
  platform?: string;
  startDate?: Date;
  endDate?: Date;
  page?: number;
  limit?: number;
}
