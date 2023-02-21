// import { Moment } from 'moment';
import * as dayjs from 'dayjs';
import { MessageSendType } from 'app/entities/enumerations/message-send-type.model';
import { SendStatus } from 'app/entities/enumerations/send-status.model';
export interface ISmsMessage {
  id?: number;
  title?: string | null;
  sendType?: MessageSendType | null;
  receiver?: string | null;
  params?: string | null;
  content?: string | null;
  sendTime?: dayjs.Dayjs | null;
  sendStatus?: SendStatus | null;
  retryNum?: number | null;
  failResult?: string | null;
  remark?: string | null;
  removedAt?: dayjs.Dayjs | null;
}

export class SmsMessage implements ISmsMessage {
  constructor(
    public id?: number,
    public title?: string | null,
    public sendType?: MessageSendType | null,
    public receiver?: string | null,
    public params?: string | null,
    public content?: string | null,
    public sendTime?: dayjs.Dayjs | null,
    public sendStatus?: SendStatus | null,
    public retryNum?: number | null,
    public failResult?: string | null,
    public remark?: string | null,
    public removedAt?: dayjs.Dayjs | null
  ) {}
}

export function getSmsMessageIdentifier(smsMessage: ISmsMessage): number | undefined {
  return smsMessage.id;
}
