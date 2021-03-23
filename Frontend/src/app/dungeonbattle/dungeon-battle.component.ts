import { Component, OnInit, Renderer2, ViewChild, ElementRef } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router,ActivatedRoute } from '@angular/router';
import { interval, Observable, Subscription } from 'rxjs';
import { MatSliderModule } from '@angular/material/slider';



@Component({
  selector: 'app-dungeon-battle',
  templateUrl: './dungeon-battle.component.html',
  styleUrls: ['./dungeon-battle.component.css']
})
export class DungeonBattleComponent implements OnInit {
  autoTicks = false;
  disabled = false;
  invert = false;
  max = 100;
  min = 0;
  showTicks = false;
  step = 5;
  thumbLabel = true;
  value = 0;
  vertical = false;

  tickInterval = 1;
  pData: Player = {'username':'','hp':0,'attack':0,'defense':0,'crit_chance':0,'gold':0,'level':0,'xp_gained':0,'xp_current':0,'xp_lvl_up':0};
  mData: Monster[]=[];
  pMaxHp: number;
  mMaxHp: number;
  mCurrentName:string;
  mCurrentHp:number;
  area:any;
  minorPotionAmount:number = 0;
  intermediatePotionAmount:number = 0;
  majorPotionAmount:number = 0;

  mAttack:boolean =false;
  pAttack:boolean = false;
  pHeal:boolean = false;
  playerTrue:boolean = true;
  wolfTrue:boolean = true;
  exit:boolean = false;
  monsterNum:number = 0;
  count:number = 1;
  goldCollected:number = 0;
  xpCollected:number = 0;
  basic:number = 0;
  boss:number = 0;
  scrollDown:number = 0;
  buttonText:string = "Leave Dungeon";
  interval;

  @ViewChild('div') div: ElementRef;

  constructor(private http: HttpClient,private router:Router,private renderer: Renderer2,private active:ActivatedRoute) { this.area = this.active.snapshot.firstChild.data.area;}

  ngOnInit() {
    if(this.area === 'forest'){
      this.forestBattle();
    }
    else if(this.area === 'desert'){
      this.desertBattle();
    }
    this.getPlayer();
    this.startBattle();
  }

  ngOnDestroy(){
    clearInterval(this.interval);
  }


  formatLabel(value: number) {
    return value + '%';
  }

  changeText(){
    this.interval = setInterval(() => {
      if(this.step > 0){
        this.step--;
        this.buttonText = this.step.toString();
      }
      if(this.step == 0){
        this.router.navigateByUrl("/dungeons");
      }
    },1500)
  }
  exitMessage(){
      if(this.pData.hp == 0){
        this.changeText();
      }
      else{
       const combatInfo: HTMLParagraphElement = this.renderer.createElement('p');
       combatInfo.innerHTML = "Preparing to leave the dungeon... awaiting result of last encounter.";
       this.renderer.appendChild(this.div.nativeElement, combatInfo);
       this.renderer.addClass(combatInfo, 'townDialog');
       }
  }

  updateLoot(){
    this.count = 3;
    const combatInfo: HTMLParagraphElement = this.renderer.createElement('p');
    if(this.pData.hp <= 0){
      this.goldCollected = Math.floor(this.goldCollected / 2);
      this.xpCollected = Math.floor(this.xpCollected / 2);
      this.basic = Math.floor(this.basic / 2);
      this.boss = Math.floor(this.boss / 2);
      combatInfo.innerHTML = "You faint and drop half of your gained loot...";
      this.renderer.appendChild(this.div.nativeElement, combatInfo);
      this.renderer.addClass(combatInfo, 'monsterCrit');
    }
    else{
       combatInfo.innerHTML = "You quickly gather all your loot and head back to town.";
       this.renderer.appendChild(this.div.nativeElement, combatInfo);
       this.renderer.addClass(combatInfo, 'townDialog');
    }
    this.scrollDown = this.div.nativeElement.scrollHeight;
    let url = "/api/home/collectGold";
    let data = this.pData.gold+this.goldCollected;
    this.http.post(url,data).subscribe(
      (rest)=>{


      },
      err=>{
       console.log('error gold');
      }
    );
    if (this.pData.xp_current + this.xpCollected >= this.pData.xp_lvl_up){
        let xp_carry = (this.pData.xp_current+this.xpCollected) - this.pData.xp_lvl_up;
        while(xp_carry > this.pData.xp_lvl_up){
          this.pData.level +=1;
          this.pData.xp_lvl_up = Math.floor((this.pData.xp_lvl_up * 2)/1.5);
          xp_carry = xp_carry - this.pData.xp_lvl_up;
          this.pData.xp_current = xp_carry;
        }
    }
    else{
        this.pData.xp_current = this.pData.xp_current+this.xpCollected;
    }

    url = "/api/home/collectXp";
    let xpData = {'xpCurrent':this.pData.xp_current,'level':this.pData.level,'xpLvlUp':this.pData.xp_lvl_up};
    this.http.post(url,xpData).subscribe(
      (rest:any)=>{

      },
      err=>{

      }
     );

    let lootData = {'type':this.area,'basic':this.basic,'rare':this.boss};
    url = "/api/dungeons/collectLoot";
    this.http.post(url,lootData).subscribe(
    (rest:any)=>{
      console.log('sent bags');

    },
    (err)=>{


    }
    );
  }

