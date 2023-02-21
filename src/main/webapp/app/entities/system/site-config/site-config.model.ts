// import { Moment } from 'moment';
import * as dayjs from 'dayjs';
import { CommonFieldType } from 'app/entities/enumerations/common-field-type.model';
export interface ISiteConfig {
  id?: number;
  title?: string | null;
  remark?: string | null;
  fieldName?: string | null;
  fieldValue?: string | null;
  fieldType?: CommonFieldType | null;
  removedAt?: dayjs.Dayjs | null;
}

export class SiteConfig implements ISiteConfig {
  constructor(
    public id?: number,
    public title?: string | null,
    public remark?: string | null,
    public fieldName?: string | null,
    public fieldValue?: string | null,
    public fieldType?: CommonFieldType | null,
    public removedAt?: dayjs.Dayjs | null
  ) {}
}

export function getSiteConfigIdentifier(siteConfig: ISiteConfig): number | undefined {
  return siteConfig.id;
}
