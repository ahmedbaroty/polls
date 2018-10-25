import {Component, OnInit} from '@angular/core';
import {ApiService} from '../services/api.service';
import {AuthService} from '../services/auth.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-poll-list',
  templateUrl: './poll-list.component.html',
  styleUrls: ['./poll-list.component.css']
})
export class PollListComponent implements OnInit {

  polls = null;
  loader = true;

  constructor(private apiService: ApiService , private authService: AuthService , private router: Router) {
  }

  ngOnInit() {
    if(this.router.url === "/poll/list/createdBy"){
      this.getCreatedByPolls();
    }else if(this.router.url === "/poll/list/votedBy"){
      this.getVotedByPolls();
    }else{
      this.getAll();
    }
  }

  vote(id) {
    if(this.authService.isAuthorized()){
        this.router.navigate([`poll`])
    }


  }

  goToPollPage(poll) {
    this.router.navigate([`poll/${poll.id}`]);
  }

  getButtonTitle(expired: boolean) {
    if(expired){
      return 'Poll Details'
    }else{
      return 'Vote'
    }
  }


  getAll(){
    this.apiService.getAllPolls().toPromise().then(data => {
      this.loader = false;
      this.polls = data;
    }).catch(error => {
      this.loader = false;
      alert(`Error Response : \n ${error.message}`);
    });
  }

  getCreatedByPolls(){
    this.apiService.getPollCreateBy(this.authService.getUsername()).toPromise().then(data => {
      this.loader = false;
      this.polls = data;
    }).catch(error => {
      this.loader = false;
      alert(`Error Response : \n ${error.message}`);
    });
  }

  getVotedByPolls(){
    this.apiService.getUserVotes(this.authService.getUsername()).toPromise().then(data => {
      this.loader = false;
      this.polls = data;
    }).catch(error => {
      this.loader = false;
      alert(`Error Response : \n ${error.message}`);
    });

  }



}
