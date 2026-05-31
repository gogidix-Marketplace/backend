import { ValueObject } from '@shared/base/base.entity';
import { CloudProvider } from '../enums/cloud-provider.enum';
import { InstanceState } from '../enums/instance-state.enum';

export class CloudInstance extends ValueObject {
  constructor(
    public instanceId: string,
    public instanceType: string,
    public state: InstanceState,
    public launchTime: Date,
    public privateIpAddress?: string,
    public publicIpAddress?: string,
    public tags: Record<string, string> = {},
  ) {
    super();
  }
}
