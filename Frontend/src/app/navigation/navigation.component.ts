import { Component, OnInit } from '@angular/core';
import { DataService } from '../data.service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.css']
})
export class NavigationComponent implements OnInit {

  interval;
  data;
  user;
  gold=0;
  updateGold=false;

  constructor(public dataService:DataService,private http:HttpClient) { }

  ngOnInit() {
    this.setData();
    this.gold = this.dataService.gold;
  }
  ngOnDestroy(){
    clearInterval(this.interval);

  }

  setData(){
    this.interval = setInterval(() => {
        if(this.gold != this.dataService.gold)
          this.updateGold=true;
        else
          this.updateGold=false;
        this.gold = this.dataService.gold;

      },1000)
    }


  logout(){
    let url = "/logout";
    this.http.post(url,{}).subscribe(
    (rest:any)=>{
    },
    err=>{
    }
    )
  }


}
