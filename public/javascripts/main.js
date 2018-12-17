/**
 * Defines new Sammy application, binds it to #app, sets new route and replaces Content from element app with ''
 */
var app = $.sammy('#app', function() {
    this.get('#/game', function(context) {
        context.app.swap('');
        setNavbarAccordingCookie();
        game(context);
    });
     this.get('#/user', function(context) {
         context.app.swap('');
         setNavbarAccordingCookie();
         user(context);
     });
    this.get('#/highscore', function (context) {
        context.app.swap('');
        setNavbarAccordingCookie();
        highscore(context);
    });
});
/**
 * Sets start page to "game"
 */
$(function(){
    app.run("#/game");
});







