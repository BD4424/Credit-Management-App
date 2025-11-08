import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ThemeService {

  private darkMode: boolean = false;

  constructor() {
    //Try to load already existing theme from local storage
    const savedTheme = localStorage.getItem('theme');
    if (savedTheme == 'dark') {
      this.darkMode = true;
      document.body.classList.add('dark-theme');
    }
  }
  toggleTheme(): void {
    this.darkMode = !this.darkMode;
    if (this.darkMode) {
      document.body.classList.add('dark-theme');
      localStorage.setItem('theme', 'dark');
    } else {
      document.body.classList.remove('dark-theme');
      localStorage.setItem('theme', 'light');
    }
  }

  isDarkMode(): boolean {
    return this.darkMode;
  }
  
}