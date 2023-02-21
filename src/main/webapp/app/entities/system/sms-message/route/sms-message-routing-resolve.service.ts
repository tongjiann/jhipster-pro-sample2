import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISmsMessage, SmsMessage } from '../sms-message.model';
import { SmsMessageService } from '../service/sms-message.service';

@Injectable({ providedIn: 'root' })
export class SmsMessageRoutingResolveService implements Resolve<ISmsMessage> {
  constructor(protected service: SmsMessageService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISmsMessage> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((smsMessage: HttpResponse<SmsMessage>) => {
          if (smsMessage.body) {
            return of(smsMessage.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SmsMessage());
  }
}
