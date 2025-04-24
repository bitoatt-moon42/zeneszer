export class UserDTO {

  constructor(data:Partial<UserDTO>) {
    Object.assign(this, data);
  }

  id?: string|null;
  name?: string|null;
  password?: string|null;

}
