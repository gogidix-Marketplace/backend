import { Segment } from '../../models/segment';
export const SEGMENT_REPOSITORY = Symbol('SEGMENT_REPOSITORY');
export interface ISegmentRepository {
  save(segment: Segment): Promise<Segment>;
  findById(id: string): Promise<Segment | null>;
  findByTenantId(tenantId: string): Promise<Segment[]>;
  update(segment: Segment): Promise<Segment>;
  delete(id: string): Promise<boolean>;
}
