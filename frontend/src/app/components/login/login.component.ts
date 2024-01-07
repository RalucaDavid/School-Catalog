import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {

  public email: string = '';
  public password: string = '';

  constructor (
    private readonly authService: AuthService,
    private readonly router: Router,
    private readonly toastrService: ToastrService
  ) 
  {
    if (this.authService.hasToken()) {
      this.router.navigate(
        ['my-courses']
      );
    }
  }

  public login() {
    
    if (!this.email || !this.password)
      return;

    this.authService.login(this.email, this.password).catch((error) => {
      this.toastrService.error(
        error, 
        'Error'
      );
    });
  }

}
