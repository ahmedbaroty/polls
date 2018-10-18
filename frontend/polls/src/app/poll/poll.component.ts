import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-poll',
  templateUrl: './poll.component.html',
  styleUrls: ['./poll.component.css']
})
export class PollComponent implements OnInit {

  poll  = {
    "id": 4,
    "question": "Do you agree with the latest change in egypt economy ? ",
    "choices": [
      {
        "id": 13,
        "text": "YES",
        "voteCount": 1200
      },
      {
        "id": 13,
        "text": "May By",
        "voteCount": 700
      },

      {
        "id": 14,
        "text": "NO",
        "voteCount": 100
      }
    ],
    "userSummary": {
      "id": 1,
      "username": "ahmedbaroty",
      "name": "ahmed"
    },
    "creationDateTime": "2018-10-17T09:29:43Z",
    "expirationDateTime": "2018-10-25T08:29:43Z",
    "totalVotes": 2000,
  };

    constructor() { }

  ngOnInit() {
  }

  getClass(choice) {
   const ratio = this.getRatio(choice);
      if(ratio < 25){
        return "red";
      }
      else if (ratio >= 25 && ratio < 50){
        return "aqua";
      }else{
        return "green";
      }


  }

  getRatio(choice) {

      return choice.voteCount / this.poll.totalVotes * 100;

  }
}
