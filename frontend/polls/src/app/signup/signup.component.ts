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

  constructor(private apiService : ApiService , private route: Router) { }

  loader:Boolean = false;
  ngOnInit() {
  }

  onSubmit(f: NgForm){

    if(f.valid){
      const signupRequest = {
        name: f.value.name,
        username: f.value.username,
        email: f.value.email,
        password: f.value.password
      };

      // display loader
      this.loader = true;

      setTimeout(()=>{

        // stop loader
        this.loader = false;
        this.route.navigate([`login`]);

      } , 3000);
      /*
            this.apiService.register(signupRequest).toPromise().then(signupResponse =>{

              // display alert success
              // go to login page
            }).catch(error =>{

              // display alert error
            });
      */
    }
    else{
      alert("Not Valid form");
      // display alert form not valid

    }

  }

}
