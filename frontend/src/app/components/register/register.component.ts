import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { RequestService } from 'src/app/services/request.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {

  public formGroup: FormGroup = new FormGroup({
    firstName: new FormControl('', [Validators.required]),
    lastName: new FormControl('', [Validators.required]),
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required])
  });

  constructor(
    private readonly authService: AuthService,
    private readonly router: Router,
    private readonly toastr: ToastrService,
    private readonly requestsService: RequestService
  ) {
    if (!this.authService.hasToken())
      return;

    this.router.navigate(['/my-courses']);
  }

  public register() {
    
    if (this.formGroup.invalid)
      return;

    const { firstName, lastName, email, password } = this.formGroup.value;

    this.requestsService.post<string>(`users/register`, { firstName, lastName, email, password }).then((response) => {
      this.router.navigate(['/login']);
      this.toastr.error(
        response, 
        'Account Registration'
      );
    }).catch((error) => {
      this.toastr.error(
        error, 
        'Account Registration'
      );
    });
  }

}
