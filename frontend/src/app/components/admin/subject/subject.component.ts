import { Component, Input } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr';
import { Subject, Teacher } from 'src/app/interfaces/subject.interface';
import { ConfirmationDialogService } from 'src/app/services/confirm.service';
import { SubjectService } from 'src/app/services/subject.service';
import { UserSearchDialogComponent } from '../../user-search-dialog/user-search-dialog.component';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'subject',
  templateUrl: './subject.component.html',
  styleUrls: ['./subject.component.scss']
})
export class SubjectComponent {

  @Input() 
  public subject?: Subject = undefined;

  constructor(
    private readonly confirmService: ConfirmationDialogService,
    private readonly subjectService: SubjectService,
    private readonly toastrService: ToastrService,
    private readonly dialog: MatDialog
  ) { }

  public removeTeacher(teacher: Teacher) {
    this.confirmService.confirm(
      'Remove Teacher',
      `Are you sure you want to remove ${teacher.fullName} from ${this.subject?.name}?`,
      'Yes', 'No'
    ).then((confirmed) => {

      if (!confirmed)
        return;

      this.subjectService.removeTeacher(this.subject!.id, teacher.id).then(() => {
        this.toastrService.success(
          `${teacher.fullName} was removed from ${this.subject?.name}.`
        );
        this.subject!.teachers = this.subject!.teachers!.filter((t) => t.id !== teacher.id);
      }).catch((error) => {
        this.toastrService.error(
          error
        );
      });

    });
  }

  public addTeacher() {
    firstValueFrom(this.dialog.open(UserSearchDialogComponent, {
      minWidth: '500px',
      maxHeight: '80vh',
    }).afterClosed()).then((user) => {
      
      if (user === undefined)
        return;

      this.confirmService.confirm(
        'Add Teacher',
        `Are you sure you want to add ${user.fullName} to ${this.subject?.name}?`,
        'Yes', 'No'
      ).then((confirmed) => {

        if (!confirmed)
          return;

        this.subjectService.addTeacher(this.subject!.id, user.id).then(() => {
          this.toastrService.success(
            `${user.fullName} was added to ${this.subject?.name}.`
          );
          this.subject!.teachers!.push(user);
        }).catch((error) => {
          this.toastrService.error(
            error
          );
        });

      });

    });
  }

}
