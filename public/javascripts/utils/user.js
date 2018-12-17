/*
  javascript for user-rendering
 */
//let userID = 1;

function user(context) {
    createUserInfo(userID);

}

function createAverageScore(dataInput, userID){
    let avgScore = '<h4 class="text-center mt-3 mb-3">Average Score: '+ calcAverageScore(dataInput, userID) +' km</h4>';
    $('#app').append(avgScore);
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
                '</div>'; //Todo: Implement USER
            $('#app').prepend(user);
        });
    //Average Score and Chart
    fetch('http://localhost:9000/api/sessions')
        .then(function(response) {
            return response.json();
        })
        .then(function(myJson) {
            //Average Score
            createAverageScore(myJson, userID); //Todo: Implement USER
            //Chart
            createChart(preprocessData(myJson, userID)); //Todo: Implement USER
        });
}






