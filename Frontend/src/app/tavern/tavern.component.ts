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
  craftSucceed = false;
  craftFailed = false;
  showEnchantButton = false;
  hideLetters = false;

  shopInventory = [];
  inventory = [];
  user=[];
  wheelTimer;
  endTimer = 0;
  resetTimer = true;
  countdown;

  modAttack=0;
  modDefense=0;
  modHp=0;
  modCrit=0;

  craftPotions = [];
  craftEquipment=[];
  craftMisc=[];
  craftedItem=[];
  enchantedItem = [];
  tempEquipment=[];

  showShopPreview=false;
  showEnchantPreview=false;
  showCraftPreview=false;
  showLuckyPreview=false;
  showForge=false;

  showCraftEquipment=false;
  showCraftPotions=false;
  showCraftMisc = false;
  craftPotionsClicked = false;
  craftEquipmentClicked = false;
  craftMiscClicked = false;
  craftedItemSuccess = false;
  enchantItemSuccess= false;

  spinVal = "";
  interval;

  @ViewChild('div') div: ElementRef;



  constructor(private http:HttpClient,private router:Router,private renderer: Renderer2,private dataService:DataService) { }
  ngOnInit() {
    this.getUser();
  }
  ngOnDestroy(){
    clearInterval(this.interval);
  }
  wheelInformation(){
  return "Press the yellow button to spin! \n\n 500 Gold: 35% \n 5,000 Gold: 10% \n 20,000 Gold: 5% \n Potion: 17% \n Level up Scroll: 8% \n Enchant Gems: 10% \n Rare Equipment: 5%\n Mystery: 10%";
  }

  controlCraft(craftType){
    if(craftType === 'equipment'){
      this.showCraftEquipment = true;
      this.showCraftPotions = false;
      this.showCraftMisc = false;
      this.craftPotionsClicked = false;
      this.craftEquipmentClicked = true;
      this.craftMiscClicked =false;
    }
    else if(craftType === 'potion'){
      this.showCraftEquipment = false;
      this.showCraftPotions = true;
      this.showCraftMisc = false;
      this.craftPotionsClicked = true;
      this.craftEquipmentClicked = false;
      this.craftMiscClicked =false;
    }
    else if(craftType ==='misc'){
      this.showCraftEquipment = false;
      this.showCraftPotions = false;
      this.showCraftMisc = true;
      this.craftPotionsClicked = false;
      this.craftEquipmentClicked = false;
      this.craftMiscClicked =true;
    }
  }

  getUser(){
      let url = '/api/home';
      this.http.get<any>(url).subscribe(
            (rest:any)=>{
              this.dataService.gold=rest.gold;
              this.user = rest;
              this.endTimer = new Date(rest.end_wheel).getTime();
              this.wheelTimer = new Date().getTime();
              console.log(this.endTimer);
              console.log(this.wheelTimer);
              console.log((this.endTimer - this.wheelTimer)/1000);
              console.log(this.countdown);
              if(this.endTimer != 0 && this.endTimer > this.wheelTimer){
                this.resetTimer = false;
                this.countdown = new Date(((this.endTimer - this.wheelTimer)/1000) * 1000).toISOString().substr(11, 8);
              }
            },
            err=>{
            }
      );
      if(this.interval == null){
        this.interval = setInterval(() => {
        this.wheelTimer = this.wheelTimer + 1000;
        if(this.wheelTimer >= this.endTimer && this.resetTimer == false){
          this.resetTimer = true;
          this.endTimer = 0;
          }
          this.countdown = new Date(((this.endTimer - this.wheelTimer)/1000) * 1000).toISOString().substr(11, 8);
        },1000)
      }
  }


  showTempEquipment(item){
    this.tempEquipment=item;
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

  displayEnchantable(){
  this.hideLetters = true;
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
  enchantItem(item){
    this.enchantedItem = item;
    let url = "/api/tavern/enchant";
      this.http.post(url,{'item':item,'enchant':1}).subscribe(
        (rest:any)=>{
          console.log(rest);
          this.modAttack = rest.attack;
          this.modDefense = rest.defense;
          this.modHp = rest.hp;
          this.modCrit = rest.critChance;
          this.displayEnchantable();
          this.enchantItemSuccess = true;
        },
        err=>{
        console.log("failed to enchant item");
        }

      );
  }
  userCraftItem(item){
    let url = "/api/tavern/userCraftItem";
    console.log(item);
    this.http.post(url,item).subscribe(
      (rest:any)=>{
        if(rest == -1){
          const shopInfo: HTMLParagraphElement = this.renderer.createElement('p');
          shopInfo.innerHTML = "Not Enough Materials!";
          this.renderer.appendChild(this.div.nativeElement, shopInfo);
          this.renderer.removeClass(shopInfo,'attemptPurchase');
          this.renderer.addClass(shopInfo, 'purchaseItemFailedShow');
          setTimeout(() => {
            this.renderer.removeChild(this.div.nativeElement,shopInfo);
          }, 1400);
        }
        else{
          this.dataService.gold = rest;
          const shopInfo: HTMLParagraphElement = this.renderer.createElement('p');
          shopInfo.innerHTML = "Item Crafted!";
          this.renderer.appendChild(this.div.nativeElement, shopInfo);
          this.renderer.removeClass(shopInfo,'attemptPurchase');
          this.renderer.addClass(shopInfo, 'purchaseItemShow');
          setTimeout(() => {
            this.renderer.removeChild(this.div.nativeElement,shopInfo);
          }, 1400);
        }
      },
      (err)=>{
        console.log("something wrong with crafting item");
      }

    );

  }
  userCraftEquipment(item){
    let url = "/api/tavern/userCraftEquipment";
    console.log(item);
    this.http.post(url,item).subscribe(
      (rest:any)=>{
        if(rest == null){
          const shopInfo: HTMLParagraphElement = this.renderer.createElement('p');
          shopInfo.innerHTML = "Not Enough Materials or Gold!";
          this.renderer.appendChild(this.div.nativeElement, shopInfo);
          this.renderer.removeClass(shopInfo,'attemptPurchase');
          this.renderer.addClass(shopInfo, 'purchaseItemFailedShow');
          setTimeout(() => {
            this.renderer.removeChild(this.div.nativeElement,shopInfo);
          }, 1400);
        }
        else{
          this.craftedItem=rest;
          console.log(this.craftedItem);
          this.craftedItemSuccess= true;
          this.dataService.gold = this.dataService.gold-item.gold;
          const shopInfo: HTMLParagraphElement = this.renderer.createElement('p');
          shopInfo.innerHTML = "Item Crafted!";
          this.renderer.appendChild(this.div.nativeElement, shopInfo);
          this.renderer.removeClass(shopInfo,'attemptPurchase');
          this.renderer.addClass(shopInfo, 'purchaseItemShow');
          setTimeout(() => {
            this.renderer.removeChild(this.div.nativeElement,shopInfo);
          }, 1400);
        }
      },
      (err)=>{
        console.log("something wrong with crafting item");
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
    if(this.spinVal === "500" || this.spinVal ==="5000" || this.spinVal ==="20000")
      this.dataService.gold = this.dataService.gold + parseInt(this.spinVal);

      console.log("prize data sent");
      const shopInfo: HTMLParagraphElement = this.renderer.createElement('p');
      shopInfo.innerHTML = "Congrats you won!" + this.spinVal;
      this.renderer.appendChild(this.div.nativeElement, shopInfo);
      this.renderer.removeClass(shopInfo,'attemptPurchase');
      this.renderer.addClass(shopInfo, 'purchaseItemShow');
      setTimeout(() => {
       this.renderer.removeChild(this.div.nativeElement,shopInfo);
      }, 1400);
      this.getUser();
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
    this.showCraft = false;
    this.showCraftPreview=false;
    this.showEnchant = false;
    this.showEnchantPreview = false;
  }

  displayLucky(){
    this.showLucky = true;
    this.showShopPreview = false;
    this.showShopBuy = false;
    this.showShopSell = false;
    this.showCraft = false;
    this.showCraftPreview=false;
    this.showEnchant = false;
    this.showEnchantPreview = false;
  }

  displayEnchant(){
    this.showEnchant = true;
    this.showShopPreview = false;
    this.showShopBuy = false;
    this.showShopSell = false;
    this.showLucky = false;
    this.showLuckyPreview= false;
    this.showCraft = false;
    this.showCraftPreview=false;
  }

  displayCraft(){
    this.showCraft = true;
    this.showShopPreview = false;
    this.showShopBuy = false;
    this.showShopSell = false;
    this.showLucky = false;
    this.showLuckyPreview = false;
    this.showEnchant = false;
    this.showEnchantPreview = false;
    if(this.craftPotions.length == 0){
      let url = "/api/tavern/getCraftables";
      this.http.get<any>(url).subscribe(
        (rest:any)=>{
          this.sortCraftables(rest);
          console.log(this.craftPotions);
          console.log(this.craftEquipment);
          console.log(this.craftMisc);
        },
        err=>{
          console.log("error");

        }
      );
    }
  }
  sortCraftables(rest){
        this.craftEquipment = rest[0];
        for(let i =0;i<rest[1].length;i++){
          if(rest[1][i].item_type === 'Potion'){
            this.craftPotions.push(rest[1][i]);
          }
          else{
            this.craftMisc.push(rest[1][i]);
          }
        }

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
