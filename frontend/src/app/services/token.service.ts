import { Injectable } from "@angular/core";
import { BehaviorSubject } from "rxjs";

const TOKEN_STORAGE_KEY = 'school_user_token';

@Injectable({ providedIn: 'root' })
export class TokenStorageService {

  private token: BehaviorSubject<string | null> = new BehaviorSubject<string | null>(
    localStorage.getItem(TOKEN_STORAGE_KEY) ?? null
  );

  public get observable() {
    return this.token.asObservable();
  }

  public get value() {
    return this.token.value;
  }

  public next(value: string | null) {
    this.token.next(
      value
    );

    if (value === null) {
      localStorage.clear();
    } else {
      localStorage.setItem(
        TOKEN_STORAGE_KEY,
        value
      );
    }
  }
}