  forestBattle(){
    let url = "/api/dungeons/forest/battle";

    this.http.get<any>(url).subscribe(
      (rest:any)=>{
      console.log(rest);
      this.mData = rest;
      this.mCurrentName = this.mData[this.monsterNum].name;
      this.mMaxHp = this.mData[this.monsterNum].hp;
      this.mCurrentHp = this.mData[this.monsterNum].hp;
      },
      err=>{
      }
      );
  }

  desertBattle(){
    let url = "/api/dungeons/desert/battle";

    this.http.get<any>(url).subscribe(
      (rest:any)=>{
      console.log(rest);
      this.mData = rest;
      this.mCurrentName = this.mData[this.monsterNum].name;
      this.mMaxHp = this.mData[this.monsterNum].hp;
      this.mCurrentHp = this.mData[this.monsterNum].hp;
      },
      err=>{
      }
      );
  }


  getPlayer(){
    let url = '/api/home';
    let i = 0;
    this.http.get<any>(url).subscribe(
          (rest:any)=>{
            console.log(rest);
            this.pData = rest;
            this.pMaxHp = rest.hp;
          },
          err=>{
            console.log("Couldn't load user.");
          }
    );
    url = "/api/dungeons/get_potions";
    this.http.get<any>(url).subscribe(
          (rest:any)=>{
            console.log(rest);
            for(i = 0; i < rest.length;i++){
              if(rest[i].name === "Minor Healing Potion")
                this.minorPotionAmount = rest[i].amount;
              else if(rest[i].name === "Intermediate Healing Potion")
                this.intermediatePotionAmount = rest[i].amount;
              else if(rest[i].name === "Major Healing Potion")
                this.majorPotionAmount = rest[i].amount;
            }
          },
          err=>{
            console.log("Couldn't get users potions.");
          }
    );
  }



  startBattle(){
  console.log("in battle");

  if(this.count == 1){

    this.playerAnimation();
    setTimeout(() => {this.pAttack=false;},1000);
    setTimeout(() => {
    this.playerAttack();
     },1000);
  }
  else if(this.count ==0){
    this.monsterAnimation();
    setTimeout(() => {
    this.monsterAttack();
     },1000);

  }
  else if(this.count ==2){
    this.playerHealAnimation();
    setTimeout(() => {
      this.playerHeal();
     },1000);
  }
 }
    playerAnimation(){
       this.pAttack = true;
       this.mAttack = false;
       this.pHeal = false;


    }

