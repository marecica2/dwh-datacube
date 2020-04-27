import { Subscription } from "rxjs";
import { Component, Input, OnDestroy, OnInit, TemplateRef } from "@angular/core";
import { AuthService } from "../../../auth/auth.service";
import { User } from "../../../shared/user.model";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit, OnDestroy {
  @Input() public placeholderRef: TemplateRef<any>;
  public userSub: Subscription;
  public user: User;
  public isAuthenticated = false;

  constructor(private authService: AuthService) {
  }

  ngOnInit(): void {
    this.userSub = this.authService.userSubject.subscribe((user) => {
      this.isAuthenticated = !!user;
      this.user = user;
    });
  }

  ngOnDestroy(): void {
    this.userSub.unsubscribe();
  }

  onLogout() {
    this.authService.logout();
  }
}
