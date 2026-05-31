import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { Payment, PaymentStatus } from '../../../domain/models';
import { PaymentRepository as IPaymentRepository } from '../../../domain/repositories';
import {
  PaymentDocument,
  PaymentBatchDocument,
  PaymentRuleDocument,
  VendorPaymentDocument,
} from './payment.schema';
import { PaymentBatch } from '../../../domain/models/payment-batch.entity';
import { PaymentRule } from '../../../domain/models/payment-rule.entity';
import { VendorPayment } from '../../../domain/models/vendor-payment.entity';

@Injectable()
export class MongoPaymentRepository implements IPaymentRepository {
  constructor(
    @InjectModel(PaymentDocument.name)
    private readonly paymentModel: Model<PaymentDocument>,
  ) {}

  async save(payment: Payment): Promise<Payment> {
    const json = payment.toJSON();
    const document = await this.paymentModel.findOneAndUpdate(
      { id: payment.id },
      json,
      { upsert: true, new: true },
    );
    return Payment.fromJSON(document.toJSON());
  }

  async findById(id: string): Promise<Payment | null> {
    const document = await this.paymentModel.findOne({ id });
    return document ? Payment.fromJSON(document.toJSON()) : null;
  }

  async findByTenantId(tenantId: string): Promise<Payment[]> {
    const documents = await this.paymentModel.find({ tenantId });
    return documents.map((d) => Payment.fromJSON(d.toJSON()));
  }

  async findByVendorId(vendorId: string): Promise<Payment[]> {
    const documents = await this.paymentModel.find({ vendorId });
    return documents.map((d) => Payment.fromJSON(d.toJSON()));
  }

  async findByBatchId(batchId: string): Promise<Payment[]> {
    const documents = await this.paymentModel.find({ batchId });
    return documents.map((d) => Payment.fromJSON(d.toJSON()));
  }

  async findByStatus(status: string, tenantId?: string): Promise<Payment[]> {
    const filter: any = { status };
    if (tenantId) {
      filter.tenantId = tenantId;
    }
    const documents = await this.paymentModel.find(filter);
    return documents.map((d) => Payment.fromJSON(d.toJSON()));
  }

  async findPending(tenantId?: string): Promise<Payment[]> {
    return this.findByStatus(PaymentStatus.PENDING, tenantId);
  }

  async findScheduled(date?: Date, tenantId?: string): Promise<Payment[]> {
    const filter: any = {
      status: PaymentStatus.SCHEDULED,
    };
    if (date) {
      filter.scheduledAt = { $lte: date };
    }
    if (tenantId) {
      filter.tenantId = tenantId;
    }
    const documents = await this.paymentModel.find(filter).sort({ scheduledAt: 1 });
    return documents.map((d) => Payment.fromJSON(d.toJSON()));
  }

  async findFailed(retryable = false, tenantId?: string): Promise<Payment[]> {
    const filter: any = { status: PaymentStatus.FAILED };
    if (retryable) {
      filter.retryCount = { $lt: '$maxRetries' };
    }
    if (tenantId) {
      filter.tenantId = tenantId;
    }
    const documents = await this.paymentModel.find(filter);
    return documents.map((d) => Payment.fromJSON(d.toJSON()));
  }

  async findAll(filters: any = {}, tenantId?: string): Promise<Payment[]> {
    const filter: any = { ...filters };
    if (tenantId) {
      filter.tenantId = tenantId;
    }
    const documents = await this.paymentModel.find(filter);
    return documents.map((d) => Payment.fromJSON(d.toJSON()));
  }

  async delete(id: string): Promise<void> {
    await this.paymentModel.deleteOne({ id });
  }

  async exists(id: string): Promise<boolean> {
    const count = await this.paymentModel.countDocuments({ id });
    return count > 0;
  }

  async count(filters: any = {}, tenantId?: string): Promise<number> {
    const filter: any = { ...filters };
    if (tenantId) {
      filter.tenantId = tenantId;
    }
    return this.paymentModel.countDocuments(filter);
  }
}

@Injectable()
export class MongoPaymentBatchRepository {
  constructor(
    @InjectModel(PaymentBatchDocument.name)
    private readonly batchModel: Model<PaymentBatchDocument>,
  ) {}

