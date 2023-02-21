import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBusinessType, BusinessType } from '../business-type.model';
import { BusinessTypeService } from '../service/business-type.service';

@Injectable({ providedIn: 'root' })
export class BusinessTypeRoutingResolveService implements Resolve<IBusinessType> {
  constructor(protected service: BusinessTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBusinessType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((businessType: HttpResponse<BusinessType>) => {
          if (businessType.body) {
            return of(businessType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new BusinessType());
  }
}
