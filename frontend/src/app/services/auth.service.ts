import { Injectable } from "@angular/core";
import { BehaviorSubject } from "rxjs";
import { User } from "../interfaces/user.interface";
import { Router } from "@angular/router";
import { RequestService } from "./request.service";
import { TokenStorageService } from "./token.service";

@Injectable({ providedIn: 'root' })
export class AuthService {

  private user: BehaviorSubject<User | null> = new BehaviorSubject<User | null>(
    null
  );

  constructor(
    private readonly tokenService: TokenStorageService,
    private readonly request: RequestService,
    private readonly router: Router,
  ) 
  {
    this.tokenService.observable.subscribe((value) => {
      
      if (!value) {
        return this.user.next(
          null
        );
      }

      this.request.get<User>(`users/me`).then((user) => {
        this.user.next(
          user
        );
      }).catch(() => {
        this.logout();
      });

    });
  }

  public getToken(): string | null {
    return this.tokenService.value;
  }

  public hasToken(): boolean {
    return this.tokenService.value != null;
  }

  public get user$() {
    return this.user.asObservable();
  }

  public logout(): void {
    this.tokenService.next(null);
    this.router.navigate(['login']);
  }

  public login(email: string, password: string) {
    return new Promise<void>((_, reject) => {
      this.request.post<string>('users/login', { email, password }).then((data) => {
        this.tokenService.next(
          data
        );

        this.router.navigate(
          ['my-courses']
        );
      }).catch(error => {
        reject(
          error
        );
      });
    });
  }
}