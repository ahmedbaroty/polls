import { Component, OnInit } from '@angular/core';
import {AuthService} from '../services/auth.service';
import {Router} from '@angular/router';
import {PollListComponent} from '../poll-list/poll-list.component';

@Component({
  selector: 'app-me',
  templateUrl: './me.component.html',
  styleUrls: ['./me.component.css']
})
export class MeComponent implements OnInit {

  constructor(private authService : AuthService , private router:Router) { }

  ngOnInit() {
  }

  getUserName(){
    return this.authService.getUsername();
  }

  getName(){
    return this.authService.getUserSummary().name;
  }

  getLogoChar(){
    return this.authService.getUserSummary().name.substring(0,1).toUpperCase();
  }

  goToCreatedPolls() {
    this.router.navigate([`poll/list/createdBy`]);
  }

  goToCreatedVotes() {
    this.router.navigate([`poll/list/votedBy`]);
  }

}
