// import { Moment } from 'moment';
import * as dayjs from 'dayjs';
import { IAuthority } from 'app/entities/system/authority/authority.model';
import { IUser } from 'app/entities/user/user.model';
import { IDepartmentAuthority } from 'app/entities/settings/department-authority/department-authority.model';
export interface IDepartment {
  id?: number;
  name?: string | null;
  code?: string | null;
  address?: string | null;
  phoneNum?: string | null;
  logo?: string | null;
  contact?: string | null;
  createUserId?: number | null;
  createTime?: dayjs.Dayjs | null;
  removedAt?: dayjs.Dayjs | null;
  children?: IDepartment[] | null;
  authorities?: IAuthority[] | null;
  parent?: IDepartment | null;
  users?: IUser[] | null;
  departmentAuthorities?: IDepartmentAuthority[] | null;
  expand?: boolean;
  nzAddLevel?: number;
}

export class Department implements IDepartment {
  constructor(
    public id?: number,
    public name?: string | null,
    public code?: string | null,
    public address?: string | null,
    public phoneNum?: string | null,
    public logo?: string | null,
    public contact?: string | null,
    public createUserId?: number | null,
    public createTime?: dayjs.Dayjs | null,
    public removedAt?: dayjs.Dayjs | null,
    public children?: IDepartment[] | null,
    public authorities?: IAuthority[] | null,
    public parent?: IDepartment | null,
    public users?: IUser[] | null,
    public departmentAuthorities?: IDepartmentAuthority[] | null,
    public expand?: boolean,
    public nzAddLevel?: number
  ) {}
}

export function getDepartmentIdentifier(department: IDepartment): number | undefined {
  return department.id;
}
