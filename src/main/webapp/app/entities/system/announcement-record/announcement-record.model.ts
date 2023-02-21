// import { Moment } from 'moment';
import * as dayjs from 'dayjs';
export interface IAnnouncementRecord {
  id?: number;
  anntId?: number | null;
  userId?: number | null;
  hasRead?: boolean | null;
  readTime?: dayjs.Dayjs | null;
  removedAt?: dayjs.Dayjs | null;
}

export class AnnouncementRecord implements IAnnouncementRecord {
  constructor(
    public id?: number,
    public anntId?: number | null,
    public userId?: number | null,
    public hasRead?: boolean | null,
    public readTime?: dayjs.Dayjs | null,
    public removedAt?: dayjs.Dayjs | null
  ) {
    this.hasRead = this.hasRead ?? false;
  }
}

export function getAnnouncementRecordIdentifier(announcementRecord: IAnnouncementRecord): number | undefined {
  return announcementRecord.id;
}
