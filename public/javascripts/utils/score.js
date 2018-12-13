function showScore(input) {
    $('#controlPanel').hide();
    $('#mapillaryContainer').hide();

    let score = '<div id="scoreDiv" class="container">' +
        '<h4 class="text-center mt-3">Distance: '+ Math.round(input.distance) +'km</h4>' +
        '</div>';
    $('#app').prepend(score);
    //score.insertBefore($('#leafletContainer'));
    console.log(input.imagepos.latitude,input.imagepos.longitude);
    createImageMarker(input);
}

function postSession(user, imgKey, score){
    let session = {
        "user_fk": user,
        "location_fk": imgID,
        "score": Math.round(score),
        "timestamp": createISODate()
    }
    console.log(session);
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

