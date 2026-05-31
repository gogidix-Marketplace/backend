import { v4 as uuidv4 } from 'uuid';
import { ScoreType } from '../../domain/entities/enums/score-type.enum';

export class CreateScoreAttributeCommand {
  readonly commandId: string;
  readonly commandType = 'CreateScoreAttribute';

  constructor(
    readonly tenantId: string,
    readonly name: string,
    readonly description: string,
    readonly type: ScoreType,
    readonly dataType: string,
    readonly weight: number,
    readonly sourceField: string,
    readonly createdBy: string,
    readonly defaultValue?: any,
    readonly isRequired?: boolean,
    readonly options?: string[],
    readonly validationRule?: string,
    readonly displayOrder?: number
  ) {
    this.commandId = uuidv4();
  }
}
