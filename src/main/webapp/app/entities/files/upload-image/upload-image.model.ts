// import { Moment } from 'moment';
import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';
import { IResourceCategory } from 'app/entities/files/resource-category/resource-category.model';
export interface IUploadImage {
  id?: number;
  fullName?: string | null;
  name?: string | null;
  ext?: string | null;
  type?: string | null;
  url?: string | null;
  path?: string | null;
  folder?: string | null;
  entityName?: string | null;
  createAt?: dayjs.Dayjs | null;
  fileSize?: number | null;
  smartUrl?: string | null;
  mediumUrl?: string | null;
  referenceCount?: number | null;
  removedAt?: dayjs.Dayjs | null;
  user?: IUser | null;
  category?: IResourceCategory | null;
}

export class UploadImage implements IUploadImage {
  constructor(
    public id?: number,
    public fullName?: string | null,
    public name?: string | null,
    public ext?: string | null,
    public type?: string | null,
    public url?: string | null,
    public path?: string | null,
    public folder?: string | null,
    public entityName?: string | null,
    public createAt?: dayjs.Dayjs | null,
    public fileSize?: number | null,
    public smartUrl?: string | null,
    public mediumUrl?: string | null,
    public referenceCount?: number | null,
    public removedAt?: dayjs.Dayjs | null,
    public user?: IUser | null,
    public category?: IResourceCategory | null
  ) {}
}

export function getUploadImageIdentifier(uploadImage: IUploadImage): number | undefined {
  return uploadImage.id;
}
