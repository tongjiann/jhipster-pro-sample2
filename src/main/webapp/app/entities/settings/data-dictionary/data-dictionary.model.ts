// import { Moment } from 'moment';
import * as dayjs from 'dayjs';
import { DictType } from 'app/entities/enumerations/dict-type.model';
export interface IDataDictionary {
  id?: number;
  name?: string | null;
  code?: string | null;
  title?: string | null;
  value?: string | null;
  description?: string | null;
  sortOrder?: number | null;
  disabled?: boolean | null;
  fontColor?: string | null;
  valueType?: DictType | null;
  backgroundColor?: string | null;
  removedAt?: dayjs.Dayjs | null;
  children?: IDataDictionary[] | null;
  parent?: IDataDictionary | null;
  expand?: boolean;
  nzAddLevel?: number;
}

export class DataDictionary implements IDataDictionary {
  constructor(
    public id?: number,
    public name?: string | null,
    public code?: string | null,
    public title?: string | null,
    public value?: string | null,
    public description?: string | null,
    public sortOrder?: number | null,
    public disabled?: boolean | null,
    public fontColor?: string | null,
    public valueType?: DictType | null,
    public backgroundColor?: string | null,
    public removedAt?: dayjs.Dayjs | null,
    public children?: IDataDictionary[] | null,
    public parent?: IDataDictionary | null,
    public expand?: boolean,
    public nzAddLevel?: number
  ) {
    this.disabled = this.disabled ?? false;
  }
}

export function getDataDictionaryIdentifier(dataDictionary: IDataDictionary): number | undefined {
  return dataDictionary.id;
}
