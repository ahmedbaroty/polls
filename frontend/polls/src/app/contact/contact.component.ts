import {Component, OnInit} from '@angular/core';
import {NgForm} from '@angular/forms';
import {invalid} from '@angular/compiler/src/render3/view/util';

@Component({
  selector: 'app-contact',
  templateUrl: './contact.component.html',
  styleUrls: ['./contact.component.css']
})
export class ContactComponent implements OnInit {

  constructor() {
  }

  ngOnInit() {

  }

  sendMail(f: NgForm) {
    if(f.valid){
      let mail = {
        from: f.value.email,
        subject: f.value.subject,
        country: f.value.country,
        name:f.value.name
      };
      console.log(mail);
    }else{
      alert('Form is invalid')
    }
  }
}
