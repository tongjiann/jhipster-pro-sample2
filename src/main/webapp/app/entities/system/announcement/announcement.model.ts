// import { Moment } from 'moment';
import * as dayjs from 'dayjs';
import { PriorityLevel } from 'app/entities/enumerations/priority-level.model';
import { AnnoCategory } from 'app/entities/enumerations/anno-category.model';
import { ReceiverType } from 'app/entities/enumerations/receiver-type.model';
import { AnnoSendStatus } from 'app/entities/enumerations/anno-send-status.model';
import { AnnoBusinessType } from 'app/entities/enumerations/anno-business-type.model';
import { AnnoOpenType } from 'app/entities/enumerations/anno-open-type.model';
export interface IAnnouncement {
  id?: number;
  titile?: string | null;
  content?: string | null;
  startTime?: dayjs.Dayjs | null;
  endTime?: dayjs.Dayjs | null;
  senderId?: number | null;
  priority?: PriorityLevel | null;
  category?: AnnoCategory | null;
  receiverType?: ReceiverType | null;
  sendStatus?: AnnoSendStatus | null;
  sendTime?: dayjs.Dayjs | null;
  cancelTime?: dayjs.Dayjs | null;
  businessType?: AnnoBusinessType | null;
  businessId?: number | null;
  openType?: AnnoOpenType | null;
  openPage?: string | null;
  receiverIds?: any | null;
  summary?: string | null;
  removedAt?: dayjs.Dayjs | null;
}

export class Announcement implements IAnnouncement {
  constructor(
    public id?: number,
    public titile?: string | null,
    public content?: string | null,
    public startTime?: dayjs.Dayjs | null,
    public endTime?: dayjs.Dayjs | null,
    public senderId?: number | null,
    public priority?: PriorityLevel | null,
    public category?: AnnoCategory | null,
    public receiverType?: ReceiverType | null,
    public sendStatus?: AnnoSendStatus | null,
    public sendTime?: dayjs.Dayjs | null,
    public cancelTime?: dayjs.Dayjs | null,
    public businessType?: AnnoBusinessType | null,
    public businessId?: number | null,
    public openType?: AnnoOpenType | null,
    public openPage?: string | null,
    public receiverIds?: any | null,
    public summary?: string | null,
    public removedAt?: dayjs.Dayjs | null
  ) {}
}

export function getAnnouncementIdentifier(announcement: IAnnouncement): number | undefined {
  return announcement.id;
}
