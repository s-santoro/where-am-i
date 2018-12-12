/**
 * Fetch position-object from Mapillary-API with given imageKey,
 * call function calcDistance() and return absolute distance.
 * @param imageKey from image, which was used as start-location
 * @param userPos in format { lat: xxx, lng: xxx }
 * @returns distance as promise
 */
function getDistance(imageKey, userPos) {
  let posImage = null;
  let posUser = {
    latitude: userPos.lat,
    longitude: userPos.lng
  };
  let urlMapillary =
    "https://a.mapillary.com/v3/images/" +
    imageKey +
    "?client_id=" +
    mapillaryId;

  return fetch(urlMapillary)
  .then((response) => {
    return response.json();
  })
  .then(image => {
    posImage = {
      // Mapillary vertauscht Latitude und Longitude bei der Abfrage!
      latitude: image.geometry.coordinates[1],
      longitude: image.geometry.coordinates[0]
    };
    console.log(image.geometry.coordinates[0], image.geometry.coordinates[1]);
  })
  .then(() => {
    let distanceInfo = {
      distance: calcDistance(posUser.latitude, posUser.longitude, posImage.latitude, posImage.longitude),
      imagepos: posImage
    }
    return distanceInfo;
  });
}

/**
 * Add toRad-function to Number
 * Needed for calcDistance-function
 * @returns {number} in rad
 */
Number.prototype.toRad = function () {
  return this * Math.PI / 180;
};

/**
 * Haversine-Formula found on:
 *  https://stackoverflow.com/questions/14560999/using-the-haversine-formula-in-javascript
 * @returns {number}
 */
function calcDistance(lat1, lon1, lat2, lon2) {

  var R = 6371; // km
  //has a problem with the .toRad() method below.
  var x1 = lat2 - lat1;
  var dLat = x1.toRad();
  var x2 = lon2 - lon1;
  var dLon = x2.toRad();
  var a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
    Math.cos(lat1.toRad()) * Math.cos(lat2.toRad()) *
    Math.sin(dLon / 2) * Math.sin(dLon / 2);
  var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
  var d = R * c;
  return d;
}
