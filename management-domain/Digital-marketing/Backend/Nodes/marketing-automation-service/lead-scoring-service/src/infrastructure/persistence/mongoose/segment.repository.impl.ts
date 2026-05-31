import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { ISegmentRepository } from '../../../domain/ports/repositories/segment.repository';
import { Segment } from '../../../domain/models/segment';
import { SegmentDocument } from '../schemas/segment.schema';

@Injectable()
export class SegmentRepositoryImpl implements ISegmentRepository {
  constructor(@InjectModel(SegmentDocument.name) private readonly model: Model<SegmentDocument>) {}

  async save(segment: Segment): Promise<Segment> {
    const props = segment.toPlainObject();
    const doc = new this.model(props);
    const saved = await doc.save();
    return new Segment({ ...saved.toObject(), id: saved._id.toString() });
  }

  async findById(id: string): Promise<Segment | null> {
    const doc = await this.model.findById(id).exec();
    if (!doc) return null;
    return new Segment({ ...doc.toObject(), id: doc._id.toString() });
  }

  async findByTenantId(tenantId: string): Promise<Segment[]> {
    const docs = await this.model.find({ tenantId }).sort({ name: 1 }).exec();
    return docs.map(doc => new Segment({ ...doc.toObject(), id: doc._id.toString() }));
  }

  async update(segment: Segment): Promise<Segment> {
    const props = segment.toPlainObject();
    const doc = await this.model.findByIdAndUpdate(props.id, props, { new: true }).exec();
    if (!doc) return segment;
    return new Segment({ ...doc.toObject(), id: doc._id.toString() });
  }

  async delete(id: string): Promise<boolean> {
    const result = await this.model.findByIdAndDelete(id).exec();
    return !!result;
  }
}
