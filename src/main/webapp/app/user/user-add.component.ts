import {Component, inject, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ActivatedRoute, Router} from '@angular/router';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {InputRowComponent} from 'app/common/input-row/input-row.component';
import {UserService} from 'app/user/user.service';
import {UserDTO} from 'app/user/user.model';
import {ErrorHandler} from 'app/common/error-handler.injectable';


@Component({
  selector: 'app-user-add',
  imports: [CommonModule, ReactiveFormsModule, InputRowComponent],
  templateUrl: './user-add.component.html'
})
export class UserAddComponent implements OnInit {

  userService = inject(UserService);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  isLogin: boolean | undefined;

  constructor(
    private route: ActivatedRoute
  ) {
  }

  ngOnInit(): void {
    let action = this.route.snapshot.params['action'];
    if ('login' === action) {
      this.isLogin = true;
    }
  }

  addForm = new FormGroup({
    name: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
    password: new FormControl(null, [Validators.required, Validators.maxLength(255)])
  }, {updateOn: 'submit'});

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      created: 'Registering user was successful.',
      noUser: 'No user found.',
      userExists: 'User already exists.',
      loggedIn: 'You are successfully logged in.'
    };
    return messages[key];
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.addForm.markAllAsTouched();
    if (!this.addForm.valid) {
      return;
    }
    const data = new UserDTO(this.addForm.value);
    if (this.isLogin) {
      this.userService.authenticate(data.name + '', data.password + '')
        .subscribe({
          next: () => {
            this.router.navigate(['/'], {
              state: {
                msgSuccess: this.getMessage('loggedIn')
              }
            }).then(() => {
              window.location.reload();
            });
          },
          error: (error) => this.router.navigate(['users/add'], {
            state: {
              msgError: this.getMessage('noUser')
            }
          })
        });
    } else {
      this.userService.createUser(data)
        .subscribe({
          next: () => this.router.navigate(['users/add'], {
            state: {
              msgSuccess: this.getMessage('created')
            }
          }),
          error: (error) => this.router.navigate(['users/add'], {
            state: {
              msgError: this.getMessage('userExists')
            }
          })
        });
    }
  }

}
