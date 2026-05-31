import { v4 as uuidv4 } from 'uuid';
export abstract class BaseEntity { readonly id: string; readonly createdAt: Date; readonly updatedAt: Date; constructor(p?: { id?: string; createdAt?: Date; updatedAt?: Date }) { this.id = p?.id || uuidv4(); this.createdAt = p?.createdAt || new Date(); this.updatedAt = p?.updatedAt || new Date(); } }
