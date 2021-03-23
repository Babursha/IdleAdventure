import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { DataService } from '../data.service';

@Component({
  selector: 'app-inventory',
  templateUrl: './inventory.component.html',
  styleUrls: ['./inventory.component.css']
})
export class InventoryComponent implements OnInit {
  model: User = {
    username:'',
    level:0,
    gold:0,
    hp:0,
    defense:0,
    attack:0,
    crit_chance:0
  };
  weaponEquipped = false;
  helmetEquipped = false;
  chestEquipped = false;
  ringEquipped = false;
  bootsEquipped = false;
  leggingEquipped = false;
  itemLevel:number=0;

  public inventory = [];

  tempEquipment=[];

  helmet = [];
  chest = [];
  weapon = [];
  legging = [];
  boots = [];
  ring = [];


  constructor(private http: HttpClient,private router:Router, private dataService:DataService ) { }

  ngOnInit() {
      this.getUser();
      this.showInventory();
      this.getEquipped();
  }
  ngOnDestroy(){

  }

  showTempEquipment(item){
    this.tempEquipment=item;
  }

    getUser(){
      let url = '/api/home';
      this.http.get<any>(url).subscribe(
            (rest:any)=>{
              this.dataService.gold=rest.gold;
              this.model = rest;
              console.log(this.model);
            },
            err=>{
            }
      );
    }

  showInventory(){
      let url = "/api/show_inventory";
       this.http.get<any>(url).subscribe(
          (rest : any)=>{
            console.log(rest);
              this.inventory = rest;
          },
          err=>{
            console.log("failed to get inventory");
          }
       );
  }



  getEquipped(){
      let url = "/api/inventory/get_equipped";
      this.http.get<any>(url).subscribe(
        (rest : any)=>{
          for(let item of rest){
            this.itemLevel = this.itemLevel+item.level;
            if(item.equip_type==="Weapon"){
              this.weapon=item;
              this.weaponEquipped=true;
            }
            if(item.equip_type==="Helmet"){
              this.helmet=item;
              this.helmetEquipped=true;
            }
            if(item.equip_type==="Chest"){
              this.chest=item;
              this.chestEquipped=true;
            }
            if(item.equip_type==="Legging"){
              this.legging=item;
              this.leggingEquipped=true;
            }
            if(item.equip_type==="Boots"){
              this.boots=item;
              this.bootsEquipped=true;
            }
            if(item.equip_type==="Ring"){
              this.ring=item;
              this.ringEquipped=true;
            }
          }
        },
        err=>{
          console.log("failed to get equipped items.");
        }

      );

  }
  unequipItem(item){
  console.log('unequipping');
    this.itemLevel = this.itemLevel - item.level;
    if(item.equip_type==="Weapon"){
      this.weapon=[];
      this.weaponEquipped=false;
    }
    else if(item.equip_type==="Chest"){
      this.chest=[];
      this.chestEquipped=false;

    }
    else if(item.equip_type==="Helmet"){
      this.helmet=[];
      this.helmetEquipped=false;

    }
    else if(item.equip_type==="Legging"){
      this.legging=[];
      this.leggingEquipped=false;

    }
    else if(item.equip_type==="Boots"){
      this.boots=[];
      this.bootsEquipped=false;

    }
    else if(item.equip_type==="Ring"){
      this.ring=[];
      this.ringEquipped=false;
    }
    let url = "/api/inventory/unequipItem";
    this.http.post(url,item).subscribe(
    (rest:any)=>{
      this.showInventory();
      this.getUser();
    },
    err=>{
      console.log("failed to unequip item.");

    }

    );

  }

  equipItem(item){
    this.itemLevel =this.itemLevel+item.level;
    this.equipCheck(item);
    if(item.equip_type==="Weapon"){
      this.weapon=item;
      this.weaponEquipped=true;
    }
    else if(item.equip_type==="Chest"){
      this.chest=item;
      this.chestEquipped=true;

    }
    else if(item.equip_type==="Helmet"){
      this.helmet=item;
      this.helmetEquipped=true;

    }
    else if(item.equip_type==="Legging"){
      this.legging=item;
      this.leggingEquipped=true;

    }
    else if(item.equip_type==="Boots"){
      this.boots=item;
      this.bootsEquipped=true;

    }
    else if(item.equip_type==="Ring"){
      this.ring=item;
      this.ringEquipped=true;

    }
      console.log(item);
      let url = "/api/inventory/equipItem";
      this.http.post(url,item).subscribe(
        (rest : any) =>{
          this.showInventory();
          this.getUser();
        },
        err=>{
          console.log("Failed to equip item.");
        }

      );

  }

  equipCheck(item){
    if(this.weaponEquipped && item.equip_type==="Weapon")
      this.unequipItem(this.weapon);
    else if(this.helmetEquipped && item.equip_type==="Helmet")
      this.unequipItem(this.helmet);
    else if(this.chestEquipped && item.equip_type==="Chest")
      this.unequipItem(this.chest);
    else if(this.leggingEquipped && item.equip_type==="Legging")
      this.unequipItem(this.legging);
    else if(this.bootsEquipped && item.equip_type==="Boots")
      this.unequipItem(this.boots);
    else if(this.ringEquipped && item.equip_type==="Ring")
      this.unequipItem(this.ring);
  }

}
export interface User{
username:string;
level:number;
gold:number;
hp:number;
defense:number;
attack:number;
crit_chance:number;
}
