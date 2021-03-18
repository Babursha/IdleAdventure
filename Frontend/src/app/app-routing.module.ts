import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { NavigationComponent } from './navigation/navigation.component';
import { InventoryComponent } from './inventory/inventory.component';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import { ProfileComponent } from './profile/profile.component';
import { FormsModule} from "@angular/forms";
import { SignupComponent } from './signup/signup.component';
import { AuthInterceptor } from './auth/auth-interceptor';
import { DungeonsComponent } from './dungeons/dungeons.component';
import { DungeonBattleComponent } from './dungeonbattle/dungeon-battle.component';
import { TavernComponent } from './tavern/tavern.component';
import { ErrorComponent } from './error/error.component';
import { FooterComponent } from './footer/footer.component';

const routes: Routes = [

 {
    path: '',
    component: LoginComponent,
    pathMatch: 'full'
  },
  {
      path: 'home',
      component: HomeComponent
  },
  {
      path: 'login',
      component: LoginComponent
  },
  {
      path: 'profile',
      component: ProfileComponent
  },
  {
      path: 'inventory',
      component: InventoryComponent
  },
  {
        path: 'signup',
        component: SignupComponent
  },
  {
        path: 'dungeons',
        component:DungeonsComponent
  },
  {
        path: 'dungeons',
        component: DungeonBattleComponent,
        children: [
                    { path: 'forest', component: DungeonBattleComponent,data:{area:'forest'} },
                    { path: 'desert', component: DungeonBattleComponent,data:{area:'desert'} },
                    { path: 'cave', component: DungeonBattleComponent,data:{area:'cave'} },
                    { path: 'magma',component: DungeonBattleComponent,data:{area:'magma'} },
                ]

  },
  {
        path:'tavern',
        component: TavernComponent
  },
  {
        path:'error',
        component:ErrorComponent
  }


];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
