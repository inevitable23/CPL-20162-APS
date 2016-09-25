import {Injectable} from '@angular/core';
import {Geolocation} from 'ionic-native';
import {Observable} from 'rxjs/Observable';

declare var Microsoft;

@Injectable()
export class Bingmap {

  mapElement: any;
  pleaseConnect: any;
  map: any;
  mapInitialised: boolean = false;
  mapLoaded: any;
  mapLoadedObserver: any;
  currentMarker: any;
  apiKey: string;
  panelElement: any;

  constructor() {}

  init(mapElement: any): any {
    this.mapElement = mapElement;

    this.mapLoaded = Observable.create(observer => {
      this.mapLoadedObserver = observer;
    });

    this.loadBingmap();

    return this.mapLoaded;
  }

  loadBingmap(): void {
    this.initMap();
  }

  initMap(): void {
    this.mapInitialised = true;
    console.log('Microsoft',Microsoft);
    this.map = new Microsoft.Maps.Map(this.mapElement,
    { credentials: "LaipUfshsMLolLAZr0sq~FDO1x20H4dyJ5APEQeI60w~Amnhlp-hS9x0bwRndSsIHyqE8Nfem8tHvnFl49T9FPSDpuECWnLynsspgCwAjNfm" });
    // Bingmap API 불러와서 map 변수에 저장
    
    console.log("map",this.map);
    //var geoLocationProvider = new Microsoft.Maps.GeoLocationProvider(this.map);

  //  geoLocationProvider.getCurrentPosition({successCallback:displayCenter});
    //현재 사용자의 위치를 지도상에 표시
    Microsoft.Maps.loadModule('Microsoft.Maps.Directions', () =>
    {
      var directionsManager = new Microsoft.Maps.Directions.DirectionsManager(this.map);
      // Set Route Mode to walking
    directionsManager.setRequestOptions({ routeMode: Microsoft.Maps.Directions.RouteMode.transit });
      var waypoint1 = new Microsoft.Maps.Directions.Waypoint({ address: '내 위치', location: new Microsoft.Maps.Location(37.497942,127.0270738 ) });
      var waypoint2 = new Microsoft.Maps.Directions.Waypoint({ address: '환자 위치', location: new Microsoft.Maps.Location(37.4977249,127.0257756) });
      directionsManager.addWaypoint(waypoint1);
      directionsManager.addWaypoint(waypoint2);
      //Set the element in which the itinerary will be rendered
      //directionsManager.setRenderOptions({ itineraryContainer: document.getElementById('printoutPanel') });
      directionsManager.calculateDirections();
    });
  }

}
