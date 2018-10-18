import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})

export class AuthService {

  private jwt = {
    type: null ,
    token: null
  };

  private userSummary = {id:undefined , name: '',username: ''};

  constructor() { }

  login(loginResponse){
    this.jwt.token = loginResponse.accessToken;
    this.jwt.type = loginResponse.tokenType;

  }

  logout(){
    this.jwt.token = null;
    this.jwt.type = null;
    this.userSummary = {id:undefined , name: '',username: ''};
  }

  isAuthorized(){
    if(this.jwt.token!==null){
      return true;
    }else{
      return false;
    }
  }

  getToken(){
    if(this.jwt.token!==null){
      return `${this.jwt.type} ${this.jwt.token}`;
    }else{
      return '';
    }
  }

  getUsername(){
    return this.userSummary.username;
  }

  getUserSummary(){
    return this.userSummary;
  }

  setUserSummary(userSummary) {
    this.userSummary = userSummary;
  }
}
