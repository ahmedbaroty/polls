import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { LoginComponent } from './login/login.component';
import { SignupComponent } from './signup/signup.component';
import { HomeComponent } from './home/home.component';
import { PollComponent } from './poll/poll.component';
import { MeComponent } from './me/me.component';
import { SidebarComponent } from './sidebar/sidebar.component';
import { PollCreationComponent } from './poll-creation/poll-creation.component';
import { PollListComponent } from './poll-list/poll-list.component';
import { AboutComponent } from './about/about.component';
import { ContactComponent } from './contact/contact.component';
import { HelpComponent } from './help/help.component';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { VerifyMailComponent } from './verify-mail/verify-mail.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    LoginComponent,
    SignupComponent,
    HomeComponent,
    PollComponent,
    MeComponent,
    SidebarComponent,
    PollCreationComponent,
    PollListComponent,
    AboutComponent,
    ContactComponent,
    HelpComponent,
    VerifyMailComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
