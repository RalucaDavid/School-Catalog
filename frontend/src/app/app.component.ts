import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  template: `<div class = "h-screen w-screen bg-[#252732]">
      <section id = "header">
          <app-navbar></app-navbar>
      </section>
      <section id = "content">
          <router-outlet></router-outlet>
      </section>
    </div>
  `
})
export class AppComponent { }
