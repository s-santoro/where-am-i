/**
 * Javascript for showing the highscore-board
 */
function highscore(context) {
    generateTableContent();
}

/**
 * Fetches highscore-list from api
 */
function generateTableContent() {
    //Create Array with Users
    let userArray = [];
    fetch(url + '/api/highscore')
        .then(response => {
            if(response.status !== 200) {
                $('#app').append("<h1>highscore-board not available at the moment!</h1>");
            }
            return response.text();
        })
    .then(text => {
        $('#app').append(text);
    })
    .catch(err => console.log(err));
}

