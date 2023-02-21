import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IApiPermission, ApiPermission } from '../api-permission.model';
import { ApiPermissionService } from '../service/api-permission.service';

@Injectable({ providedIn: 'root' })
export class ApiPermissionRoutingResolveService implements Resolve<IApiPermission> {
  constructor(protected service: ApiPermissionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IApiPermission> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((apiPermission: HttpResponse<ApiPermission>) => {
          if (apiPermission.body) {
            return of(apiPermission.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ApiPermission());
  }
}
