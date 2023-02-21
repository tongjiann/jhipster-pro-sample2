import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDataDictionary, DataDictionary } from '../data-dictionary.model';
import { DataDictionaryService } from '../service/data-dictionary.service';

@Injectable({ providedIn: 'root' })
export class DataDictionaryRoutingResolveService implements Resolve<IDataDictionary> {
  constructor(protected service: DataDictionaryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDataDictionary> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((dataDictionary: HttpResponse<DataDictionary>) => {
          if (dataDictionary.body) {
            return of(dataDictionary.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DataDictionary());
  }
}
