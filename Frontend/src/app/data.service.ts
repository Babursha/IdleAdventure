import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class DataService {
  gold = 0;
  gold_progress = 0;
  xp_progress = 0;
  xp_current = 0;
  xp_lvl_up = 0;
  xp_progress_max = 0;
  xpBarWidth =0;
  xp_speed = 0;
  counterStarted = false;

  counter:number = 0;
  counterXp:number=0;
  interval;

  constructor() { }


  startGoldTimer() {
      this.interval = setInterval(() => {
        if(this.counter <= 99.9) {
          this.counter+= .1;
          this.gold_progress++;
        }
      },1000)
    }

  startXpTimer() {
      this.interval = setInterval(() => {
          if(this.xp_progress < this.xp_progress_max)
            this.xp_progress++;
          this.xpBarWidth = (this.xp_progress/this.xp_progress_max) * 100;
      },5500)
    }
  updateXpBar(){
      this.xpBarWidth = (this.xp_progress/this.xp_progress_max) * 100;
  }
}
