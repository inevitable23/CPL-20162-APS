import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';


declare var Tmap;

@Injectable()
export class Tmapapi {
  mapElement : any;
  mapLoaded: any;
  mapLoadedObserver: any;
  map : any;


  constructor(private http: Http) {}

  init(mapElement: any){
    this.mapLoaded = Observable.create(observer => {
      this.mapLoadedObserver = observer;
    });

    this.loadTmap();

    return this.mapLoaded;
  }
  loadTmap(): void {
    this.initMap();
  }
  initMap(){
    //var map = new Tmap.Map({ div:"map_div", width:'100%', height:'50%'});
  //  map.setCenter(new Tmap.LonLat(14135911, 4518361),15);
    //var c_ll = map.getCenter();
    //this.loadGetAddressFromLonLat(c_ll);
    var centerLL = new Tmap.LonLat(14145677.4, 4511257.6);
    this.map = new Tmap.Map({div:'map_div',
                        width:'100%',
                        height:'50%',
                        transitionEffect:"resize",
                        animation:true
                    });
    this.searchRoute();
//tData.events.register("onComplete", tData, onComplete);
//tData.events.register("onProgress", tData, onProgress);
//tData.events.register("onError", tData, onError);
  }

  searchRoute(){
      var routeFormat = new Tmap.Format.KML({extractStyles:true, extractAttributes:true});
      var startX = 14129105.461214;
      var startY = 4517042.1926406;
      var endX = 14136027.789587;
      var endY = 4517572.4745242;
      var startName = "홍대입구";
      var endName = "명동";
      var urlStr = "https://apis.skplanetx.com/tmap/routes/pedestrian?version=1&format=xml";
          urlStr += "&startX="+startX;
          urlStr += "&startY="+startY;
          urlStr += "&endX="+endX;
          urlStr += "&endY="+endY;
          urlStr += "&startName="+encodeURIComponent(startName);
          urlStr += "&endName="+encodeURIComponent(endName);
          urlStr += "&appKey=bf052a40-d219-312e-96e5-e2823d7593a7";
      var prtcl = new Tmap.Protocol.HTTP({
                                          url: urlStr,
                                          format:routeFormat
                                          });
      var routeLayer = new Tmap.Layer.Vector("route", {protocol:prtcl, strategies:[new Tmap.Strategy.Fixed()]});
      routeLayer.events.register("featuresadded", routeLayer, function(e){
        this.map.zoomToExtent(this.getDataExtent());
      });
      //console.log('ext',this.map.getDataExtent());
      this.map.addLayer(routeLayer);
  }
  //경로 그리기 후 해당영역으로 줌

}
