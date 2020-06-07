export class RegisterUserRequest {
  constructor(
    public name: string,
    public email: string,
    public password: string
  ) {}
}
