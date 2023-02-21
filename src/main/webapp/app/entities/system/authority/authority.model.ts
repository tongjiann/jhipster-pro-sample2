// import { Moment } from 'moment';
import * as dayjs from 'dayjs';
import { IDepartment } from 'app/entities/settings/department/department.model';
import { IApiPermission } from 'app/entities/system/api-permission/api-permission.model';
import { IViewPermission } from 'app/entities/system/view-permission/view-permission.model';
export interface IAuthority {
  id?: number;
  name?: string | null;
  code?: string | null;
  info?: string | null;
  order?: number | null;
  display?: boolean | null;
  removedAt?: dayjs.Dayjs | null;
  children?: IAuthority[] | null;
  parent?: IAuthority | null;
  departments?: IDepartment[] | null;
  apiPermissions?: IApiPermission[] | null;
  viewPermissions?: IViewPermission[] | null;
  expand?: boolean;
  nzAddLevel?: number;
}

export class Authority implements IAuthority {
  constructor(
    public id?: number,
    public name?: string | null,
    public code?: string | null,
    public info?: string | null,
    public order?: number | null,
    public display?: boolean | null,
    public removedAt?: dayjs.Dayjs | null,
    public children?: IAuthority[] | null,
    public parent?: IAuthority | null,
    public departments?: IDepartment[] | null,
    public apiPermissions?: IApiPermission[] | null,
    public viewPermissions?: IViewPermission[] | null,
    public expand?: boolean,
    public nzAddLevel?: number
  ) {
    this.display = this.display ?? false;
  }
}

export function getAuthorityIdentifier(authority: IAuthority): number | undefined {
  return authority.id;
}
