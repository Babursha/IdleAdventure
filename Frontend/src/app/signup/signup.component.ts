import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { Validators, FormBuilder, FormGroup, FormControl } from '@angular/forms';


@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {
  model:UserViewModel = {
  username:'',
  email:'',
  password:'',
  password2:''
  };
  samePasswordCheck = true;
  passwordErrorCheck = false;
  userExistError = false;
  usernameErrorCheck = false;
  emailErrorCheck = false;
  emailExistCheck = false;
  successfulRegister = false;
  errorCheck= false;
  emailRegex = RegExp("^\\S+\\@\\S+$");
  passwordRegex = RegExp("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d\\D]{8,}$");

  constructor(private http: HttpClient,private router: Router) { }

  ngOnInit() {
  }

  passwordDescription(){
  return "*Password must be at least 8 characters. \n *Must have 1 uppercase letter.\n *Must have 1 lowercase letter \n *Must have 1 number."
  }
  clearEmailErrors(){
    this.emailErrorCheck=false;
    this.emailExistCheck=false;
  }
  clearUserErrors(){
    this.userExistError=false;
    this.usernameErrorCheck=false;
  }

  createUser(): void {
  console.log(this.model.email);
  console.log((this.emailRegex.test(this.model.email)));
  if(!(this.emailRegex.test(this.model.email))){
    this.emailErrorCheck=true;
  }
  console.log((this.passwordRegex.test(this.model.password)));
  if(!(this.passwordRegex.test(this.model.password))){
    this.passwordErrorCheck = true;
  }
  if(!(this.model.password === this.model.password2)){
    this.samePasswordCheck = false;
  }

  if(!(this.model.username.length >= 1 && this.model.username.length <= 20)){
    this.usernameErrorCheck = true;
  }

  if(this.usernameErrorCheck == false && this.samePasswordCheck == true && this.emailErrorCheck == false && this.passwordErrorCheck ==false){
    let url = "/register"
    this.http.post(url,this.model).subscribe(
      rest=>{
        this.successfulRegister = true;

      },
      err=>{
        if(err.error === "Username already exists."){
          this.userExistError = true;
        }
        if(err.error === "Email already registered."){
          this.emailExistCheck = true;

        }
      }
      );
  }
}
}

export interface UserViewModel{
  username:string;
  email:string;
  password:string;
  password2:string;
}
