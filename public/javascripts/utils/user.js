/*
  javascript for user-rendering
 */

function user(context) {
    createUserInfo(userID);
}

function createUserInfo(userID){
    app.swap('');
    fetch('http://localhost:9000/api/users/' + userID)
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
    fetch('http://localhost:9000/api/sessions')
        .then(function(response) {
            return response.json();
        })
        .then(function(myJson) {
            //Average Score
            createAverageScore(myJson, userID);
            //Chart
            createChart(preprocessData(myJson, userID));
        });
}


function createAverageScore(dataInput, userID){
    let avg = calcAverageScore(dataInput, userID);
    console.log(avg);
    let avgScore;
    if(avg>0){
        avgScore = '<h4 class="text-center mt-3 mb-3">Average Score: '+ avg +' km</h4>';
    }else{
        avgScore = '<h4 class="text-center mt-3 mb-3">Sie haben noch kein Spiel gespielt</h4>';
    }
    $('#app').append(avgScore);
}






