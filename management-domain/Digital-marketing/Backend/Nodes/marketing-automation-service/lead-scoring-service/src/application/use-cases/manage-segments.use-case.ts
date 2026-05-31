import { Injectable, Inject } from '@nestjs/common';
import { Segment } from '../../domain/models/segment';
import { ISegmentRepository, SEGMENT_REPOSITORY } from '../../domain/ports/repositories/segment.repository';
import { ILeadScoreRepository, LEAD_SCORE_REPOSITORY } from '../../domain/ports/repositories/lead-score.repository';
import { CreateSegmentDto, UpdateSegmentDto } from '../dtos/segment.dto';
import { SegmentNotFoundException } from '../../domain/exceptions/domain.exceptions';

@Injectable()
export class ManageSegmentsUseCase {
  constructor(
    @Inject(SEGMENT_REPOSITORY) private readonly segmentRepo: ISegmentRepository,
    @Inject(LEAD_SCORE_REPOSITORY) private readonly scoreRepo: ILeadScoreRepository,
  ) {}

  async create(dto: CreateSegmentDto, tenantId: string): Promise<Segment> {
    const segment = new Segment({ tenantId, name: dto.name, description: dto.description, criteria: dto.criteria, criteriaLogic: dto.criteriaLogic ?? 'and', minScore: dto.minScore, maxScore: dto.maxScore, autoAssign: dto.autoAssign ?? false });
    return this.segmentRepo.save(segment);
  }

  async findAll(tenantId: string): Promise<Segment[]> {
    return this.segmentRepo.findByTenantId(tenantId);
  }

  async findById(id: string): Promise<Segment> {
    const segment = await this.segmentRepo.findById(id);
    if (!segment) throw new SegmentNotFoundException(id);
    return segment;
  }

  async update(id: string, dto: UpdateSegmentDto): Promise<Segment> {
    const segment = await this.findById(id);
    const updated = new Segment({ ...segment.toPlainObject(), ...dto, updatedAt: new Date() });
    return this.segmentRepo.update(updated);
  }

  async delete(id: string): Promise<boolean> {
    return this.segmentRepo.delete(id);
  }

  async findMatchingSegments(leadData: Record<string, any>, score: number, tenantId: string): Promise<Segment[]> {
    const segments = await this.segmentRepo.findByTenantId(tenantId);
    return segments.filter(s => s.autoAssign && s.matches(leadData, score));
  }
}
