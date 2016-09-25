import { Component } from '@angular/core';
import { Page, NavController, Platform, Alert } from 'ionic-angular';
import { ElementRef, ViewChild } from '@angular/core';
import { Bingmap } from '../../providers/bingmap/bingmap';
import { Tmapapi } from '../../providers/tmap/tmap';
import { Data } from '../../providers/data/data';
import { Socketio } from '../../providers/socketio/socketio';


@Component({
  templateUrl: 'build/pages/home/home.html'
})
export class HomePage {
  @ViewChild('map') mapElement: ElementRef;

  latitude: number;
  longitude: number;
  socket : any;
  address : string;

  constructor(public nav: NavController, public maps: Bingmap, public platform: Platform, public socketService : Socketio, public dataService: Data) {
    this.address ="서울특별시 서초구 서초대로 405 가락빌딩 커피니 2층 화장실 앞";
  }
  ngAfterViewInit(){
    let mapLoaded = this.maps.init(this.mapElement.nativeElement);
    mapLoaded.subscribe(update => {
      //this.maps.changeMarker(this.latitude, this.longitude);
    });
    //this.connectSocket();
  }
  /*
  connectSocket(){
    this.socket = this.socketService.connectSocket();
    this.initmsg();
  }

  initmsg(){
    this.socket.on('rescue',(data)=>{

    });
  }*/
}
