function showScore(input) {
    $('#controlPanel').hide();
    $('#mapillaryContainer').hide();

    let score = '<div id="scoreDiv" class="container">' +
        '<h4 class="text-center mt-3">Distance: '+ Math.round(input.distance) +'km</h4>' +
        '</div>';
    $('#app').prepend(score);
    createImageMarker(input);
}

function postSession(user, imgKey, score){
    let session = {
        "user_fk": user,
        "location_fk": imgID,
        "score": Math.round(score),
        "timestamp": createISODate()
    }
    fetch('http://localhost:9000/api/sessions',{
        method: 'post',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(session)
    });
}

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
    avg = sum/scoreArray.length;
    console.log(avg);
    return avg;
}

