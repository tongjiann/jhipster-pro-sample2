import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRegionCode, RegionCode } from '../region-code.model';
import { RegionCodeService } from '../service/region-code.service';

@Injectable({ providedIn: 'root' })
export class RegionCodeRoutingResolveService implements Resolve<IRegionCode> {
  constructor(protected service: RegionCodeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRegionCode> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((regionCode: HttpResponse<RegionCode>) => {
          if (regionCode.body) {
            return of(regionCode.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new RegionCode());
  }
}
