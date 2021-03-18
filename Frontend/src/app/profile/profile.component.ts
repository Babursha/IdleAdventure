import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  model:password = {
  password:''
  };


  constructor(private http:HttpClient,private router:Router) { }

  ngOnInit() {

  }

  changePassword(){
    let url= "/api/account/changePassword"
    let data = {'username':localStorage.getItem('username'),'password':this.model.password};
    this.http.post(url,data).subscribe(
       rest=>{

       },
       err=>{

       }
       );
  }

}
export interface password{
  password:string;
}
