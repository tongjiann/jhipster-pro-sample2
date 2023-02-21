// import { Moment } from 'moment';
import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';
import { IApiPermission } from 'app/entities/system/api-permission/api-permission.model';
import { IViewPermission } from 'app/entities/system/view-permission/view-permission.model';
import { IDepartment } from 'app/entities/settings/department/department.model';
export interface IDepartmentAuthority {
  id?: number;
  name?: string | null;
  code?: string | null;
  description?: string | null;
  createUserId?: number | null;
  createTime?: dayjs.Dayjs | null;
  removedAt?: dayjs.Dayjs | null;
  users?: IUser[] | null;
  apiPermissions?: IApiPermission[] | null;
  viewPermissions?: IViewPermission[] | null;
  department?: IDepartment | null;
}

export class DepartmentAuthority implements IDepartmentAuthority {
  constructor(
    public id?: number,
    public name?: string | null,
    public code?: string | null,
    public description?: string | null,
    public createUserId?: number | null,
    public createTime?: dayjs.Dayjs | null,
    public removedAt?: dayjs.Dayjs | null,
    public users?: IUser[] | null,
    public apiPermissions?: IApiPermission[] | null,
    public viewPermissions?: IViewPermission[] | null,
    public department?: IDepartment | null
  ) {}
}

export function getDepartmentAuthorityIdentifier(departmentAuthority: IDepartmentAuthority): number | undefined {
  return departmentAuthority.id;
}
