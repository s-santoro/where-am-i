/**
 * Javascript for rendering Userstatistics
 */

function user(context) {
    createUserInfo(userID);
}

/**
 * Fetches Data for Userstatistics
 */
function createUserInfo(userID){
    app.swap('');
    fetch(url + '/api/users/' + userID)
        .then(function(response) {
            return response.json();
        })
        .then(function(myJson) {
            let user =  '<div id="scoreDiv" class="container">' +
                '<h4 class="text-center mt-3 mb-3">User: '+ myJson.username +'</h4>' +
                '</div>';
            $('#app').prepend(user);
        });
    //Average Score and Chart
    fetch(url + '/api/sessions')
        .then(function(response) {
            return response.json();
        })
        .then(function(myJson) {
            //Calculate Average Score
            createAverageScore(myJson, userID);
            //Chart.js
            createChart(preprocessData(myJson, userID));
        });
}

/**
 * Calculates Average Score for Userstatistics
 */
function createAverageScore(dataInput, userID){
    let avg = calcAverageScore(dataInput, userID);
    let avgScore;
    if(avg>0){
        avgScore = '<h4 class="text-center mt-3 mb-3">Average Score: '+ avg +' km</h4>';
    }else{
        avgScore = '<h4 class="text-center mt-3 mb-3">Sie haben noch kein Spiel gespielt</h4>';
    }
    $('#app').append(avgScore);
}






