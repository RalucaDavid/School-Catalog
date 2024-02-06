import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { SubjectStudents } from 'src/app/interfaces/subject.interface';
import { SubjectService } from 'src/app/services/subject.service';
import { UserSearchDialogComponent } from '../user-search-dialog/user-search-dialog.component';
import { ConfirmationDialogService } from 'src/app/services/confirm.service';
import { UserSearch } from 'src/app/interfaces/usersearch.interface';
import { ToastrService } from 'ngx-toastr';
import {Sort} from '@angular/material/sort';

@Component({
  selector: 'app-subject-students',
  templateUrl: './subject-students.component.html',
  styleUrls: ['./subject-students.component.scss']
})
export class SubjectStudentsComponent implements OnInit {

  private subjectId: number | undefined = undefined;

  private students: SubjectStudents | undefined = undefined;
  public sortedStudents: SubjectStudents | undefined = undefined;

  constructor(
    private readonly activeRoute: ActivatedRoute,
    private readonly router: Router,
    private readonly subjectsService: SubjectService,
    private readonly dialog: MatDialog,
    private readonly confirmService: ConfirmationDialogService,
    private readonly toastrService: ToastrService
  ) { }

  ngOnInit(): void {
    this.activeRoute.paramMap.subscribe((params) => {
      
      this.subjectId = Number(params.get('id'));

      if (isNaN(this.subjectId)) {
        this.router.navigateByUrl('/my-courses');
      }
      else {
        this.subjectsService.get(this.subjectId).then(data => {
          this.students = data;
          this.sortedStudents = this.students.slice();
        }).catch(() => {
          this.router.navigateByUrl('/my-courses');
        }); 
      }

    });
  }

  public finalGrade(grades: Array<any>) {
    return grades.find(g => g.final);
  }

  public nonFinalGrades(grades: Array<any>) {
    return grades.filter(g => !g.final);
  }

  public addStudent() {
    firstValueFrom(this.dialog.open(UserSearchDialogComponent, {
      minWidth: '500px',
      maxHeight: '80vh',
    }).afterClosed()).then((user: UserSearch) => {
      
      if (user === undefined)
        return;

      this.confirmService.confirm(
        'Add Student',
        `Are you sure you want to add ${user.fullName} to your course?`,
      ).then((response) => {

        if (!response)
          return;

        this.subjectsService.addStudent(this.subjectId!, user.id).then(() => {
          this.refresh();
        });

      });

    });
  }

  private refresh() {
    this.subjectsService.get(this.subjectId!).then(data => {
      this.students = data;
      this.sortedStudents = this.students.slice();
    });
  }

  public addGrade(student: any) {
    
    const grade = prompt('Please enter the grade:', '10');

    if (!grade)
      return;

    const gradeNumber = parseFloat(grade);

    if (isNaN(gradeNumber) || gradeNumber < 1 || gradeNumber > 10)
      return this.toastrService.error('Invalid grade', 'Error');    
    
    return this.confirmService.confirm(
      'Add Grade',
      `Are you sure you want to add grade ${gradeNumber} to ${student.userName}?`,
    ).then((response) => {

      if (!response)
        return;

      this.subjectsService.addGrade(student.userLearningSubjectId, gradeNumber).then(() => {
        this.refresh();
      }).catch((error: string) => {
        this.toastrService.error(error, 'Error');
      });

    });
  }

  public setFinalGrade(student: any) {
    return this.confirmService.confirm(
      'Set Final Grade',
      `Are you sure you want to set final grade for ${student.userName}?`,
    ).then((response) => {

      if (!response)
        return;

      this.subjectsService.finalGrade(student.userLearningSubjectId).then(() => {
        this.refresh();
      }).catch((error: string) => {
        this.toastrService.error(error, 'Error');
      });

    });
  }

  public sortData(sort: Sort) {
    
    const data = this.students!.slice();
    if (!sort.active || sort.direction === '') {
      this.sortedStudents = data;
      return;
    }

    this.sortedStudents = data.sort((a, b) => {
      const isAsc = sort.direction === 'asc';
      switch (sort.active) {
        case 'name':
          return compare(a.userName, b.userName, isAsc);
        case 'grades-date':
          //@ts-ignore
          return compare(Math.max(...a.grades.map(g => g.date)), Math.max(...b.grades.map(g => g.date)), isAsc);
        case 'final-grade':
          return compare(this.finalGrade(a.grades)?.grade, this.finalGrade(b.grades)?.grade, isAsc);
        default:
          return 0;
      }
    });

  }


}

function compare(a: number | string, b: number | string, isAsc: boolean) {
  return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
}