  async save(batch: PaymentBatch): Promise<PaymentBatch> {
    const json = batch.toJSON();
    const document = await this.batchModel.findOneAndUpdate(
      { id: batch.id },
      json,
      { upsert: true, new: true },
    );
    return PaymentBatch.fromJSON(document.toJSON());
  }

  async findById(id: string): Promise<PaymentBatch | null> {
    const document = await this.batchModel.findOne({ id });
    return document ? PaymentBatch.fromJSON(document.toJSON()) : null;
  }

  async findByTenantId(tenantId: string): Promise<PaymentBatch[]> {
    const documents = await this.batchModel.find({ tenantId });
    return documents.map((d) => PaymentBatch.fromJSON(d.toJSON()));
  }

  async findByStatus(status: string, tenantId?: string): Promise<PaymentBatch[]> {
    const filter: any = { status };
    if (tenantId) {
      filter.tenantId = tenantId;
    }
    const documents = await this.batchModel.find(filter);
    return documents.map((d) => PaymentBatch.fromJSON(d.toJSON()));
  }

  async findPending(tenantId?: string): Promise<PaymentBatch[]> {
    return this.findByStatus('PENDING', tenantId);
  }

  async findProcessing(tenantId?: string): Promise<PaymentBatch[]> {
    return this.findByStatus('PROCESSING', tenantId);
  }

  async findScheduled(date?: Date, tenantId?: string): Promise<PaymentBatch[]> {
    const filter: any = { status: 'PENDING' };
    if (date) {
      filter.scheduledAt = { $lte: date };
    }
    if (tenantId) {
      filter.tenantId = tenantId;
    }
    const documents = await this.batchModel.find(filter).sort({ scheduledAt: 1 });
    return documents.map((d) => PaymentBatch.fromJSON(d.toJSON()));
  }

  async findAll(filters: any = {}, tenantId?: string): Promise<PaymentBatch[]> {
    const filter: any = { ...filters };
    if (tenantId) {
      filter.tenantId = tenantId;
    }
    const documents = await this.batchModel.find(filter);
    return documents.map((d) => PaymentBatch.fromJSON(d.toJSON()));
  }

  async delete(id: string): Promise<void> {
    await this.batchModel.deleteOne({ id });
  }

  async exists(id: string): Promise<boolean> {
    const count = await this.batchModel.countDocuments({ id });
    return count > 0;
  }

  async count(filters: any = {}, tenantId?: string): Promise<number> {
    const filter: any = { ...filters };
    if (tenantId) {
      filter.tenantId = tenantId;
    }
    return this.batchModel.countDocuments(filter);
  }
}

@Injectable()
export class MongoPaymentRuleRepository {
  constructor(
    @InjectModel(PaymentRuleDocument.name)
    private readonly ruleModel: Model<PaymentRuleDocument>,
  ) {}

  async save(rule: PaymentRule): Promise<PaymentRule> {
    const json = rule.toJSON();
    const document = await this.ruleModel.findOneAndUpdate(
      { id: rule.id },
      json,
      { upsert: true, new: true },
    );
    return PaymentRule.fromJSON(document.toJSON());
  }

  async findById(id: string): Promise<PaymentRule | null> {
    const document = await this.ruleModel.findOne({ id });
    return document ? PaymentRule.fromJSON(document.toJSON()) : null;
  }

  async findByTenantId(tenantId: string): Promise<PaymentRule[]> {
    const documents = await this.ruleModel.find({ tenantId });
    return documents.map((d) => PaymentRule.fromJSON(d.toJSON()));
  }

  async findActive(tenantId?: string): Promise<PaymentRule[]> {
    const filter: any = { isActive: true };
    if (tenantId) {
      filter.tenantId = tenantId;
    }
    const documents = await this.ruleModel.find(filter).sort({ priority: -1 });
    return documents.map((d) => PaymentRule.fromJSON(d.toJSON()));
  }

  async findByType(ruleType: string, tenantId?: string): Promise<PaymentRule[]> {
    const filter: any = { ruleType };
    if (tenantId) {
      filter.tenantId = tenantId;
    }
    const documents = await this.ruleModel.find(filter);
    return documents.map((d) => PaymentRule.fromJSON(d.toJSON()));
  }

