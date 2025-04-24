import {Component, inject, OnInit} from '@angular/core';
import {CommonModule, NgOptimizedImage} from '@angular/common';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import {UserService} from "../../user/user.service";
import {UserDTO} from "../../user/user.model";


@Component({
  selector: 'app-header',
  imports: [CommonModule, NgOptimizedImage, RouterLink],
  templateUrl: './header.component.html'
})
export class HeaderComponent implements OnInit {

  user: UserDTO | undefined
  router = inject(Router);

  constructor(
    private userService: UserService
  ) {
  }

  ngOnInit(): void {
    this.userService.checkUser()
      .subscribe(value => this.user = value);
  }

  isUserLoggedIn(): boolean {
    return this.user != undefined || this.user != null;
  }

  getUserName(): string {
    return <string>this.user?.name;
  }

  logout() {
    this.user = undefined;
    this.userService.logout().subscribe({
      next: () => this.router.navigate(['/'])
    });
  }
}
