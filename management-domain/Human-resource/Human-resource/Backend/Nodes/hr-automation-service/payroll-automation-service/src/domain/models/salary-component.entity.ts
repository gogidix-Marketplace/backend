import { ValueObject } from '@shared/base/base.entity';
import { PayrollComponentType } from '../enums/payroll-component-type.enum';

export class SalaryComponent extends ValueObject {
  constructor(
    public readonly name: string,
    public readonly type: PayrollComponentType,
    public readonly amount: number,
    public readonly isFixed: boolean,
    public readonly isTaxable: boolean,
  ) {
    super();
  }
}
