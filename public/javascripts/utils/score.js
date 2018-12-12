function showScore(input) {
  $('#controlPanel').hide();
  $('#mapillaryContainer').hide();

  let score = '<div id="scoreDiv" class="container">' +
    '<h4 class="text-center mt-3">Distanz: '+ Math.round(input.distance) +'km</h4>' +
    '</div>';
  $('#app').prepend(score);
  //score.insertBefore($('#leafletContainer'));
  console.log(input.imagepos.latitude,input.imagepos.longitude);
  createImageMarker(input);
}
