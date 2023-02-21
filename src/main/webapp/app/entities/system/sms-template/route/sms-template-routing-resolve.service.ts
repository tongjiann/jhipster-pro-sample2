import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISmsTemplate, SmsTemplate } from '../sms-template.model';
import { SmsTemplateService } from '../service/sms-template.service';

@Injectable({ providedIn: 'root' })
export class SmsTemplateRoutingResolveService implements Resolve<ISmsTemplate> {
  constructor(protected service: SmsTemplateService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISmsTemplate> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((smsTemplate: HttpResponse<SmsTemplate>) => {
          if (smsTemplate.body) {
            return of(smsTemplate.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SmsTemplate());
  }
}
