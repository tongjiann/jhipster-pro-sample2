import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPosition, Position } from '../position.model';
import { PositionService } from '../service/position.service';

@Injectable({ providedIn: 'root' })
export class PositionRoutingResolveService implements Resolve<IPosition> {
  constructor(protected service: PositionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPosition> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((position: HttpResponse<Position>) => {
          if (position.body) {
            return of(position.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Position());
  }
}
