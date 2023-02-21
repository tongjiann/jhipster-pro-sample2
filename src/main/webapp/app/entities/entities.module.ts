import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'system',
        loadChildren: () => import('./system/system.module').then(m => m.SystemModule),
      },

      {
        path: 'settings',
        loadChildren: () => import('./settings/settings.module').then(m => m.SettingsModule),
      },

      {
        path: 'sms-message',
        loadChildren: () => import('./system/sms-message/sms-message.module').then(m => m.SmsMessageModule),
      },

      {
        path: 'files',
        loadChildren: () => import('./files/files.module').then(m => m.FilesModule),
      },

      {
        path: 'report',
        loadChildren: () => import('./report/report.module').then(m => m.ReportModule),
      },

      {
        path: 'announcement-record',
        loadChildren: () => import('./system/announcement-record/announcement-record.module').then(m => m.AnnouncementRecordModule),
      },

      {
        path: 'sms-template',
        loadChildren: () => import('./system/sms-template/sms-template.module').then(m => m.SmsTemplateModule),
      },

      {
        path: 'site-config',
        loadChildren: () => import('./system/site-config/site-config.module').then(m => m.SiteConfigModule),
      },

      {
        path: 'announcement',
        loadChildren: () => import('./system/announcement/announcement.module').then(m => m.AnnouncementModule),
      },

      {
        path: 'sys-log',
        loadChildren: () => import('./system/sys-log/sys-log.module').then(m => m.SysLogModule),
      },

      {
        path: 'data-permission-rule',
        loadChildren: () => import('./system/data-permission-rule/data-permission-rule.module').then(m => m.DataPermissionRuleModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class EntitiesModule {}
