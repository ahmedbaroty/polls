import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ApiService} from '../services/api.service';

@Component({
  selector: 'app-verify-mail',
  templateUrl: './verify-mail.component.html',
  styleUrls: ['./verify-mail.component.css']
})
export class VerifyMailComponent implements OnInit {

  loader = false;
  status = 'init';


  constructor(private activeRouter: ActivatedRoute , private apiService:ApiService , private router:Router) { }

  ngOnInit() {
    const token:String = this.activeRouter.snapshot.params['token'];

    if(token !== undefined){
      status = 'verify';
      this.loader = true;
      this.apiService.sendTokenToVerfiyUserTokenRequest(token).toPromise().then(data => {
        this.loader = false;
        this.router.navigate(['login'])
      }).catch(error => {
        console.log(error)
      });

    }
  }

  sendMail(){
    this.loader = true;
    const id:String = this.activeRouter.snapshot.params['id'];
    this.apiService.sendVerifyMailRequest(id).toPromise().then(data => {
      this.loader = false;
      alert("Verify your email")
    }).catch(error => {
      console.log(error)
    });
  }

}
