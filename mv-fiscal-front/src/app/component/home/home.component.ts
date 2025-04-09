import { Component } from '@angular/core';
import { CardModule } from 'primeng/card';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'home',
  imports: [CardModule, RouterModule],
  standalone: true,
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
})
export class HomeComponent {}
