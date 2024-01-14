import { Injectable } from "@angular/core";
import { RequestService } from "./request.service";
import { Subject } from "../interfaces/subject.interface";

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
}