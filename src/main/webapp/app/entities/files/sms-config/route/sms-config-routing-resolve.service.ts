import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISmsConfig, SmsConfig } from '../sms-config.model';
import { SmsConfigService } from '../service/sms-config.service';

@Injectable({ providedIn: 'root' })
export class SmsConfigRoutingResolveService implements Resolve<ISmsConfig> {
  constructor(protected service: SmsConfigService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISmsConfig> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((smsConfig: HttpResponse<SmsConfig>) => {
          if (smsConfig.body) {
            return of(smsConfig.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SmsConfig());
  }
}
