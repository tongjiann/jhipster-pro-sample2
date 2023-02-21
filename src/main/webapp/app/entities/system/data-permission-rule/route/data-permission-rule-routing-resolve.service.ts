import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDataPermissionRule, DataPermissionRule } from '../data-permission-rule.model';
import { DataPermissionRuleService } from '../service/data-permission-rule.service';

@Injectable({ providedIn: 'root' })
export class DataPermissionRuleRoutingResolveService implements Resolve<IDataPermissionRule> {
  constructor(protected service: DataPermissionRuleService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDataPermissionRule> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((dataPermissionRule: HttpResponse<DataPermissionRule>) => {
          if (dataPermissionRule.body) {
            return of(dataPermissionRule.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DataPermissionRule());
  }
}
