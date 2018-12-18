const mapillaryId = 'UlhwcmNzcEs2TFpQaEQ1TEFRaWtaZzo1MDk0NjU0OGY5YjNjYzA2';
let clicked = false;
let userID;

function game(context) {
//Structure
  let controlPanel = '<div class="container text-center" id="controlPanel"></div>';
  let mapillaryContainer = '<div class="container" id="mapillaryContainer"></div>';
  $('#app').append(controlPanel, mapillaryContainer);

  //CONTROLPANEL
//Create Control Buttons with functionality and add them to index.html
  let evalButton =  '<button id="evalButton" type="button" class="btn btn-outline-dark mb-2 mt-2 mr-2">Evaluate</button>';
  let resetButton = '<button id="resetButton" type="button" class="btn btn-outline-dark mb-2 mt-2 mr-2">Restart</button>';
  $('#controlPanel').append(resetButton);
  $('#controlPanel').append(evalButton);
  //Button Functionality
  $('#resetButton').click(function() {
    location.reload();
  });
  $('#evalButton').click(function() {
    if(coordinates!=undefined){
      getDistance(imgKey, coordinates)
        .then((distance) => {
          clicked=true;
          showScore(distance);
          postSession(userID, imgKey, distance.distance); //Todo: Implement USER
        });

    }else{
      $('#controlPanel').append("<h4 id='noTipError' class='ml-4'>Please set your tip!</h4>");
      setTimeout(function () {
          $("#noTipError").remove();
      }, 2000);
    }

  });
  //create Mapillary View
  createMapillary();
  //create Leaflet View
  createLeaflet();
}
