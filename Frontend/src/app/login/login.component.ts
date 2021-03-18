import { Component, OnInit, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})


export class LoginComponent implements OnInit {

  model:UserViewModel = {
  username:'',
  password:''
  };
  failedLogin = false;
  constructor(private http: HttpClient,private router: Router) { }

  ngOnInit() {
  }

  loginUser(): void {
    let url = "/api/login"
    this.http.post(url,this.model).subscribe(
      (rest : any)=>{
      console.log('success');
          this.router.navigateByUrl("/home");
      },
          err=>{this.failedLogin= true}
    );

  }

}
export interface UserViewModel{
  username:string;
  password:string;
}
