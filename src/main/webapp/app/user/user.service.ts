import {Injectable, inject} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from 'environments/environment';
import {UserDTO} from 'app/user/user.model';


@Injectable({
  providedIn: 'root',
})
export class UserService {

  http = inject(HttpClient);
  resourcePath = environment.apiPath + '/api/users';

  getAllUsers() {
    return this.http.get<UserDTO[]>(this.resourcePath);
  }

  checkUser() {
    return this.http.get<UserDTO>(this.resourcePath + '/loggedInUser');
  }

  logout() {
    return this.http.get<void>(this.resourcePath + '/logout');
  }

  authenticate(name: string, password: string) {
    return this.http.get<UserDTO>(this.resourcePath + '/' + name + '/' + password);
  }

  getUser(id: string) {
    return this.http.get<UserDTO>(this.resourcePath + '/' + id);
  }

  createUser(userDTO: UserDTO) {
    return this.http.post<string>(this.resourcePath, userDTO);
  }

  updateUser(id: string, userDTO: UserDTO) {
    return this.http.put<string>(this.resourcePath + '/' + id, userDTO);
  }

  deleteUser(id: string) {
    return this.http.delete(this.resourcePath + '/' + id);
  }

}
