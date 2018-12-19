const url = 'https://where-am-i-located.herokuapp.com';
/**
 * Defines new Sammy application, binds it to #app, sets new route and replaces Content from element app with ''
 */
var app = $.sammy('#app', function() {
    this.get('#/game', function(context) {
        context.app.swap('');
        setNavbarAccordingCookie();
        initiateUser();
        game(context);
    });
     this.get('#/user', function(context) {
         context.app.swap('');
         setNavbarAccordingCookie();
         initiateUser();
         user(context);
     });
    this.get('#/highscore', function (context) {
        context.app.swap('');
        setNavbarAccordingCookie();
        initiateUser();
        highscore(context);
    });
});
/**
 * Sets start page to "game"
 */
$(function(){
    app.run("#/game");
});