  async findAll(filters: any = {}, tenantId?: string): Promise<PaymentRule[]> {
    const filter: any = { ...filters };
    if (tenantId) {
      filter.tenantId = tenantId;
    }
    const documents = await this.ruleModel.find(filter);
    return documents.map((d) => PaymentRule.fromJSON(d.toJSON()));
  }

  async delete(id: string): Promise<void> {
    await this.ruleModel.deleteOne({ id });
  }

  async exists(id: string): Promise<boolean> {
    const count = await this.ruleModel.countDocuments({ id });
    return count > 0;
  }

  async count(filters: any = {}, tenantId?: string): Promise<number> {
    const filter: any = { ...filters };
    if (tenantId) {
      filter.tenantId = tenantId;
    }
    return this.ruleModel.countDocuments(filter);
  }
}

@Injectable()
export class MongoVendorPaymentRepository {
  constructor(
    @InjectModel(VendorPaymentDocument.name)
    private readonly vendorPaymentModel: Model<VendorPaymentDocument>,
  ) {}

  async save(vendorPayment: VendorPayment): Promise<VendorPayment> {
    const json = vendorPayment.toJSON();
    const document = await this.vendorPaymentModel.findOneAndUpdate(
      { id: vendorPayment.id },
      json,
      { upsert: true, new: true },
    );
    return VendorPayment.fromJSON(document.toJSON());
  }

  async findById(id: string): Promise<VendorPayment | null> {
    const document = await this.vendorPaymentModel.findOne({ id });
    return document ? VendorPayment.fromJSON(document.toJSON()) : null;
  }

  async findByVendorId(vendorId: string): Promise<VendorPayment | null> {
    const document = await this.vendorPaymentModel.findOne({ vendorId });
    return document ? VendorPayment.fromJSON(document.toJSON()) : null;
  }

  async findByTenantId(tenantId: string): Promise<VendorPayment[]> {
    const documents = await this.vendorPaymentModel.find({ tenantId });
    return documents.map((d) => VendorPayment.fromJSON(d.toJSON()));
  }

  async findActive(tenantId?: string): Promise<VendorPayment[]> {
    const filter: any = { isActive: true };
    if (tenantId) {
      filter.tenantId = tenantId;
    }
    const documents = await this.vendorPaymentModel.find(filter);
    return documents.map((d) => VendorPayment.fromJSON(d.toJSON()));
  }

  async findByAutoPayEnabled(tenantId?: string): Promise<VendorPayment[]> {
    const filter: any = {
      'paymentSettings.autoPayEnabled': true,
      isActive: true,
    };
    if (tenantId) {
      filter.tenantId = tenantId;
    }
    const documents = await this.vendorPaymentModel.find(filter);
    return documents.map((d) => VendorPayment.fromJSON(d.toJSON()));
  }

  async findByVendorCode(vendorCode: string, tenantId?: string): Promise<VendorPayment | null> {
    const filter: any = { vendorCode };
    if (tenantId) {
      filter.tenantId = tenantId;
    }
    const document = await this.vendorPaymentModel.findOne(filter);
    return document ? VendorPayment.fromJSON(document.toJSON()) : null;
  }

  async findAll(filters: any = {}, tenantId?: string): Promise<VendorPayment[]> {
    const filter: any = { ...filters };
    if (tenantId) {
      filter.tenantId = tenantId;
    }
    const documents = await this.vendorPaymentModel.find(filter);
    return documents.map((d) => VendorPayment.fromJSON(d.toJSON()));
  }

  async delete(id: string): Promise<void> {
    await this.vendorPaymentModel.deleteOne({ id });
  }

  async exists(id: string): Promise<boolean> {
    const count = await this.vendorPaymentModel.countDocuments({ id });
    return count > 0;
  }

  async count(filters: any = {}, tenantId?: string): Promise<number> {
    const filter: any = { ...filters };
    if (tenantId) {
      filter.tenantId = tenantId;
    }
    return this.vendorPaymentModel.countDocuments(filter);
  }
}
