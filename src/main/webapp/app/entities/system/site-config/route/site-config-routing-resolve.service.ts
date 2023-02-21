import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISiteConfig, SiteConfig } from '../site-config.model';
import { SiteConfigService } from '../service/site-config.service';

@Injectable({ providedIn: 'root' })
export class SiteConfigRoutingResolveService implements Resolve<ISiteConfig> {
  constructor(protected service: SiteConfigService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISiteConfig> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((siteConfig: HttpResponse<SiteConfig>) => {
          if (siteConfig.body) {
            return of(siteConfig.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SiteConfig());
  }
}
