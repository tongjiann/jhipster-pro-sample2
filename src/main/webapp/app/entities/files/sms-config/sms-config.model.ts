// import { Moment } from 'moment';
import * as dayjs from 'dayjs';
import { SmsProvider } from 'app/entities/enumerations/sms-provider.model';
export interface ISmsConfig {
  id?: number;
  provider?: SmsProvider | null;
  smsCode?: string | null;
  templateId?: string | null;
  accessKey?: string | null;
  secretKey?: string | null;
  regionId?: string | null;
  signName?: string | null;
  remark?: string | null;
  enabled?: boolean | null;
  removedAt?: dayjs.Dayjs | null;
}

export class SmsConfig implements ISmsConfig {
  constructor(
    public id?: number,
    public provider?: SmsProvider | null,
    public smsCode?: string | null,
    public templateId?: string | null,
    public accessKey?: string | null,
    public secretKey?: string | null,
    public regionId?: string | null,
    public signName?: string | null,
    public remark?: string | null,
    public enabled?: boolean | null,
    public removedAt?: dayjs.Dayjs | null
  ) {
    this.enabled = this.enabled ?? false;
  }
}

export function getSmsConfigIdentifier(smsConfig: ISmsConfig): number | undefined {
  return smsConfig.id;
}
