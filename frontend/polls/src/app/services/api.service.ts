import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  root = 'http://localhost:5000';

  api = {
    register: `${this.root}/api/auth/signup`,
    login: `${this.root}/api/auth/signin`,
    pollCreation: `${this.root}/api/polls`,
    allPolls: `${this.root}/api/polls`,
    pollCreatedBy: `${this.root}/api/users/polls?username=`,
    me: `${this.root}/api/user/me`,
    userProfile: `${this.root}/api/users/`,
    userVotes: `${this.root}/api/users/votes?username=`,
    checkEmailAvailability: `${this.root}/api/user/checkEmailAvailability?email=`,
    checkUsernameAvailability: `${this.root}/api/user/checkUsernameAvailability?username=`,
    castVote: `${this.root}/api/polls/`,
    pollById:`${this.root}/api/polls/`
  };
  constructor(private http:HttpClient , private authService: AuthService) { }

  register(signupRequest){
    return this.http.post(this.api.register , signupRequest);
  }

  login(loginRequest){
    return this.http.post(this.api.login , loginRequest);
  }

  pollCreation(pollRequest){
    // create header with auth service
    return this.http.post(this.api.pollCreation , pollRequest ,
      {headers:{Authorization: this.authService.getToken()}});
  }
  getAllPolls(){
    return this.http.get(this.api.allPolls ,
      {headers:{Authorization: this.authService.getToken()}});
  }
  getPollCreateBy(username){
    return this.http.get(this.api.pollCreatedBy+username ,
      {headers:{Authorization: this.authService.getToken()}});
  }
  getMe(){
    return this.http.get(this.api.me,
      {headers:{Authorization: this.authService.getToken()}});
  }
  getUserProfile(username){
    return this.http.get(this.api.userProfile+username,
      {headers:{Authorization: this.authService.getToken()}});
  }
  getUserVotes(username){
    return this.http.get(this.api.userVotes+username,
      {headers:{Authorization: this.authService.getToken()}});
  }

  checkEmailAvailability(email){
    return this.http.get(this.api.checkEmailAvailability+email,
      {headers:{Authorization: this.authService.getToken()}});
  }

  checkUsernameAvailability(username){
    return this.http.get(this.api.checkUsernameAvailability+username,
      {headers:{Authorization: this.authService.getToken()}});
  }

  createCastVote(voteRequest , pollId){
    return this.http.post(this.api.castVote+pollId+'/votes',voteRequest,
      {headers:{Authorization: this.authService.getToken()}});
  }

  getPollById(pollId){
    return this.http.get(this.api.pollById+pollId , {headers:{Authorization: this.authService.getToken()}})
  }

}
