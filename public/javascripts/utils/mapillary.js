let imgKey;
let imgID;
/**
 * Creates Mapillary View for Game session
 */
function createMapillary() {
//GET Image-Keys and take Random for initializing Mapillary
  fetch('http://localhost:9000/api/locations')
    .then(function(response) {
      return response.json();
    })
    .then(function(myJson) {
      let imgKeys = myJson;
      tempKey = imgKeys[Math.floor(Math.random() * imgKeys.length)];
      imgID = tempKey['id'];
      imgKey = tempKey['imageKey'];
      // Create Div for Mapillary and add it to index.html
      let mapillaryDiv = '<div id="mly" style="width: 100%; height: 300px;"></div>';
      $('#mapillaryContainer').append(mapillaryDiv);
      let mly = new Mapillary.Viewer(
        'mly',
        mapillaryId,
        imgKey
      );
    });
}
