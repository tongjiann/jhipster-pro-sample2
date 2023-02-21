import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISysFillRule, SysFillRule } from '../sys-fill-rule.model';
import { SysFillRuleService } from '../service/sys-fill-rule.service';

@Injectable({ providedIn: 'root' })
export class SysFillRuleRoutingResolveService implements Resolve<ISysFillRule> {
  constructor(protected service: SysFillRuleService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISysFillRule> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((sysFillRule: HttpResponse<SysFillRule>) => {
          if (sysFillRule.body) {
            return of(sysFillRule.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SysFillRule());
  }
}
