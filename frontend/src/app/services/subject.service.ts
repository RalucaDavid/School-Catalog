import { Injectable } from "@angular/core";
import { RequestService } from "./request.service";
import { LearningSubjectAndGrades, Subject, SubjectStudents } from "../interfaces/subject.interface";

@Injectable({ providedIn: 'root' })
export class SubjectService {
  
  constructor(
    private readonly requestService: RequestService
  ) { }

  public getAll() {
    return this.requestService.get<Array<Subject>>(`subjects/list`);
  }

  public create(name: string, description: string) {
    return this.requestService.post<Subject>(`subjects/create`, { name, description });
  }

  public addTeacher(subjectId: number, userId: number) {
    return this.requestService.put(`subjects/teacher`, { subjectId, userId });
  }

  public removeTeacher(subjectId: number, userId: number) {
    return this.requestService.delete(`subjects/teacher`, { subjectId, userId });
  }

  public getTeachingSubjects() {
    return this.requestService.get<Array<Subject>>(`subjects/teaching`);
  }

  public getLearningSubjects() {
    return this.requestService.get<Array<LearningSubjectAndGrades>>(`subjects/learning`);
  }

  public get(subjectId: number) {
    return this.requestService.get<SubjectStudents>(`subjects/students/${subjectId}`);
  }

  public addStudent(subjectId: number, userId: number) {
    return this.requestService.put(`subjects/student`, { subjectId, userId });
  }

  public addGrade(userLearningSubjectId: number, grade: number) {
    return this.requestService.put(`subjects/grade`, { userLearningSubjectId, grade });
  }

  public finalGrade(userLearningSubjectId: number) {
    return this.requestService.put(`subjects/final-grade/` + userLearningSubjectId);
  }

}