import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Response } from "../interfaces/response.interface";
import { firstValueFrom } from "rxjs";
import { environment } from "src/environment/environment";

@Injectable({ providedIn: 'root' })
export class RequestService {
    constructor(
        private readonly httpClient: HttpClient
    ) { }

    public get<K>(path: string) {
      return new Promise<K>((resolve, reject) => {
        firstValueFrom(this.httpClient.get<Response<K>>(`${environment.apiUrl}${path}`)).then((response) => {

          const { status, data } = response;

          switch (status) {
            case 'ERROR':
              reject(data);
              break;
            case 'SUCCESS':
              resolve(data);
              break;
          }
        }).catch(() => {
          reject(
            'Failed to send request.'
          );
        });
      });
    }

    public post<K>(path: string, data?: Object) {
      return new Promise<K>((resolve, reject) => {
        firstValueFrom(this.httpClient.post<Response<K>>(`${environment.apiUrl}${path}`, data)).then((response) => {

          const { status, data } = response;

          switch (status) {
            case 'ERROR':
              reject(data);
              break;
            case 'SUCCESS':
              resolve(data);
              break;
          }
        }).catch((e) => {
          alert(e);
          reject(
            'Failed to send request.'
          );
        });
      });
    }
}