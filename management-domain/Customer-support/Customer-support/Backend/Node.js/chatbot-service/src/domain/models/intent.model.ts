import { BaseEntity } from '@shared/base';
import { IntentCategory } from '../enums';

export interface ParameterProps {
  name: string;
  type: string;
  required: boolean;
  entityType: string;
  prompts: string[];
}

export interface IntentProps {
  name: string;
  category: IntentCategory;
  description: string;
  trainingPhrases: string[];
  responses: string[];
  parameters?: ParameterProps[];
  requiredSkills?: string[];
  requiresHandoff: boolean;
  priority: number;
  language: string;
  isActive: boolean;
}

export class Intent extends BaseEntity {
  private props: IntentProps;

  constructor(id: string, props: IntentProps, createdAt?: Date, updatedAt?: Date) {
    super(id, createdAt, updatedAt);
    this.props = props;
  }

  static create(props: Partial<IntentProps> & { name: string; category: IntentCategory; description: string; trainingPhrases: string[]; responses: string[] }): Intent {
    return new Intent(
      props.name,
      {
        name: props.name,
        category: props.category,
        description: props.description,
        trainingPhrases: props.trainingPhrases,
        responses: props.responses,
        parameters: props.parameters || [],
        requiredSkills: props.requiredSkills || [],
        requiresHandoff: props.requiresHandoff || false,
        priority: props.priority || 0,
        language: props.language || 'en',
        isActive: props.isActive !== undefined ? props.isActive : true,
      },
    );
  }

  get name(): string {
    return this.props.name;
  }

  get category(): IntentCategory {
    return this.props.category;
  }

  get description(): string {
    return this.props.description;
  }

  get trainingPhrases(): string[] {
    return this.props.trainingPhrases;
  }

  get responses(): string[] {
    return this.props.responses;
  }

  get parameters(): ParameterProps[] | undefined {
    return this.props.parameters;
  }

  get requiredSkills(): string[] | undefined {
    return this.props.requiredSkills;
  }

  get requiresHandoff(): boolean {
    return this.props.requiresHandoff;
  }

  get priority(): number {
    return this.props.priority;
  }

  get language(): string {
    return this.props.language;
  }

  get isActive(): boolean {
    return this.props.isActive;
  }

  addTrainingPhrase(phrase: string): void {
    if (!this.props.trainingPhrases.includes(phrase)) {
      this.props.trainingPhrases.push(phrase);
      this.touch();
    }
  }

  addResponse(response: string): void {
    if (!this.props.responses.includes(response)) {
      this.props.responses.push(response);
      this.touch();
    }
  }

  toggleActive(): void {
    this.props.isActive = !this.props.isActive;
    this.touch();
  }

  toPlainObject(): IntentProps {
    return { ...this.props };
  }
}
