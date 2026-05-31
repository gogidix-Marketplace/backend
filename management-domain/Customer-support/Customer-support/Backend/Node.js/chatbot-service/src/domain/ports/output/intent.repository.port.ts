import { IntentProps } from '../../models';
import { IntentCategory } from '../../enums';

export interface IIntentRepository {
  findById(intentId: string): Promise<IntentProps | null>;
  findOne(filter: any): Promise<IntentProps | null>;
  find(filter: any, sort?: any): Promise<IntentProps[]>;
  findActiveByLanguage(language: string): Promise<IntentProps[]>;
  findByCategory(category: IntentCategory, language?: string): Promise<IntentProps[]>;
  save(data: any): Promise<any>;
  findByIdAndUpdate(id: string, update: any, options?: any): Promise<IntentProps | null>;
  findByIdAndDelete(id: string): Promise<IntentProps | null>;
}
