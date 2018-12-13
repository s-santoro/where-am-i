const mapillaryId = 'UlhwcmNzcEs2TFpQaEQ1TEFRaWtaZzo1MDk0NjU0OGY5YjNjYzA2';
let clicked = false;

function game(context) {
//Structure
  let controlPanel = '<div class="container" id="controlPanel"></div>';
  let mapillaryContainer = '<div class="container" id="mapillaryContainer"></div>';
  $('#app').append(controlPanel, mapillaryContainer);

  //CONTROLPANEL
//Create Control Buttons with functionality and add them to index.html
  let evalButton =  '<button id="evalButton" type="button" class="btn btn-outline-dark mb-2 mt-2 mr-2">Evaluate</button>';
  let resetButton = '<button id="resetButton" type="button" class="btn btn-outline-dark mb-2 mt-2 mr-2">Restart</button>';
  $('#controlPanel').append(resetButton);
  $('#controlPanel').append(evalButton);

  $('#resetButton').click(function() {
    location.reload();
  });
  $('#evalButton').click(function() {
    if(coordinates!=undefined){
      getDistance(imgKey, coordinates)
        .then((distance) => {
          clicked=true;
          showScore(distance);
          postSession(1, imgKey, distance.distance);
        });

    }else{
      alert("Please set your tip");
    }

  });
//MAPILLARY
  createMapillary();
  //LEAFLET
  createLeaflet();

}
