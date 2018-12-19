/**
 * Shows Score after Game Session
 */
function showScore(input) {
    $('#evalButton').hide();
    $('#mapillaryContainer').hide();

    let score = '<div id="scoreDiv" class="container">' +
        '<h4 class="text-center mt-3">Distance: '+ Math.round(input.distance) +'km</h4>' +
        '</div>';
    $('#app').prepend(score);
    createImageMarker(input);
}

/**
 * Posts Session to Database
 */
function postSession(userID, imgKey, score){
    if(userID!=null){
        let session = {
            "user_fk": userID,
            "location_fk": imgID,
            "score": Math.round(score),
            "timestamp": createISODate()
        }
        fetch(url + '/api/sessions',{
            method: 'post',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(session)
        });
    }
}

/**
 * Creates IOS Date for posting Session to Database
 */
function createISODate(){
    let date = new Date();
    let year = date.getFullYear();
    let month = date.getMonth()+1;
    let dt = date.getDate();
    if (dt < 10) {
        dt = '0' + dt;
    }
    if (month < 10) {
        month = '0' + month;
    }
    return year+'-' + month + '-'+dt;
}

/**
 * Calculates Average Score
 */
function calcAverageScore(dataInput, userID){
    let scoreArray = [];
    let sum = 0;
    let avg;
    dataInput.forEach((element) => {
        if(element.user_fk == userID){
            scoreArray.push(element.score);
        }
    });
    scoreArray.forEach((element) => {
        sum += element;
    });
    avg = Math.round(sum/scoreArray.length);
    return avg;
}

