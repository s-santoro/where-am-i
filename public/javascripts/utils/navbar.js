const navbarUser = $("#navbarUser");

function setUserMenu(username) {
    let id = null;
    fetch("http://localhost:9000/api/users/getId?username=" + username)
    .then(response => response.json())
    .then((json) => {
        id = json;
    })
    .then(() => {
        navbarUser.html(
            '<li class="nav-item dropdown">' +
            '<a class="nav-link dropdown-toggle" href="#" id="navbarUserInfo"' +
            'role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">' +
            'logged in as: ' + username + '</a>' +
            '<ul class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarUserInfo">' +
            '<li><a id="userStatistics" class="dropdown-item" href="#/">User statistics</a></li>' +
            '<li><a class="dropdown-item" href="#/">Change avatar</a></li>' +
            '<li><a id="logOut" class="dropdown-item" href="#/game">Log out</a></li>' +
            '</ul>' +
            '</li>');

        $("#logOut").on("click", logOut);
        $("#userStatistics").on("click", function () {
            createUserInfo(id);
        });
    })
    .catch(err => console.log(err));
}

function logOut() {
    // TODO: delete cookie
    resetNavbar();
}

function resetNavbar() {
    navbarUser.html(
        '<li class="nav-item">' +
        '<a class="nav-link" href="#" data-toggle="modal" data-target="#registerModal">User</a>' +
        '</li>'
    );
}