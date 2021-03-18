import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http'
import { Router } from '@angular/router';

@Component({
  selector: 'app-dungeons',
  templateUrl: './dungeons.component.html',
  styleUrls: ['./dungeons.component.css']
})
export class DungeonsComponent implements OnInit {
  forest = [];
  desert = [];
  cave=[];
  volcano=[];

  showDetailsForest=false;
  showDetailsDesert=false;
  showDetailsCave=false;
  showDetailsVolcano=false;
  pulse = false;
  monster=[];
  monsterBasic = false;
  monsterBoss = false;


  constructor(private http: HttpClient,private router:Router) { }

  ngOnInit() {
    this.getForestDetails();
    this.getDesertDetails();
  }
  ngOnDestroy(){

  }

    showMonster(monster){
      this.monster=monster;
    }

  getForestDetails(){
  let url = "/api/dungeons/forestDetails"
  this.http.get<any>(url).subscribe(
      rest=>{
      console.log(rest);
      this.forest = rest;
      },
      err=>{
      this.router.navigate(['/error']);
      }
  );
  }

    getDesertDetails(){
    let url = "/api/dungeons/desertDetails"
    this.http.get<any>(url).subscribe(
        rest=>{
        this.desert = rest;
        },
        err=>{
        this.router.navigate(['/error']);
        }
    );
    }

}
