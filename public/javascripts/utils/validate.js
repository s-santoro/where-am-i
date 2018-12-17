// get elements from modal
const userSignUp = $("#usernameSignUp");
const passSignUp = $("#passwordSignUp");
const passConfSignUp = $("#passwordSignUpConfirm");
const userLogIn = $("#usernameLogIn");
const passLogIn = $("#passwordLogIn");

// add event-listeners to elements
userSignUp.on("change", validateUserSignUp);
passSignUp.on("change", validatePassSignUp);
passConfSignUp.on("change", validatePassConfSignUp);
userLogIn.on("change", validateUserLogIn);
passLogIn.on("change", validatePassLogIn);

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
    if (input.length > 0 && !regex.test(input)) {
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
    if (input.length > 0 && !regex.test(input)) {
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
    if (input.length > 0 && !regex.test(input)) {
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
    if (input.length > 0 && !regex.test(input)) {
        passLogIn.parent().append(invalidDiv);
        passLogIn.addClass("is-invalid");
    }
    return (regex.test(input));
}