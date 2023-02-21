import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUser, User } from '../user.model';
import { UserService } from '../service/user.service';

@Injectable({ providedIn: 'root' })
export class UserRoutingResolveService implements Resolve<IUser> {
  constructor(protected service: UserService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUser> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((user: HttpResponse<User>) => {
          if (user.body) {
            return of(user.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new User());
  }
}
