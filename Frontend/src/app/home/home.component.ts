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
  showDetails = false;
  showDungGuide= false;
  showItemGuide = false;
  showBestiaryGuide = false;
  showCraftGuide = false;
  showPassGuide = false;
  showEnchGuide = false;
  showBatGuide = false;
  dungeonTip="Battles take place automatically in various different themed areas to acquire loot bags.\n"+
              "'Regular' Loot bags drop at a 25% chance per monster, 'Boss' Loot bags drop at a 5% chance from bosses.\nOpen the loot bags in the 'Inventory'"+
              "tab to receive crafting materials assigned to that type of loot bag.\n Battles in dungeon's will continue infinitely until the user clicks on the 'leave' button.\n"+
              "If 'leave' is clicked during battle, the battle simulations keeps going until the user faints or the monster faints.\n Dying in battle causes you"+
              " to drop half of your gained resources.";

  craftingTip="Crafted equipment scales based on the crafting materials used.\n Currently potions only have 3 tiers but more will be added.\n Misc crafting "+
              "planned.\n Unlock achievements by crafting every possible weapon.";

  battleTip ="Idle battle system which fights monsters and collects loot on its own.\nAutomated potion usage with user specified Hp threshold.\n Damage is calculated as every point in attack is negated by a point of defense.\n"+
             "Critical hits do 1.5x damage at base.\n Evasion and hit chance currently not implemented.\n Once a monster dies a new one will spawn automatically.\n"


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
  resetInfo(){
    this.showDetails = false;
    this.showDungGuide= false;
    this.showItemGuide = false;
    this.showBestiaryGuide = false;
    this.showCraftGuide = false;
    this.showPassGuide = false;
    this.showEnchGuide = false;
    this.showBatGuide = false;
  }
  showDungeonGuide(){
    this.showDetails=true;
    this.showDungGuide = true;
  }
  showCraftingGuide(){
    this.showDetails=true;
    this.showCraftGuide = true;
  }
  showBattleGuide(){
    this.showDetails=true;
    this.showBatGuide = true;
  }
  showEnchantGuide(){
    this.showDetails=true;
    this.showEnchGuide=true;
  }
  showItemIndex(){
    this.showDetails=true;
    this.showItemGuide=true;
  }
  showBestiary(){
    this.showDetails=true;
    this.showBestiaryGuide = true;
  }
  showPassiveGuide(){
    this.showDetails=true;
    this.showPassGuide = true;
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
