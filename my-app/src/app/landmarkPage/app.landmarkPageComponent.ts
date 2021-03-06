import { Component, OnInit, ViewChild, ElementRef, NgZone, Input, Output, EventEmitter } from '@angular/core';
import { MapsAPILoader, MouseEvent } from '@agm/core';
import { Location } from 'src/app/domens/location';
import {DataService} from 'src/app/services/data.service';
import { Way } from 'src/app/domens/way';
import { PlaceDetails } from '../domens/placeDetails';
import { PlacesService } from '../services/places.service';
import { THIS_EXPR } from '@angular/compiler/src/output/output_ast';
import { MessageBox, MessageBoxService } from 'message-box-plugin';
import { TokenStorageService } from '../_services/token-storage.service';

@Component({
  templateUrl: './landmarkPage.html',
  styleUrls: ['./app.landmarkPageComponent.css']

})
export class LandmarkPageComponent implements OnInit{
  rating: number;
  periods: [];
  address: string = '';
  photo: Array<string> = new Array<string>();
  name: string = '';
  types: Array<string>;
  number: string = '';
  open_hours: Array<Array<string>>;
  open: Array<any>;
  close: Array<any>;
  loc: Location;
  wayArray: Array<Location> = new Array<Location>();
  way: Way;
  ind = 0;
  lat: number = 0;
  lng: number = 0;

  placeId: string;
  placeDetails: Array<PlaceDetails> = new Array<PlaceDetails>();

  @ViewChild('map1', {static: false})
  mapElement: ElementRef;

  cityLocation: Array<Location> ;
  map: any;

  /*@ViewChild('searchlm', {static: true})
  public searchElementRef: ElementRef;*/

  constructor(
    private mapsAPILoader: MapsAPILoader,
    private ngZone: NgZone,
    private data: DataService,
    private placeService: PlacesService,
    private messageBoxService: MessageBoxService,
    private tokenStorageService: TokenStorageService,
  ) { }

  /*checkLocInArray(){
    this.data.currentWay.subscribe(w => this.way = w);
    this.way.points.forEach(l => {
      if (l.lat == this.loc.lat && l.lng == this.loc.lng)
      {
        this.ind = 1;
      }
    });
    /*this.data.currentWayLocations.subscribe(loc => this.wayArray = loc);
    this.wayArray.forEach(l => {
      if (l.lat == this.loc.lat && l.lng == this.loc.lng)
      {
        this.ind = 1;
      }
    });
  }*/

  /*addToWay(){
    this.checkLocInArray();
    this.way.name = 'Тестовый путь 2';
    this.data.currentCityName.subscribe(w => this.way.cityAddress = w);
    this.way.index = 1;
    if(this.ind == 0){
      this.data.changeWay(this.loc, this.way, 1);
    }
    console.log(this.way);
  }*/

  ngOnInit() {
    this.placeId = sessionStorage.getItem('landmark');
    console.log(this.placeId);
    this.loadPlaces();
  }

  addToWay(i: number){
    var locs = [];
    if(JSON.parse(sessionStorage.getItem("LocatsToWay")) != null){
      locs = JSON.parse(sessionStorage.getItem("LocatsToWay"));
    }
    locs.push({lat: this.lat, lng: this.lng, placeId: this.placeId});
    sessionStorage.setItem("LocatsToWay", JSON.stringify(locs));
    //this.locations[i].addedToWay = true;
    //sessionStorage.removeItem('locatsToShowOnMap');
    //sessionStorage.setItem('locatsToShowOnMap', JSON.stringify(this.locations));
    console.log("Way" + sessionStorage.getItem("LocatsToWay"));
  }

  loadPlaces(){
    this.mapsAPILoader.load().then(() => {
      let city = {lat: 147.4984232, lng:  19.0705289};
      let mapOptions = {
        center: city,
        zoom: 15
      }

      this.map = new google.maps.Map(this.mapElement.nativeElement, mapOptions);
      let service = new google.maps.places.PlacesService(this.map);

      var request = {
        placeId: this.placeId,
        fields: ['name', 'formatted_address', 'place_id', 'geometry', 'rating', 'international_phone_number', 'photos', 'types']
      };

      service.getDetails(request, (place, status) => {
          console.log(place.geometry.location.lat())
          this.getPlaces(place, status)
      });

    }, (err) => {
      console.log(err);
    });

  }


  addToFavP(){
    if(!this.tokenStorageService.getUser()){
      let messageBox = MessageBox
      .Create('Хэй, друг)' ,'Войди в систему, чтобы сохранить место :)')
      this.messageBoxService.present(messageBox);
      window.scroll(0,0);
      return 0;
    }
    let lat: number = this.lat;
    let lng: number = this.lng;

    var loc = {lat: lat, lng: lng, placeId: this.placeId};
    this.placeService.addPlace(loc).subscribe(data =>console.log(data));
  }

  getPlaces(results, status){
      console.log("place"+results.photos);
      this.name = results.name;
      this.rating = results.rating;
      this.types = results.types;
      this.number = results.international_phone_number;
      this.address = results.formatted_address;

      if(results.photos != null)
        {
          for (var i = 0; i < results.photos.length ; i++) {
              this.photo.push(results.photos[i].getUrl());
          }
        }
      else{
        this.photo.push("/assets/img/place.jpeg");
      }
      this.lat = results.geometry.location.lat();
      this.lng = results.geometry.location.lng();
      //this.placeDetails.push({name: results.name, address: results.formatted_address,photos:results.photos,
        //types: results.types});
      //console.log("details" + this.placeDetails[0].name);
    }
}
