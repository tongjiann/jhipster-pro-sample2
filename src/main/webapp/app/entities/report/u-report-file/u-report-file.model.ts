// import { Moment } from 'moment';
import * as dayjs from 'dayjs';
export interface IUReportFile {
  id?: number;
  name?: string | null;
  content?: string | null;
  createAt?: dayjs.Dayjs | null;
  updateAt?: dayjs.Dayjs | null;
  removedAt?: dayjs.Dayjs | null;
}

export class UReportFile implements IUReportFile {
  constructor(
    public id?: number,
    public name?: string | null,
    public content?: string | null,
    public createAt?: dayjs.Dayjs | null,
    public updateAt?: dayjs.Dayjs | null,
    public removedAt?: dayjs.Dayjs | null
  ) {}
}

export function getUReportFileIdentifier(uReportFile: IUReportFile): number | undefined {
  return uReportFile.id;
}
