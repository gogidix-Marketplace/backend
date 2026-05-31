import { Process, Processor } from '@nestjs/bull';
import { Logger, Inject} from '@nestjs/common';
import { Job } from 'bull';
import { PayrollCalculationService } from '@application/services/payroll-calculation.service';
import { IEventPublisher } from '@domain/ports/output/event-publisher.interface';
import { PayrollCompletedEvent } from '@domain/events/payroll-completed.event';
import { EmployeePayrollCalculatedEvent } from '@domain/events/employee-payroll-calculated.event';

@Processor('payroll-processing')
export class PayrollProcessorService {
  private readonly logger = new Logger(PayrollProcessorService.name);

  constructor(
    private readonly payrollCalculationService: PayrollCalculationService,
    @Inject('IEventPublisher')
    private readonly eventPublisher: IEventPublisher,
  ) {}

  @Process('process-payroll')
  async handlePayrollProcessing(job: Job): Promise<any> {
    const { tenantId, payrollId, employeeIds, periodStart, periodEnd } = job.data;
    this.logger.log(`Processing payroll job ${job.id} for ${employeeIds.length} employees`);

    const results = [];
    let totalGross = 0;
    let totalNet = 0;

    for (let i = 0; i < employeeIds.length; i++) {
      const employeeId = employeeIds[i];
      const calculation = this.payrollCalculationService.calculateEmployeePayroll(
        payrollId, employeeId, tenantId, 5000,
      );

      results.push(calculation);
      totalGross += calculation.grossEarnings;
      totalNet += calculation.netPay;

      await job.progress(Math.round(((i + 1) / employeeIds.length) * 100));

      await this.eventPublisher.publish(new EmployeePayrollCalculatedEvent(
        payrollId, employeeId, tenantId, calculation.grossEarnings, calculation.netPay, calculation.currency,
      ));
    }

    await this.eventPublisher.publish(new PayrollCompletedEvent(
      payrollId, tenantId, results.length, totalGross, totalNet, 'USD',
    ));

    this.logger.log(`Completed payroll job ${job.id}: ${results.length} employees processed`);
    return { success: true, processedCount: results.length };
  }
}
