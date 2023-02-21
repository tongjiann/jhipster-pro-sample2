import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAnnouncementRecord, AnnouncementRecord } from '../announcement-record.model';
import { AnnouncementRecordService } from '../service/announcement-record.service';

@Injectable({ providedIn: 'root' })
export class AnnouncementRecordRoutingResolveService implements Resolve<IAnnouncementRecord> {
  constructor(protected service: AnnouncementRecordService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAnnouncementRecord> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((announcementRecord: HttpResponse<AnnouncementRecord>) => {
          if (announcementRecord.body) {
            return of(announcementRecord.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AnnouncementRecord());
  }
}
