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
    this.apiService.getAllPolls().toPromise().then(data => {
      this.loader = false;
      this.polls = data;
    }).catch(error => {
      this.loader = false;
      alert(`Error Response : \n ${error.message}`);
    });
  }


  vote(id) {
    if(this.authService.isAuthorized()){
        this.router.navigate([`poll`])
    }


  }

  goToPollPage(poll) {
    if(!poll.expired){
      if(this.authService.isAuthorized()){
        this.router.navigate([`poll/${poll.id}`]);
      } else{
        alert("You Must Login Before Vote");
        this.router.navigate([`login`]);
      }
    }else{
      alert("This poll is Expired!");
    }
  }
}
