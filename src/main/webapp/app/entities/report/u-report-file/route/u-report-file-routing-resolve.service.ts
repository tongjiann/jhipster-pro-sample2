import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUReportFile, UReportFile } from '../u-report-file.model';
import { UReportFileService } from '../service/u-report-file.service';

@Injectable({ providedIn: 'root' })
export class UReportFileRoutingResolveService implements Resolve<IUReportFile> {
  constructor(protected service: UReportFileService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUReportFile> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((uReportFile: HttpResponse<UReportFile>) => {
          if (uReportFile.body) {
            return of(uReportFile.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new UReportFile());
  }
}
