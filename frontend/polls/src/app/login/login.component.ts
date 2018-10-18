import {Component, OnInit} from '@angular/core';
import {NgForm} from '@angular/forms';
import {ApiService} from '../services/api.service';
import {Router} from '@angular/router';
import {AuthService} from '../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loader: Boolean = false;

  constructor(private apiService: ApiService, private route: Router, private authService: AuthService) {
  }

  ngOnInit() {
  }

  onSubmit(f: NgForm) {
    if (f.valid) {
      // display loader
      this.loader = true;

      const loginRequest = {
        usernameOrEmail: f.value.usernameOrEmail,
        password: f.value.password
      };

      this.apiService.login(loginRequest).toPromise().then(loginResponse => {

        // set auth service
        this.authService.login(loginResponse);


        this.apiService.getMe().toPromise().then(userSummary => {

          // stop loader
          this.loader = false;

          this.authService.setUserSummary(userSummary);
            // go to home page
          this.route.navigate(['']);

        }).catch(error => {

          // display alert error
          alert(`Me Data : \n ${error.message}`);

          // stop loader
          this.loader = false;
        });


      }).catch(error => {

        // display alert error
        alert(`Login Error : \n ${error.message}`);

        // stop loader
        this.loader = false;
      });

    }
    else {
      // stop loader
      this.loader = false;
      // display alert form not valid
      alert('Login Form Is Invalid');
    }
  }

}
