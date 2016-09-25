import { Component } from '@angular/core';
import { ionicBootstrap, Platform } from 'ionic-angular';
import { StatusBar } from 'ionic-native';
import { Bingmap } from './providers/bingmap/bingmap';
import { Tmapapi } from './providers/tmap/tmap';
import { Socketio } from './providers/socketio/socketio';
import { Data } from './providers/data/data';
import { HomePage } from './pages/home/home';


@Component({
  template: '<ion-nav [root]="rootPage"></ion-nav>'
})
export class MyApp {
  rootPage: any = HomePage;

  constructor(public platform: Platform) {
    platform.ready().then(() => {
      // Okay, so the platform is ready and our plugins are available.
      // Here you can do any higher level native things you might need.
      //StatusBar.styleDefault();
    });
  }
}

ionicBootstrap(MyApp,[Data, Bingmap, Socketio, Tmapapi]);
