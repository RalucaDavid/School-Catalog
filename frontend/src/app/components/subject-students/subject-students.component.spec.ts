import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubjectStudentsComponent } from './subject-students.component';

describe('SubjectStudentsComponent', () => {
  let component: SubjectStudentsComponent;
  let fixture: ComponentFixture<SubjectStudentsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SubjectStudentsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SubjectStudentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
