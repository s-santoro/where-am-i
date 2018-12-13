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
    let highscoreInfo = {
        user: userArray[importUser()],
        games: games[test],
        average: average[createAverageScore()]
    };
    importUser();
}

function importUser() {
    let userArray = [];
    fetch('http://localhost:9000/api/users')
        .then(function(response) {
            return response.json();
        })
        .then(function(myJson) {
            myJson.forEach((element) => {
                userArray.push(element.username);
            });
        });
    return userArray;
}
