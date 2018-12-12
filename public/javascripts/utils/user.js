/*
  javascript for user-rendering
 */


function user(context) {

  fetch('http://localhost:9000/api/sessions')
    .then(function(response) {
      return response.json();
    })
    .then(function(myJson) {
        createLeaflet();
        createChart(preprocessData(myJson, 1)); //Statisch nur Elemente für User 1
    });
}


