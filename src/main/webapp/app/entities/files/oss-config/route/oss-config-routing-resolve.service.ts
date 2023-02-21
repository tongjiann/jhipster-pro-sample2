import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOssConfig, OssConfig } from '../oss-config.model';
import { OssConfigService } from '../service/oss-config.service';

@Injectable({ providedIn: 'root' })
export class OssConfigRoutingResolveService implements Resolve<IOssConfig> {
  constructor(protected service: OssConfigService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOssConfig> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((ossConfig: HttpResponse<OssConfig>) => {
          if (ossConfig.body) {
            return of(ossConfig.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new OssConfig());
  }
}
