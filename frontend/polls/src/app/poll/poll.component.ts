import {Component, OnInit} from '@angular/core';
import {NgForm} from '@angular/forms';
import {ApiService} from '../services/api.service';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthService} from '../services/auth.service';

@Component({
  selector: 'app-poll',
  templateUrl: './poll.component.html',
  styleUrls: ['./poll.component.css']
})
export class PollComponent implements OnInit {

  poll = {
    choices: [{
      id: null,
      text: '',
      voteCount: null
    }],
    creationDateTime: '',
    expirationDateTime: '',
    expired: false,
    id: null,
    question: '',
    selectedChoice: null,
    totalVotes: null,
    userSummary: {
      id: null,
      name: '',
      username: ''
    }
  };
  loader = true;

  constructor(private router: Router, private apiService: ApiService, private activeRouter: ActivatedRoute, private authService: AuthService) {
  }

  ngOnInit() {
    this.apiService.getPollById(this.activeRouter.snapshot.params['id']).toPromise()
      .then((data: any) => {
        this.loader = false;
        this.poll = data;
      }).catch(error => {
      alert('error ' + error.message);
    });
  }

  getClass(choice) {
    const ratio = this.getRatio(choice);
    if (ratio < 25) {
      return 'red';
    }
    else if (ratio >= 25 && ratio < 50) {
      return 'aqua';
    } else {
      return 'green';
    }
  }

  getRatio(choice) {
    if (this.poll.totalVotes !== 0)
      return choice.voteCount / this.poll.totalVotes * 100;
    return 0;
  }

  displayRatio(choice) {
    let num = this.getRatio(choice);
    return num.toFixed(1);
  }

  getUserLogo() {
    return this.poll.userSummary.name.substring(0, 1).toLocaleUpperCase();
  }

  onVote(f: NgForm) {

    if (this.authService.isAuthorized()) {
      let value = f.value.vote;
      if (value !== '') {
        this.loader = true;
        // todo make a cast vote
        let voteRequest = {
          choiceId: value
        };

        this.apiService.createCastVote(voteRequest, this.poll.id).toPromise().then((data: any) => {
          this.loader = false;
          this.poll = data;
        }).catch((error) => {
          alert('Error ' + error.message);
        });
      } else {
        alert('Please Choose an Choice');
      }
    } else {
      alert('You Must Login Before Vote');
      this.router.navigate([`login`]);
    }
  }

  isSelectedChoice() {
    if (this.poll.selectedChoice) {
      return true;
    }
    return false;
  }
}
