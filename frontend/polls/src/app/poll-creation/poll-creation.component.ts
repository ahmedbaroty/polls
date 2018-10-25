import { Component, OnInit } from '@angular/core';
import {NgForm} from '@angular/forms';
import {ApiService} from '../services/api.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-poll-creation',
  templateUrl: './poll-creation.component.html',
  styleUrls: ['./poll-creation.component.css']
})
export class PollCreationComponent implements OnInit {

  numberOfChoices = 2;
  choices = ["","","","","",""];
  numberOfDays = 0;
  numberOfHours = 0;
  constructor(private apiService: ApiService , private router: Router) { }

  ngOnInit() {
  }


  onSubmit(f : NgForm){
    if(f.valid &&(this.numberOfChoices > 0 || this.numberOfDays > 0)){

      // todo create poll request

      let tempChoices = [];
      for(let i=1;i<=f.value.numberOfChoices;i++){
        tempChoices.push({text: f.value["choice"+i]})
      }

      let pollRequest = {
        question : f.value.question,
        choices : tempChoices,
        pollLength : {
          days: f.value.numberOfDays,
          hours : f.value.numberOfHours
        }
      };

      this.apiService.pollCreation(pollRequest).toPromise().then((data:any) => {

        alert(`Create Poll Success : ${data.success}\nMessage : ${data.message}`);

        this.router.navigate([`poll/list`])

      }).catch(error =>{

        alert(`Error : ${error.message}`);
      });

    }else{
      alert("Form Not Valid")
    }
  }

}
