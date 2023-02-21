// import { Moment } from 'moment';
import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';
export interface IPosition {
  id?: number;
  code?: string;
  name?: string;
  sortNo?: number | null;
  description?: string | null;
  removedAt?: dayjs.Dayjs | null;
  users?: IUser[] | null;
}

export class Position implements IPosition {
  constructor(
    public id?: number,
    public code?: string,
    public name?: string,
    public sortNo?: number | null,
    public description?: string | null,
    public removedAt?: dayjs.Dayjs | null,
    public users?: IUser[] | null
  ) {}
}

export function getPositionIdentifier(position: IPosition): number | undefined {
  return position.id;
}
