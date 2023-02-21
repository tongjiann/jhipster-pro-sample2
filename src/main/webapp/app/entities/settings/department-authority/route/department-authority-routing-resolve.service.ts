import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDepartmentAuthority, DepartmentAuthority } from '../department-authority.model';
import { DepartmentAuthorityService } from '../service/department-authority.service';

@Injectable({ providedIn: 'root' })
export class DepartmentAuthorityRoutingResolveService implements Resolve<IDepartmentAuthority> {
  constructor(protected service: DepartmentAuthorityService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDepartmentAuthority> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((departmentAuthority: HttpResponse<DepartmentAuthority>) => {
          if (departmentAuthority.body) {
            return of(departmentAuthority.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DepartmentAuthority());
  }
}
