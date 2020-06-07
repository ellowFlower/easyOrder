import {AbstractControl} from '@angular/forms';

export class RegisterTableRequest {
  constructor(
    public name: string,
    public email: string,
    public password: string,
    public seats: number
  ) {}
}
