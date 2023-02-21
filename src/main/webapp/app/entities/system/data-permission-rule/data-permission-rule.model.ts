// import { Moment } from 'moment';
import * as dayjs from 'dayjs';
export interface IDataPermissionRule {
  id?: number;
  permissionId?: string | null;
  name?: string | null;
  column?: string | null;
  conditions?: string | null;
  value?: string | null;
  disabled?: boolean | null;
  removedAt?: dayjs.Dayjs | null;
}

export class DataPermissionRule implements IDataPermissionRule {
  constructor(
    public id?: number,
    public permissionId?: string | null,
    public name?: string | null,
    public column?: string | null,
    public conditions?: string | null,
    public value?: string | null,
    public disabled?: boolean | null,
    public removedAt?: dayjs.Dayjs | null
  ) {
    this.disabled = this.disabled ?? false;
  }
}

export function getDataPermissionRuleIdentifier(dataPermissionRule: IDataPermissionRule): number | undefined {
  return dataPermissionRule.id;
}
