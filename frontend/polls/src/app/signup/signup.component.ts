import {Component, OnInit, ViewChild} from '@angular/core';
import {NgForm} from '@angular/forms';
import {ApiService} from '../services/api.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {

  constructor(private apiService: ApiService, private route: Router) {
  }

  loader: Boolean = false;

  ngOnInit() {
  }

  onSubmit(f: NgForm) {

    if (f.valid) {
      const signupRequest = {
        name: f.value.name,
        username: f.value.username,
        email: f.value.email,
        password: f.value.password
      };

      // display loader
      this.loader = true;

      this.apiService.register(signupRequest).toPromise().then((signupResponse: any) => {
        this.loader = false;
        console.log(signupResponse);
        alert(`Register Success :  ${signupResponse.success} \nRegister Message : ${signupResponse.message}\n go to login`);
        this.route.navigate([`verify/${signupResponse.userId}`]);

      }).catch(error => {
        this.loader = false;
        // display alert error
        alert('ERROR Not Valid form \n' + error.message);
      });

    }
    else {
      alert('Not Valid form');
      // display alert form not valid
    }

  }

}
