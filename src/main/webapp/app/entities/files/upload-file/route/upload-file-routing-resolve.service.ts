import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUploadFile, UploadFile } from '../upload-file.model';
import { UploadFileService } from '../service/upload-file.service';

@Injectable({ providedIn: 'root' })
export class UploadFileRoutingResolveService implements Resolve<IUploadFile> {
  constructor(protected service: UploadFileService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUploadFile> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((uploadFile: HttpResponse<UploadFile>) => {
          if (uploadFile.body) {
            return of(uploadFile.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new UploadFile());
  }
}
