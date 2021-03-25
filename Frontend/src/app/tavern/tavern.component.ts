import { Component, OnInit,Renderer2, ViewChild, ElementRef } from '@angular/core';
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

  shopInventory = [];
  showShopPreview=false;
  inventory = [];
  user=[];

  @ViewChild('div') div: ElementRef;

  constructor(private http:HttpClient,private router:Router,private renderer: Renderer2,private dataService:DataService) { }
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
          const shopInfo: HTMLParagraphElement = this.renderer.createElement('p');
          shopInfo.innerHTML = "Not enough Gold!";
          this.renderer.appendChild(this.div.nativeElement, shopInfo);
          this.renderer.removeClass(shopInfo,'attemptPurchase');
          this.renderer.addClass(shopInfo, 'purchaseItemFailedShow');
          setTimeout(() => {
            this.renderer.removeChild(this.div.nativeElement,shopInfo);
          }, 1400);
        }
        else{
          this.dataService.gold = rest;
          this.shopInventory.splice(index, 1);
          this.shopInventory = this.shopInventory.slice(0);
          const shopInfo: HTMLParagraphElement = this.renderer.createElement('p');
          shopInfo.innerHTML = "Item bought!";
          this.renderer.appendChild(this.div.nativeElement, shopInfo);
          this.renderer.removeClass(shopInfo,'attemptPurchase');
          this.renderer.addClass(shopInfo, 'purchaseItemShow');
          setTimeout(() => {
            this.renderer.removeChild(this.div.nativeElement,shopInfo);
          }, 1400);
        }
      },
      err=>{
      }
    );

  }

  shopItemSell(item){
    let url = "/api/tavern/shop/sell";
    this.http.post(url,item).subscribe(
      (rest:any)=>{
        this.dataService.gold=rest;
        const shopInfo: HTMLParagraphElement = this.renderer.createElement('p');
        shopInfo.innerHTML = "Item Sold!";
        this.renderer.appendChild(this.div.nativeElement, shopInfo);
        this.renderer.removeClass(shopInfo,'attemptPurchase');
        this.renderer.addClass(shopInfo, 'purchaseItemShow');
        setTimeout(() => {
          this.renderer.removeChild(this.div.nativeElement,shopInfo);
        }, 1400);
        this.displayShopSell();

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
