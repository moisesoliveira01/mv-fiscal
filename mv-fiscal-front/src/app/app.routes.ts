import { Routes } from '@angular/router';
import { HomeComponent } from './component/home/home.component';
import { UserComponent } from './component/user/user.component';
import { TaskComponent } from './component/task/task.component';

export const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent },
  { path: 'user', component: UserComponent },
  { path: 'task', component: TaskComponent },
];
