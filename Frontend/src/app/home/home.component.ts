import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { DataService } from '../data.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})

export class HomeComponent implements OnInit {
  model: User = {
    username:'',
    level:0,
    gold:0,
    gold_progress:0,
    xp_progress:0,
    xp_current:0,
    xp_lvl_up:0
  };

  counter:number = 0;
  counterXp:number = 0;
  interval;
  showGoldCollected = false;
  showXpCollected = false;
  authorized = false;

  constructor(private http: HttpClient,private router: Router,public dataService: DataService) { }

  ngOnInit() {
    this.getUser();

  }

  ngOnDestroy(){
    this.saveGold();
    this.saveXp();
  }

  getUser(){
    let url = '/api/home';
    this.http.get<any>(url).subscribe(
          (rest:any)=>{
            console.log(rest);
            this.authorized = true;
            this.model = rest;
            this.dataService.gold=rest.gold;
            this.dataService.counter = this.model.gold_progress * .1;
            this.dataService.counterXp = this.model.xp_progress *.05;
            this.dataService.xp_progress_max = Math.floor(this.model.xp_lvl_up / 1.5);
            if(this.dataService.counterStarted == false){
              this.dataService.xp_progress = rest.xp_progress;
              this.dataService.gold_progress = rest.gold_progress;
              this.dataService.startGoldTimer();
              this.dataService.startXpTimer();
              this.dataService.updateXpBar();
              this.dataService.counterStarted= true;
            }

          },
          err=>{
          }
    );
  }

  collectGold(){
    this.showGoldCollected= true;
    setTimeout(() => {
       this.showGoldCollected = false;
    }, 2000);
    let url = "/api/home/collectGold";
    let data = this.model.gold + this.dataService.gold_progress;
    this.http.post(url,data).subscribe(
      (rest)=>{


      },
      err=>{
       console.log('error gold');
      }
    );
    this.dataService.counter = 0;
    this.model.gold = this.model.gold + this.dataService.gold_progress;
    this.dataService.gold = this.dataService.gold+ this.dataService.gold_progress;
    this.model.gold_progress = 0;
    this.dataService.gold_progress = 0;
  }

  collectXp(){
  this.showXpCollected= true;
     setTimeout(() => {
       this.showXpCollected = false;
  }, 2000);
  let url = "/api/home/collectXp";
  console.log(this.model.xp_current);
  if (this.model.xp_current + this.dataService.xp_progress >= this.model.xp_lvl_up){
      let xp_carry = (this.model.xp_current+this.dataService.xp_progress) - this.model.xp_lvl_up;
      this.model.level +=1;
      this.model.xp_lvl_up = Math.floor((this.model.xp_lvl_up * 2)/1.5);
      this.dataService.xp_progress_max = Math.floor(this.model.xp_lvl_up / 1.5);
      this.model.xp_current = xp_carry;
  }
  else{
      this.model.xp_current = this.model.xp_current+this.dataService.xp_progress;
  }
  console.log(this.model.xp_current);
  let data = {'xpCurrent':this.model.xp_current,'level':this.model.level,'xpLvlUp':this.model.xp_lvl_up};
  this.http.post(url,data).subscribe(
    (rest)=>{


    },
    err=>{
     console.log('error xp');
    }
  );
  this.dataService.counterXp = 0;
  this.model.xp_progress = 0;
  this.dataService.xp_progress = 0;
  this.dataService.updateXpBar();
  }

  saveGold(){
    let url='/api/home/goldProgress';
    this.http.post(url,this.dataService.gold_progress).subscribe(
      (rest:any)=>{
      console.log(rest);

      },
      error=>{
        console.log('error');
      }

    );
  }

  saveXp(){
    let url='/api/home/xpProgress';
    this.http.post(url,this.dataService.xp_progress).subscribe(
      (rest:any)=>{
      console.log(rest);

      },
      error=>{
        console.log('error');
      }

    );
  }

}
export interface User{
username:string;
level:number;
gold:number;
gold_progress:number;
xp_progress:number;
xp_current:number;
xp_lvl_up:number;
}
