import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { Subject, debounceTime, distinctUntilChanged } from 'rxjs';
import { UserSearch } from 'src/app/interfaces/usersearch.interface';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-user-search-dialog',
  templateUrl: './user-search-dialog.component.html',
  styleUrls: ['./user-search-dialog.component.scss']
})
export class UserSearchDialogComponent implements OnInit {

  public keyword$: Subject<string> = new Subject<string>();
  public users: Array<UserSearch> | undefined = undefined;

  constructor(
    private readonly userService: UserService,
    private readonly dialog: MatDialogRef<UserSearchDialogComponent>
  ) { }

  ngOnInit(): void {
    this.keyword$.pipe(
      debounceTime(500),
      distinctUntilChanged()
    ).subscribe((keyword) => {
      this.userService.search(keyword).then((users) => {
        this.users = users;
      });
    });
  }

  public inputDataChange(keyword: string) {
    this.keyword$.next(
      keyword
    );
  }

  public selectUser(user: UserSearch) {
    this.dialog.close(
      user
    );
  }

}
