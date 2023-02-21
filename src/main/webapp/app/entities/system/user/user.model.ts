// import { Moment } from 'moment';
import * as dayjs from 'dayjs';
import { IDepartment } from 'app/entities/settings/department/department.model';
import { IPosition } from 'app/entities/settings/position/position.model';
export interface IUser {
  id?: number;
  login?: string | null;
  password?: string | null;
  firstName?: string | null;
  lastName?: string | null;
  email?: string | null;
  mobile?: string | null;
  birthday?: dayjs.Dayjs | null;
  activated?: boolean | null;
  langKey?: string | null;
  imageUrl?: string | null;
  activationKey?: string | null;
  resetKey?: string | null;
  resetDate?: dayjs.Dayjs | null;
  removedAt?: dayjs.Dayjs | null;
  department?: IDepartment | null;
  position?: IPosition | null;
}

export class User implements IUser {
  constructor(
    public id?: number,
    public login?: string | null,
    public password?: string | null,
    public firstName?: string | null,
    public lastName?: string | null,
    public email?: string | null,
    public mobile?: string | null,
    public birthday?: dayjs.Dayjs | null,
    public activated?: boolean | null,
    public langKey?: string | null,
    public imageUrl?: string | null,
    public activationKey?: string | null,
    public resetKey?: string | null,
    public resetDate?: dayjs.Dayjs | null,
    public removedAt?: dayjs.Dayjs | null,
    public department?: IDepartment | null,
    public position?: IPosition | null
  ) {
    this.activated = this.activated ?? false;
  }
}

export function getUserIdentifier(user: IUser): number | undefined {
  return user.id;
}
