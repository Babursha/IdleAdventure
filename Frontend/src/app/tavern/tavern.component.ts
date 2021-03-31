import { Component, OnInit,Renderer2, ViewChild, ElementRef} from '@angular/core';
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
  showLucky = false;
  showEnchant=false;

  showShopBuy = false;
  showShopSell = false;

  shopInventory = [];
  inventory = [];
  user=[];

  showShopPreview=false;
  showEnchantPreview=false;
  showCraftPreview=false;
  showLuckyPreview=false;
  showForge=false;

  spinVal = "";

  @ViewChild('div') div: ElementRef;



  constructor(private http:HttpClient,private router:Router,private renderer: Renderer2,private dataService:DataService) { }
  ngOnInit() {
    this.getUser();
  }
  wheelInformation(){
  return "Press the yellow button to spin! \n\n 500 Gold: 35% \n 5,000 Gold: 10% \n 20,000 Gold: 5% \n Potion: 17% \n Level up Scroll: 8% \n Enchant Gems: 10% \n Rare Equipment: 5%\n Mystery: 10%";
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

  luckyWheelSpin(){

    let d = (Math.random() * 100);

    if(d <= 5){
    console.log("20k");
    this.spinVal = "20000";
    }
    else if(d >= 6 && d <= 15){
    console.log("mystery");
    this.spinVal="mystery";
    }
    else if(d >= 16 && d <= 50){
    console.log("500");
    this.spinVal = "500";
    }
    else if(d >=51 && d <=55){
    console.log("rare equipment");
    this.spinVal = "rare";
    }
    else if(d>=56 && d <= 65){
    console.log("5k");
    this.spinVal = "5000";
    }
    else if(d>= 66 && d<= 73){
    console.log("lvl scroll");
    this.spinVal = "lvl";
    }
    else if(d >= 74 && d <=90){
    console.log("potion");
    this.spinVal = "potion";
    }
    else{
    console.log("enchant");
    this.spinVal = "enchant";
    }
    this.userGetPrize();
  }

  userGetPrize(){
  let url = "/api/tavern/lucky";
  this.http.post(url,this.spinVal).subscribe(
    (rest:any)=>{
      console.log("prize data sent");

    },
    err=>{

      console.log("couldn't get prize info");
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
    this.showLucky = false;
    this.showLuckyPreview = false;
  }
  displayLucky(){
    this.showLucky = true;
    this.showShopPreview = false;
    this.showShopBuy = false;
    this.showShopSell = false;
  }
  displayEnchant(){
    this.showLucky = true;
    this.showShopPreview = false;
    this.showShopBuy = false;
    this.showShopSell = false;
  }
  displayCraft(){
    this.showLucky = true;
    this.showShopPreview = false;
    this.showShopBuy = false;
    this.showShopSell = false;
  }
  displayShopPreview(){
    if(this.showShop !=true){
      this.showShopPreview = true;
      setTimeout(() => {
        this.showShopPreview = false;
      }, 100);
    }

  }
  displayCraftPreview(){
    if(this.showCraft !=true){
      this.showCraftPreview = true;
      setTimeout(() => {
        this.showCraftPreview = false;
      }, 100);
    }

  }
  displayEnchantPreview(){
    if(this.showEnchant !=true){
      this.showEnchantPreview = true;
      setTimeout(() => {
        this.showEnchantPreview = false;
      }, 100);
    }

  }
  displayLuckyPreview(){
    if(this.showLucky !=true){
      this.showLuckyPreview = true;
      setTimeout(() => {
        this.showLuckyPreview = false;
      }, 100);
    }

  }

}
