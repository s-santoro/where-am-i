let coordinates;
let mymap;

function createLeaflet() {
  let leafletContainer = '<div class="container" id="leafletContainer"></div>';
  $('#app').append(leafletContainer);
// Create Div for Leaflet and add it to index.html
  let leafletDiv = '<div id="mapid" style="width: 100%; height: 300px; position: relative;"></div>';
  $('#leafletContainer').append(leafletDiv);
//Basic Map Settings
  mymap = L.map('mapid').setView([51.505, -0.09],1);
  L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoiZ2lhbmJydW5uZXIiLCJhIjoiY2puazFqbXV3MGFmNTNrbWgyNG5zcDFyZSJ9.87l6WgQ_tzccQJif8HcnVA', {
    attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, <a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
    maxZoom: 15,
    id: 'mapbox.streets',
    accessToken: 'pk.eyJ1IjoiZ2lhbmJydW5uZXIiLCJhIjoiY2puazFqbXV3MGFmNTNrbWgyNG5zcDFyZSJ9.87l6WgQ_tzccQJif8HcnVA'
  }).addTo(mymap);

//Create Marker on Click
  let userPopup = L.popup();
  let userMarker;
  function onMapClick(e) {
    if(clicked===false){
      coordinates = e.latlng;
      if(userMarker!=undefined){
        mymap.removeLayer(userMarker);
      }
      userPopup
        .setLatLng(coordinates)
        .setContent("Your Tip");
      userMarker = L.marker(coordinates).addTo(mymap);
      userMarker.bindPopup(userPopup).openPopup();
    }
  }
  mymap.on('click', onMapClick);
}

function createImageMarker(input) {
  let imgPopup = L.popup();
  imgPopup.setContent("Actual Position");
  let imgMarker = L.marker([input.imagepos.latitude, input.imagepos.longitude]).addTo(mymap);
  imgMarker.bindPopup(imgPopup).openPopup();
}
