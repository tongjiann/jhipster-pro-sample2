// import { Moment } from 'moment';
import * as dayjs from 'dayjs';
import { IDepartmentAuthority } from 'app/entities/settings/department-authority/department-authority.model';
import { IAuthority } from 'app/entities/system/authority/authority.model';
import { TargetType } from 'app/entities/enumerations/target-type.model';
import { ViewPermissionType } from 'app/entities/enumerations/view-permission-type.model';
export interface IViewPermission {
  id?: number;
  text?: string | null;
  i18n?: string | null;
  group?: boolean | null;
  link?: string | null;
  externalLink?: string | null;
  target?: TargetType | null;
  icon?: string | null;
  disabled?: boolean | null;
  hide?: boolean | null;
  hideInBreadcrumb?: boolean | null;
  shortcut?: boolean | null;
  shortcutRoot?: boolean | null;
  reuse?: boolean | null;
  code?: string | null;
  description?: string | null;
  type?: ViewPermissionType | null;
  order?: number | null;
  apiPermissionCodes?: string | null;
  componentFile?: string | null;
  redirect?: string | null;
  removedAt?: dayjs.Dayjs | null;
  children?: IViewPermission[] | null;
  departmentAuthority?: IDepartmentAuthority | null;
  parent?: IViewPermission | null;
  authorities?: IAuthority[] | null;
  expand?: boolean;
  nzAddLevel?: number;
}

export class ViewPermission implements IViewPermission {
  constructor(
    public id?: number,
    public text?: string | null,
    public i18n?: string | null,
    public group?: boolean | null,
    public link?: string | null,
    public externalLink?: string | null,
    public target?: TargetType | null,
    public icon?: string | null,
    public disabled?: boolean | null,
    public hide?: boolean | null,
    public hideInBreadcrumb?: boolean | null,
    public shortcut?: boolean | null,
    public shortcutRoot?: boolean | null,
    public reuse?: boolean | null,
    public code?: string | null,
    public description?: string | null,
    public type?: ViewPermissionType | null,
    public order?: number | null,
    public apiPermissionCodes?: string | null,
    public componentFile?: string | null,
    public redirect?: string | null,
    public removedAt?: dayjs.Dayjs | null,
    public children?: IViewPermission[] | null,
    public departmentAuthority?: IDepartmentAuthority | null,
    public parent?: IViewPermission | null,
    public authorities?: IAuthority[] | null,
    public expand?: boolean,
    public nzAddLevel?: number
  ) {
    this.group = this.group ?? false;
    this.disabled = this.disabled ?? false;
    this.hide = this.hide ?? false;
    this.hideInBreadcrumb = this.hideInBreadcrumb ?? false;
    this.shortcut = this.shortcut ?? false;
    this.shortcutRoot = this.shortcutRoot ?? false;
    this.reuse = this.reuse ?? false;
  }
}

export function getViewPermissionIdentifier(viewPermission: IViewPermission): number | undefined {
  return viewPermission.id;
}
