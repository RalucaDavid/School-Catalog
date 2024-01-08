import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable()
export class SuperUserGuard {

  constructor(
    private readonly authService: AuthService, 
    public readonly router: Router
  ) { }

  public canActivate(): boolean {

    if (!this.authService.hasToken()) {
      this.router.navigate(['login']);
      return false;
    }

    if (!this.authService.userValue || !this.authService.userValue.superUser) {
      this.router.navigate(['login']);
      return false;
    }

    return true;
  }
}
