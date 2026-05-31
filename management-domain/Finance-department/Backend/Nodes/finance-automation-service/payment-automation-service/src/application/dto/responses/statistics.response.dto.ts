export interface PaymentStatisticsResponseDto {
  totalPayments: number;
  totalAmount: number;
  currency: string;
  statusBreakdown: Record<string, number>;
  gatewayBreakdown: Record<string, number>;
  averageProcessingTime: number;
  successRate: number;
  period?: {
    startDate: Date;
    endDate: Date;
  };
}

export interface BatchStatisticsResponseDto {
  totalBatches: number;
  activeBatches: number;
  completedBatches: number;
  totalPaymentsProcessed: number;
  totalAmountProcessed: number;
  currency: string;
  period?: {
    startDate: Date;
    endDate: Date;
  };
}

export interface VendorPaymentStatisticsResponseDto {
  totalVendors: number;
  activeVendors: number;
  totalPaidAmount: number;
  currency: string;
  averagePaymentAmount: number;
  ratingDistribution: Record<string, number>;
}
