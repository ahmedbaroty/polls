import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {HomeComponent} from './home/home.component';
import {MeComponent} from './me/me.component';
import {PollListComponent} from './poll-list/poll-list.component';
import {PollCreationComponent} from './poll-creation/poll-creation.component';
import {LoginComponent} from './login/login.component';
import {SignupComponent} from './signup/signup.component';
import {PollComponent} from './poll/poll.component';
import {ContactComponent} from './contact/contact.component';
import {AboutComponent} from './about/about.component';
import {HelpComponent} from './help/help.component';

const routes: Routes = [
  {path: '' , component: HomeComponent},
  {path: 'me' , component: MeComponent},
  {path: 'poll/list' , component: PollListComponent},
  {path: 'poll/list/createdBy' , component: PollListComponent},
  {path: 'poll/list/votedBy' , component: PollListComponent},
  {path: 'poll/create' , component: PollCreationComponent},
  {path: 'login' , component: LoginComponent},
  {path: 'signup' , component: SignupComponent},
  {path: 'poll/:id' , component: PollComponent},
  {path: 'contact' , component: ContactComponent},
  {path: 'about' , component: AboutComponent},
  {path: 'help' , component: HelpComponent}
  ];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
