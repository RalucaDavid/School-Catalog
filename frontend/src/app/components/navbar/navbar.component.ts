import { Component, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { User } from 'src/app/interfaces/user.interface';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnDestroy {

  public user: User | null = null;
  private subscription: Subscription;

  public userMenuStatus: boolean = false;

  constructor(
    private readonly authService: AuthService,
  ) {
    this.subscription = this.authService.user$.subscribe((user) => {
      this.user = user;
    });
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  public logout() {
    this.authService.logout();
  }

  public toggleUserMenu() {
    this.userMenuStatus = !this.userMenuStatus;
  }

}
