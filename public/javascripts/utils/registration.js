// get elements from modal
const submit = $("#registerSubmit");

// add event-listeners to elements
submit.on("click", submitForm);

function submitForm() {
    let tabs = $(".tab-content").children();
    let activeTab = tabs.filter(index => tabs[index].classList.contains("active"));
    let id = activeTab[0].id;
    switch (id) {
        case "logIn":
            if (validateUserLogIn() && validatePassLogIn()) {
                let username = userLogIn.val();
                let basic = "Basic " + username + ":" + passLogIn.val();
                fetch('http://localhost:9000/api/users/login', {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json; charset=utf-8",
                        "Authorization": basic
                    }
                }).then(response => {
                    emptyLogInput();
                    return response.status
                }).then((status) => {
                    switch (status) {
                        case 200:
                            // Successful Authorization
                            // TODO: set cookie
                            $('#registerModal').modal('toggle');
                            setUserMenu(username);
                            setUserID();
                            break;
                        case 403:
                            // Failed Authorization
                            submit.parent().prepend("<h4 id='logInFail' class='mr-4'>Invalid user-credentials!</h4>");
                            setTimeout(function () {
                                $("#logInFail").remove();
                            }, 2000);
                            break;
                        default:
                            // Unexpected Error
                            console.log("Unexpected error at: ", status);
                            break;
                    }
                }).catch(err => console.log(err));
            }
            break;
        case "signUp":
            if (validatePassConfSignUp() &&
                validatePassSignUp() &&
                validateUserSignUp()) {
                console.log("sign up");
                let data = {
                    "username": userSignUp.val().toLowerCase(),
                    "password": passSignUp.val().toLowerCase(),
                    "avatar": "1"
                };
                fetch('http://localhost:9000/api/users', {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json; charset=utf-8",
                    },
                    body: JSON.stringify(data), // body data type must match "Content-Type" header
                })
                .then(response => {
                    emptySignUpInput();
                    // Give successful sign-up feedback
                    if (response.status === 200) {
                        submit.parent().prepend("<h4 id='signUpSuccess' class='mr-4'>Account created, please log in!</h4>");
                        setTimeout(function () {
                            $("#signUpSuccess").remove();
                        }, 2000);
                    }
                    // Give failed sign-up feedback
                    if (response.status === 404) {
                        submit.parent().prepend("<h4 id='signUpFail' class='mr-4'>Username already in use!</h4>");
                        setTimeout(function () {
                            $("#signUpFail").remove();
                        }, 2000);
                    }
                })
                .catch(error => {
                    emptySignUpInput();
                    console.log(error)
                });
            }
            break;
        default:
            // Unexpected error
            console.log("Unexpected error at: ", id);
            break;
    }
}

function emptySignUpInput() {
    userSignUp.val("");
    userSignUp.attr("placeholder", "Username");
    passSignUp.val("");
    passSignUp.attr("placeholder", "Password");
    passConfSignUp.val("");
    passConfSignUp.attr("placeholder", "Confirm Password");
}

function emptyLogInput() {
    userLogIn.val("");
    userLogIn.attr("placeholder", "Username");
    passLogIn.val("");
    passLogIn.attr("placeholder", "Password");
}

function setUserID(){
    clicked = false;
    let username = extractUser();
    fetch('http://localhost:9000/api/users/getId?username='+ username)
        .then(function(response) {
            return response.json();
        })
        .then(function(myJson) {
            userID = myJson;
        });
}