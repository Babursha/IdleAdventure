import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { DataService } from '../data.service';

@Component({
  selector: 'app-tavern',
  templateUrl: './tavern.component.html',
  styleUrls: ['./tavern.component.css']
})
export class TavernComponent implements OnInit {

  showShop = false;
  showCraft = false;
  showDismantle = false;

  showShopBuy = false;
  showShopSell = false;

  showBought = false;
  showSell = false;

  shopInventory = [];
  notEnoughGold=false;
  showShopPreview=false;
  inventory = [];
  user=[];

  constructor(private http:HttpClient,private router:Router,private dataService:DataService) { }
  ngOnInit() {
    this.getUser();
  }

  getUser(){
      let url = '/api/home';
      this.http.get<any>(url).subscribe(
            (rest:any)=>{
              this.dataService.gold=rest.gold;
              this.user = rest;
            },
            err=>{
            }
      );
  }

  shopItemBuy(item,index){
    console.log(index);
    let url = '/api/tavern/shop/buyItem';
    this.http.post(url,item).subscribe(
      (rest:number)=>{
        if(rest == -1){
          this.notEnoughGold = true;
          setTimeout(() => {
            this.notEnoughGold = false;
          }, 1500);
        }
        else{
          this.dataService.gold = rest;
          this.shopInventory.splice(index, 1);
          this.shopInventory = this.shopInventory.slice(0);
          this.showBought = true;
          setTimeout(() => {
            this.showBought = false;
          }, 1500);
        }
      },
      err=>{
      }
    );

  }
  shopItemSell(name){
    this.showSell = true;
    setTimeout(() => {
      this.showSell = false;
    }, 1000);
    console.log(name);
    let data = {username:localStorage.getItem('username'),itemName:name};
    let url = "/api/tavern/shop/sell";
    this.http.post(url,data).subscribe(
      (rest:any)=>{
      },
      err=>{
      }
      );
  }

  displayShopSell(){
    this.showShopBuy = false;
    this.showShopSell = true;
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
  displayShopBuy(){
    if(this.showShopBuy !=true){
      this.showShopBuy = true;
      this.showShopSell = false;
      let url = "/api/tavern/shop/buy"
      this.http.get<any>(url).subscribe(
      (rest:any)=>{
        console.log(rest);
        this.shopInventory = rest;
      },
      err=>{
      }
      );
    }
  }


  displayShop(){
    this.showShop = true;
  }
  displayShopPreview(){
    if(this.showShop !=true){
      this.showShopPreview = true;
      setTimeout(() => {
        this.showShopPreview = false;
      }, 100);
    }

  }
}
