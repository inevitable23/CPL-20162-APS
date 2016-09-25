import { Injectable, NgZone } from '@angular/core';
import { Http } from '@angular/http';
import 'rxjs/add/operator/map';

/*
  Generated class for the Socket provider.

  See https://angular.io/docs/ts/latest/guide/dependency-injection.html
  for more info on providers and Angular 2 DI.
*/
declare var io;
@Injectable()
export class Socketio {

  socket : any;
  //serverUrl = "http://ec2-52-78-1-158.ap-northeast-2.compute.amazonaws.com:8080";
  serverUrl ="http://localhost:8080";

  constructor() {
  }
  connectSocket(){
    console.log("url",this.serverUrl);
    this.socket = io(this.serverUrl);
  }
  diconnectSocket(){
    this.socket.disconnect();
  }
  getSocket(){
    return this.socket;
  }
}
