const navbarUser = $("#navbarUser");

function setUserMenu(username) {
    navbarUser.html(
        '<li class="nav-item dropdown">' +
        '<a class="nav-link dropdown-toggle" href="#" id="navbarUserInfo"' +
        'role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">' +
        'logged in as: ' + username + '</a>' +
        '<ul class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarUserInfo">' +
        '<li><a id="userStatistics" class="dropdown-item" href="#/user">User statistics</a></li>' +
        '<li><a class="dropdown-item" href="#/">Change avatar</a></li>' +
        '<li><a id="logOut" class="dropdown-item" href="#/game">Log out</a></li>' +
        '</ul>' +
        '</li>');

    $("#logOut").on("click", logOut);
}

function logOut() {
    // TODO: delete cookie
    fetch('http://localhost:9000/api/users/logout')
    .then(response => {
        emptyLogInput();
        return response.status
    }).then((status) =>  {
        console.log(status);
    }).catch(err => console.log(err));

    resetNavbar();
}

function resetNavbar() {
    navbarUser.html(
        '<li class="nav-item">' +
        '<a class="nav-link" href="#" data-toggle="modal" data-target="#registerModal">User</a>' +
        '</li>'
    );
}