import { Component } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { filter } from 'rxjs';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'menu',
  imports: [CommonModule],
  standalone: true,
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.css',
})
export class MenuComponent {
  menuItems = [
    { label: 'Início', icon: 'pi pi-home', routerLink: '/home' },
    { label: 'Usuários', icon: 'pi pi-users', routerLink: '/user' },
    { label: 'Tarefas', icon: 'pi pi-check-square', routerLink: '/task' },
  ];

  activeMenuItem = '';

  constructor(private router: Router) {
    this.router.events
      .pipe(filter((event) => event instanceof NavigationEnd))
      .subscribe((event: NavigationEnd) => {
        if (event.url === '/') {
          this.activeMenuItem = '';
        } else {
          this.activeMenuItem = event.url;
        }
      });
  }

  onMenuItemClick(routerLink: string) {
    this.activeMenuItem = routerLink;
    this.router.navigate([routerLink]);
  }
}
