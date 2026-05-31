import { v4 as uuidv4 } from 'uuid';
export abstract class BaseEntity { readonly id: string; readonly createdAt: Date; readonly updatedAt: Date; constructor(props?: { id?: string; createdAt?: Date; updatedAt?: Date }) { this.id = props?.id || uuidv4(); this.createdAt = props?.createdAt || new Date(); this.updatedAt = props?.updatedAt || new Date(); } }
