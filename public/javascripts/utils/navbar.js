const navbarUser = $("#navbarUser");

// detect any window-change and set navbar according to cookie correctly
function setNavbarAccordingCookie() {
    if(document.cookie.length === 0) {
        resetNavbar();
    }
    else {
        setUserMenu(extractUser());
    }
}


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
    fetch('http://localhost:9000/api/users/logout')
    .then(response => {
        emptyLogInput();
        return response.status
    }).then((status) =>  {
        setNavbarAccordingCookie();
        userID = undefined;
    }).catch(err => console.log(err));
}

function resetNavbar() {
    navbarUser.html(
        '<li class="nav-item">' +
        '<a class="nav-link" href="#" data-toggle="modal" data-target="#registerModal">User</a>' +
        '</li>'
    );
}

function extractUser() {
    return document.cookie.replace("logged-in=", "");
}