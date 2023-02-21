import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IViewPermission, ViewPermission } from '../view-permission.model';
import { ViewPermissionService } from '../service/view-permission.service';

@Injectable({ providedIn: 'root' })
export class ViewPermissionRoutingResolveService implements Resolve<IViewPermission> {
  constructor(protected service: ViewPermissionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IViewPermission> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((viewPermission: HttpResponse<ViewPermission>) => {
          if (viewPermission.body) {
            return of(viewPermission.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ViewPermission());
  }
}
