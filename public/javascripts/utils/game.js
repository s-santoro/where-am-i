

const mapillaryId = 'UlhwcmNzcEs2TFpQaEQ1TEFRaWtaZzo1MDk0NjU0OGY5YjNjYzA2';
let clicked = false;
let userID;

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
          postSession(userID, imgKey, distance.distance); //Todo: Implement USER
        });

    }else{
      alert("Please set your tip");
    }

  });
  //MAPILLARY
  createMapillary();
  //LEAFLET
  createLeaflet();
  //USER
  getUserID();

}

function getUserID(){
  let username = document.cookie;
  username = username.substring(10);
    fetch('http://localhost:9000/api/users/getId?username='+ username)
        .then(function(response) {
            return response.json();
        })
        .then(function(myJson) {
          userID = myJson;
          console.log(userID);
        });
}
