import { Injectable } from "@angular/core";
import { AuthService } from "../services/auth.service";
import { Router } from "@angular/router";

@Injectable()
export class LoggedGuard  {
	public constructor(
    private readonly authService: AuthService, 
    public readonly router: Router
  ) { }

	public canActivate(): boolean {
		if (!this.authService.hasToken()) {
			this.router.navigate(['login']);
			return false;
		}
		return true;
	}
}
