import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { Subject } from 'src/app/interfaces/subject.interface';
import { SubjectService } from 'src/app/services/subject.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.scss']
})
export class AdminComponent implements OnInit {
  
  public subjects?: Array<Subject> = undefined;

  public formGroup: FormGroup = new FormGroup({
    name: new FormControl('', [Validators.required]),
    description: new FormControl('', [Validators.required]),
  });
  
  constructor(
    private readonly subjectService: SubjectService,
    private readonly userService: UserService,
    private readonly toastr: ToastrService
  ) { }

  ngOnInit(): void {
    this.subjectService.getAll().then(subjects => {
      this.subjects = subjects;
    });
  }

  public createSubject() {
    
    if (!this.formGroup.valid)
      return;

      const { name, description } = this.formGroup.value;

      this.subjectService.create(name, description).then(subject => {
        
        if (!this.subjects)
          this.subjects = [];

        this.subjects.push(subject);
        this.formGroup.reset();

      }).catch((error) => {
        this.toastr.error(error, 'Error');
      });

  }
}