    monsterAnimation(){
      this.mAttack= true;
      this.pHeal=false;
    }
    playerHealAnimation(){
      this.mAttack= false;
      this.pHeal = true;
    }
    playerHeal(){
      const combatInfo: HTMLParagraphElement = this.renderer.createElement('p');
        if((this.pData.hp/this.pMaxHp)*100 <= this.value){
          if(this.minorPotionAmount == 0 && this.intermediatePotionAmount == 0 && this.majorPotionAmount ==0){
            combatInfo.innerHTML = "You search your bag for more healing potions but none can be found!"
            this.renderer.appendChild(this.div.nativeElement, combatInfo);
            this.renderer.addClass(combatInfo, 'playerCrit');
          }
          else{
            if(this.pData.hp +10 > this.pMaxHp)
              this.pData.hp = this.pMaxHp;
            else
              this.pData.hp +=10;
             combatInfo.innerHTML=this.pData.username + "uses a potion and heals for 10 hp!";
              this.renderer.appendChild(this.div.nativeElement, combatInfo);
              this.renderer.addClass(combatInfo, 'playerCrit');
          }
          this.count = 1;
          this.pauseOneSecond();
        }
    }
    playerAttack(){
      const combatInfo: HTMLParagraphElement = this.renderer.createElement('p');
      let d = Math.floor((Math.random() * 100) + 1);

      if (d < this.pData.crit_chance){
        if(((this.pData.attack*1.5)-this.mData[this.monsterNum].defense) >= this.mData[this.monsterNum].hp){
          combatInfo.innerHTML = "**CRITICAL STRIKE** "+ this.pData.username+" attacked the " + this.mData[this.monsterNum].name + " for  " + ((this.pData.attack*1.5)-this.mData[this.monsterNum].defense) + " hp! The monster was defeated and you collect " + this.mData[this.monsterNum].goldDrop + " gold and " + this.mData[this.monsterNum].xpDrop + " xp.";
          this.renderer.appendChild(this.div.nativeElement, combatInfo);
          this.renderer.addClass(combatInfo, 'playerCrit');
          this.mData[this.monsterNum].hp=0;
          this.mCurrentHp=0;
          this.pauseOneSecond();
        }
        else{
          this.mData[this.monsterNum].hp = this.mData[this.monsterNum].hp - ((this.pData.attack*1.5)-this.mData[this.monsterNum].defense);
          combatInfo.innerHTML = "**CRITICAL STRIKE** "+ this.pData.username+" hit the " + this.mData[this.monsterNum].name + " for  " + ((Math.ceil(this.pData.attack*1.5))-this.mData[this.monsterNum].defense) + " hp!";
          this.renderer.appendChild(this.div.nativeElement, combatInfo);
          this.renderer.addClass(combatInfo, 'playerCrit');
          this.mCurrentHp= this.mData[this.monsterNum].hp;
          this.pauseOneSecond();
        }
      }
      else{
        if(this.pData.attack-this.mData[this.monsterNum].defense >= this.mData[this.monsterNum].hp){
          combatInfo.innerHTML = this.pData.username +" attacked the " + this.mData[this.monsterNum].name + " for  " + (this.pData.attack-this.mData[this.monsterNum].defense) + " hp! The monster was defeated and you collect " + this.mData[this.monsterNum].goldDrop + " gold and " + this.mData[this.monsterNum].xpDrop + " xp.";
          this.renderer.appendChild(this.div.nativeElement, combatInfo);
          this.mCurrentHp=0;
          this.mData[this.monsterNum].hp=0;
          this.pauseOneSecond();
        }
        else{
          if(this.pData.attack-this.mData[this.monsterNum].defense <= 0){
            combatInfo.innerHTML = this.pData.username +" attacks! " + this.mData[this.monsterNum].name + " for 0 hp!";
            this.renderer.appendChild(this.div.nativeElement, combatInfo);
            this.pauseOneSecond();
          }
          else{
            combatInfo.innerHTML = this.pData.username +" attacked the " + this.mData[this.monsterNum].name + " for  " + (this.pData.attack-this.mData[this.monsterNum].defense) + " hp!";
            this.renderer.appendChild(this.div.nativeElement, combatInfo);
            this.mData[this.monsterNum].hp -= (this.pData.attack-this.mData[this.monsterNum].defense);
            this.mCurrentHp=this.mData[this.monsterNum].hp;
            this.pauseOneSecond();
          }
        }
      }
    }

