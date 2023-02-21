import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IResourceCategory, ResourceCategory } from '../resource-category.model';
import { ResourceCategoryService } from '../service/resource-category.service';

@Injectable({ providedIn: 'root' })
export class ResourceCategoryRoutingResolveService implements Resolve<IResourceCategory> {
  constructor(protected service: ResourceCategoryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IResourceCategory> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((resourceCategory: HttpResponse<ResourceCategory>) => {
          if (resourceCategory.body) {
            return of(resourceCategory.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ResourceCategory());
  }
}
