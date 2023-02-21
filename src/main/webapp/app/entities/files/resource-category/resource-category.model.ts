// import { Moment } from 'moment';
import * as dayjs from 'dayjs';
import { IUploadFile } from 'app/entities/files/upload-file/upload-file.model';
import { IUploadImage } from 'app/entities/files/upload-image/upload-image.model';
export interface IResourceCategory {
  id?: number;
  title?: string | null;
  code?: string | null;
  sort?: number | null;
  removedAt?: dayjs.Dayjs | null;
  files?: IUploadFile[] | null;
  children?: IResourceCategory[] | null;
  images?: IUploadImage[] | null;
  parent?: IResourceCategory | null;
  expand?: boolean;
  nzAddLevel?: number;
}

export class ResourceCategory implements IResourceCategory {
  constructor(
    public id?: number,
    public title?: string | null,
    public code?: string | null,
    public sort?: number | null,
    public removedAt?: dayjs.Dayjs | null,
    public files?: IUploadFile[] | null,
    public children?: IResourceCategory[] | null,
    public images?: IUploadImage[] | null,
    public parent?: IResourceCategory | null,
    public expand?: boolean,
    public nzAddLevel?: number
  ) {}
}

export function getResourceCategoryIdentifier(resourceCategory: IResourceCategory): number | undefined {
  return resourceCategory.id;
}
