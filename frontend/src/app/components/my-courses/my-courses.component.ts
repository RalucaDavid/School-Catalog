import { AfterViewInit, ChangeDetectorRef, Component } from '@angular/core';
import { Grade, LearningSubjectAndGrades, Subject } from 'src/app/interfaces/subject.interface';
import { AuthService } from 'src/app/services/auth.service';
import { SubjectService } from 'src/app/services/subject.service';

@Component({
  selector: 'app-my-courses',
  templateUrl: './my-courses.component.html',
  styleUrls: ['./my-courses.component.scss']
})
export class MyCoursesComponent implements AfterViewInit {

  public teachingCourses: Array<Subject> | undefined = undefined;
  public learningCourses: Record<string, Array<Grade>> | undefined = undefined;

  constructor(
    private readonly authService: AuthService,
    private readonly subjectsService: SubjectService,
    private readonly cdr: ChangeDetectorRef
  ) { }

  ngAfterViewInit(): void {
    if (this.authService.userValue) 
      this.getData();

    this.authService.user$.subscribe(user => {

      if (!user)
        return;

      this.getData();
    });
  }

  private getData() {

    this.subjectsService.getTeachingSubjects().then((subjects) => {
      this.teachingCourses = subjects;
      this.cdr.detectChanges();
    });

    this.subjectsService.getLearningSubjects().then((subjects) => {

      this.learningCourses = {};

      for (let [name, grade, timestamp, final] of subjects) {
        
        if (!this.learningCourses[name])
          this.learningCourses[name] = [];

        this.learningCourses[name].push({
          grade,
          timestamp,
          final
        });
      }

      this.cdr.detectChanges();
    });
  }

}