    monsterAttack(){
      const combatInfo: HTMLParagraphElement = this.renderer.createElement('p');
      let d = Math.floor((Math.random() * 100) + 1);

      if (d < this.mData[this.monsterNum].critChance){
        if(((this.mData[this.monsterNum].attack*1.5)-this.pData.defense) >= this.pData.hp){
          combatInfo.innerHTML = "**CRITICAL HIT**" + this.mData[this.monsterNum].name +" attacks! " + this.mData[this.monsterNum].name + " hits you for " + ((Math.ceil(this.mData[this.monsterNum].attack*1.5))-this.pData.defense) + " hp!";
          this.renderer.appendChild(this.div.nativeElement, combatInfo);
          this.renderer.addClass(combatInfo, 'monsterCrit');
          this.pData.hp=0;
          this.pauseOneSecond();
        }
        else{
           if(((this.mData[this.monsterNum].attack*1.5)-this.pData.defense) <=0){
              combatInfo.innerHTML = "**CRITICAL HIT**" + this.mData[this.monsterNum].name +" attacks! " + this.mData[this.monsterNum].name + " hits you for 0 hp!";
              this.renderer.appendChild(this.div.nativeElement, combatInfo);
              this.renderer.addClass(combatInfo, 'monsterCrit');
              this.pauseOneSecond();
           }
           else{
              combatInfo.innerHTML = "**CRITICAL HIT**" + this.mData[this.monsterNum].name +" attacks! " + this.mData[this.monsterNum].name + " hits you for " + ((Math.ceil(this.mData[this.monsterNum].attack*1.5))-this.pData.defense) + " hp!";
              this.renderer.appendChild(this.div.nativeElement, combatInfo);
              this.renderer.addClass(combatInfo, 'monsterCrit');
              this.pData.hp = this.pData.hp - ((Math.ceil(this.mData[this.monsterNum].attack*1.5))-this.pData.defense);
              this.pauseOneSecond();
          }
        }
      }
      else{
        if(this.mData[this.monsterNum].attack-this.pData.defense >= this.pData.hp){
          combatInfo.innerHTML = this.mData[this.monsterNum].name +" attacks! " + this.mData[this.monsterNum].name + " hits you for " + (this.mData[this.monsterNum].attack-this.pData.defense) + " hp!";
          this.renderer.appendChild(this.div.nativeElement, combatInfo);
          this.pData.hp=0;
          this.pauseOneSecond();
          }
        else{
          if(this.mData[this.monsterNum].attack-this.pData.defense < 0){
            combatInfo.innerHTML = this.mData[this.monsterNum].name +" attacks! " + this.mData[this.monsterNum].name + " hits you for 0 hp!";
            this.renderer.appendChild(this.div.nativeElement, combatInfo);
          }
          else{
            combatInfo.innerHTML = this.mData[this.monsterNum].name +" attacks! " + this.mData[this.monsterNum].name + " hits you for " + (this.mData[this.monsterNum].attack-this.pData.defense) + " hp!";
            this.renderer.appendChild(this.div.nativeElement, combatInfo);
            this.pData.hp = this.pData.hp - (this.mData[this.monsterNum].attack-this.pData.defense);
          }
           this.pauseOneSecond();
        }
      }
    }
    pauseOneSecond(){
    this.scrollDown = this.div.nativeElement.scrollHeight;
      setTimeout(() => {
        this.turnEnd();
      },1000);
    }
    getLoot(){
      this.pData.gold += this.mData[this.monsterNum].goldDrop;
      this.pData.xp_gained = this.mData[this.monsterNum].xpDrop + this.pData.xp_gained;
      if(this.pData.xp_current >= this.pData.xp_lvl_up){
         this.pData.level +=1;
         this.pData.xp_lvl_up = (this.pData.xp_lvl_up * 2)/1.5;
         this.pData.xp_current = 0;
      }
      this.pData.xp_current = this.mData[this.monsterNum].xpDrop + this.pData.xp_current;
      console.log(this.pData.gold);
      console.log(this.pData.xp_gained);
    }

    turnEnd(){
    if(this.count == 1 || this.count == 0 || this.count == 2){
      if(this.exit == true && this.mData[this.monsterNum].hp == 0){
        this.updateLoot();
        this.changeText();
      }
      else if(this.mData[this.monsterNum].hp == 0){
        let d = Math.random() * 100;
        const combatInfo: HTMLParagraphElement = this.renderer.createElement('p');

        if(d < 25){
          combatInfo.innerHTML = "A forest loot bag drops."
          this.renderer.appendChild(this.div.nativeElement, combatInfo);
          this.renderer.addClass(combatInfo, 'townDialog');
          this.basic++;
        }
        if(d < 5 && this.mData[this.monsterNum].lootType === "Boss"){
          combatInfo.innerHTML = "A Rare forest loot bag drops!"
          this.renderer.appendChild(this.div.nativeElement, combatInfo);
          this.renderer.addClass(combatInfo, 'townDialogRare');
          this.boss++;
        }
        setTimeout(() => {
              this.goldCollected = this.goldCollected + this.mData[this.monsterNum].goldDrop;
              this.xpCollected = this.xpCollected + this.mData[this.monsterNum].xpDrop;
              this.count = 1;
              this.monsterNum++;
              this.mMaxHp = this.mData[this.monsterNum].hp;
              this.mCurrentHp=this.mData[this.monsterNum].hp;
              this.mCurrentName = this.mData[this.monsterNum].name;
              this.startBattle();
        },1000);
      }
      else if(this.pData.hp <= 0){
        this.updateLoot();
      }
      else if(this.monsterNum == 99){
        this.count = 1;
        this.monsterNum = 0;
        this.forestBattle();
        this.startBattle();


      }
      else if(this.count == 0 || this.count == 2){
        if((this.pData.hp/this.pMaxHp)*100 <= this.value){
          this.count = 2;
          this.startBattle();
        }
        else{
          this.count = 1;
          this.mCurrentHp= this.mData[this.monsterNum].hp;
          this.startBattle();
        }
      }
      else{
        this.count = 0;
        this.startBattle();
      }
      }
    }


  }

export interface Player {
   username:string;
   hp:number;
   attack:number;
   defense:number;
   crit_chance:number;
   gold:number;
   level:number;
   xp_gained:number;
   xp_current:number;
   xp_lvl_up:number;
}
export interface Monster {
   name: string;
   hp:number;
   attack:number;
   defense:number;
   critChance:number;
   goldDrop:number;
   xpDrop:number;
   lootType:string;
}
