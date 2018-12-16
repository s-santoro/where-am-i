
// get elements from modal
const submit = $("#registerSubmit");
const userSignUp = $("#usernameSignUp");
const passSignUp = $("#passwordSignUp");
const passConfSignUp = $("#passwordSignUpConfirm");
const userLogIn = $("#usernameLogIn");
const passLogIn = $("#passwordLogIn");

// add event-listeners to elements
submit.on("click", submitForm);
userSignUp.on("change", validateUserSignUp);
passSignUp.on("change", validatePassSignUp);
passConfSignUp.on("change", validatePassConfSignUp);
userLogIn.on("change", validateUserLogIn);
passLogIn.on("change", validatePassLogIn);



function submitForm() {
    let tabs = $(".tab-content").children();
    let activeTab = tabs.filter(index => tabs[index].classList.contains("active"));
    let id = activeTab[0].id;
    switch (id) {
        case "logIn":
            if(validateUserLogIn() && validatePassLogIn()) {
                let username = userLogIn.val();
                let password = passLogIn.val();
                emptyLogInput();
                let basic = "Basic " + username + ":" + password;
                fetch('http://localhost:9000/api/users/validate', {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json; charset=utf-8",
                        "Authorization": basic
                    }
                }).then(response => {
                    return response.status
                }).then((status) => {
                    switch (status) {
                        case 200:
                            // Successful Authorization
                            // TODO: set cookie
                            console.log(status);
                            break;
                        case 403:
                            // Failed Authorization
                            console.log(status);
                            submit.parent().prepend("<h4 id='logInError' class='mr-4'>Invalid user-credentials!</h4>");
                            setTimeout(function () {
                                $("#logInError").remove();
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
            if( validatePassConfSignUp() &&
                validatePassSignUp() &&
                validateUserSignUp())
            {
                // TODO: check if user already exists
                // TODO: add user
                console.log("sign up");
                let data = {
                    "username" : userSignUp.val().toLowerCase(),
                    "password" : passSignUp.val().toLowerCase(),
                    "avatar" : "1"
                };
                fetch('http://localhost:9000/api/users',{
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json; charset=utf-8",
                    },
                    body: JSON.stringify(data), // body data type must match "Content-Type" header
                })
                .then(response => {
                    emptySignUpInput();
                    // Give successful sign-up feedback
                    if(response.status === 200) {
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

function validateUserSignUp() {
    // only accept alphanumeric signs starting with a letter
    // all letters are lowercase
    let regex = /^\w+$/;
    let input = userSignUp.val().toLowerCase();
    let invalidDiv = '<div id="invalidUSU" class="invalid-feedback">Only use alphanumeric sings</div>';
    let nameShortDiv = '<div id="shortUSU" class="invalid-feedback">Name is too short</div>';
    $('#invalidUSU').remove();
    $('#shortUSU').remove();
    userSignUp.removeClass("is-invalid");
    if (input.length > 0 && input.length <= 3) {
        userSignUp.parent().append(nameShortDiv);
        userSignUp.addClass("is-invalid");
    }
    if(input.length > 0 && !regex.test(input)) {
        userSignUp.parent().append(invalidDiv);
        userSignUp.addClass("is-invalid");
    }
    return (input.length > 3 && regex.test(input));
}

function validatePassSignUp() {
    validatePassConfSignUp();
    // only accept alphanumeric signs starting with a letter
    // all letters are lowercase
    let regex = /^\w+$/;
    let input = passSignUp.val().toLowerCase();
    let invalidDiv = '<div id="invalidPSU" class="invalid-feedback">Only use alphanumeric sings</div>';
    let nameShortDiv = '<div id="shortPSU" class="invalid-feedback">Name is too short</div>';
    $('#invalidPSU').remove();
    $('#shortPSU').remove();
    passSignUp.removeClass("is-invalid");
    if (input.length > 0 && input.length <= 3) {
        passSignUp.parent().append(nameShortDiv);
        passSignUp.addClass("is-invalid");
    }
    if(input.length > 0 && !regex.test(input)) {
        passSignUp.parent().append(invalidDiv);
        passSignUp.addClass("is-invalid");
    }
    return (input.length > 3 && regex.test(input));
}

function validatePassConfSignUp() {
    // compare both password-inputs
    let input = passConfSignUp.val().toLowerCase();
    let inputPass = passSignUp.val().toLowerCase();
    let notEqualDiv = '<div id="notEqualPCSU" class="invalid-feedback">Password does not match</div>';
    $('#notEqualPCSU').remove();
    passConfSignUp.removeClass("is-invalid");
    if (input.length > 0 && input != inputPass) {
        passConfSignUp.parent().append(notEqualDiv);
        passConfSignUp.addClass("is-invalid");
    }
    return (input == inputPass);
}

function validateUserLogIn() {
    // only accept alphanumeric signs starting with a letter
    // all letters are lowercase
    let regex = /^\w+$/;
    let input = userLogIn.val().toLowerCase();
    let invalidDiv = '<div id="invalidULI" class="invalid-feedback">Only use alphanumeric sings</div>';
    $('#invalidULI').remove();
    userLogIn.removeClass("is-invalid");
    if(input.length > 0 && !regex.test(input)) {
        userLogIn.parent().append(invalidDiv);
        userLogIn.addClass("is-invalid");
    }
    return (regex.test(input));
}

function validatePassLogIn() {
// only accept alphanumeric signs starting with a letter
    // all letters are lowercase
    let regex = /^\w+$/;
    let input = passLogIn.val().toLowerCase();
    let invalidDiv = '<div id="invalidPLI" class="invalid-feedback">Only use alphanumeric sings</div>';
    $('#invalidPLI').remove();
    passLogIn.removeClass("is-invalid");
    if(input.length > 0 && !regex.test(input)) {
        passLogIn.parent().append(invalidDiv);
        passLogIn.addClass("is-invalid");
    }
    return (regex.test(input));
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