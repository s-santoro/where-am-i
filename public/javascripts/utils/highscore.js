/*
  javascript for highscore-rendering
 */

function highscore(context) {
    let table = '<table class="table table-striped">'+
                    '<thead>'+
                        '<tr>'+
                            '<th scope="col">#</th>'+
                            '<th scope="col">User</th>'+
                            '<th scope="col">Number of Games</th>'+
                            '<th scope="col">Average Score</th>'+
                        '</tr>'+
                    '</thead>'+
                    '<tbody id ="tableContent">'+
                    '</tbody>'+
                '</table>';
    $('#app').append(table);
    generateTableContent();

}

function generateTableContent() {
    //Create Array with Users
    let userArray = [];
    fetch('http://localhost:9000/api/users')
        .then(function(response) {
            return response.json();
        })
        .then(function(myJson) {
            myJson.forEach((element) => {
                let user = {user_fk: element.id, username: element.username, avg_score: 0, nrOfGames: 0};
                userArray.push(user);
            });
            return userArray;
        })
        .then(function(userArray){
            //Get Session Information to fill Array
            fetch('http://localhost:9000/api/sessions')
                .then(function(response) {
                    return response.json();
                })
                .then(function(Sessions) {
                    //Fill Array with Information
                    userArray.forEach((userElement) => {
                        Sessions.forEach((sessionElement) => {
                            if(userElement.user_fk==sessionElement.user_fk){
                                userElement.avg_score+=sessionElement.score;
                                userElement.nrOfGames++;
                            }
                        });
                    });
                    //Remove Users with zero number of Games
                    console.log(userArray);
                    userArray.forEach((userElement, index) => {
                        console.log(index);
                        if(userElement.nrOfGames==0){
                            userArray.splice(index, 1);
                        }
                    });
                    //Calculate Average Score
                    userArray.forEach((userElement) => {
                        userElement.avg_score = Math.round(userElement.avg_score/userElement.nrOfGames);
                    });
                    //Sort Table for average Score
                    userArray.sort((a,b) => (a.avg_score > b.avg_score) ? 1 : ((b.avg_score > a.avg_score) ? -1 : 0));
                    // Put Information into Table
                    let counter = 1;
                    userArray.forEach((userElement) => {
                        let userRow =   '<tr>'+
                            '<th scope="row">'+ counter +'</th>'+
                            '<td>'+ userElement.username +'</td>'+
                            '<td>'+ userElement.nrOfGames +'</td>'+
                            '<td>'+ userElement.avg_score +'</td>'+
                            '</tr>';
                        $('#tableContent').append(userRow);
                        counter++;
                    });
                })
        });
}
