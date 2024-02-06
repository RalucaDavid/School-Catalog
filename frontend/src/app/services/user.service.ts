import { Injectable } from "@angular/core";
import { RequestService } from "./request.service";
import { User } from "../interfaces/user.interface";
import { UserSearch } from "../interfaces/usersearch.interface";

@Injectable({ providedIn: 'root' })
export class UserService {
  
  constructor(
    private readonly requestService: RequestService
  ) { }

  public search(keyword: string) {
    return this.requestService.get<Array<UserSearch>>(`users/search/${keyword}`);
  }

}