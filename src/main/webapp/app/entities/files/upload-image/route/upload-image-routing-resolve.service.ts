import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUploadImage, UploadImage } from '../upload-image.model';
import { UploadImageService } from '../service/upload-image.service';

@Injectable({ providedIn: 'root' })
export class UploadImageRoutingResolveService implements Resolve<IUploadImage> {
  constructor(protected service: UploadImageService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUploadImage> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((uploadImage: HttpResponse<UploadImage>) => {
          if (uploadImage.body) {
            return of(uploadImage.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new UploadImage());
  }
}
