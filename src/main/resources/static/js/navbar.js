$(document).ready(function() {

    timeout = setTimeout(lockScreen, 10000);

    $(window).click(function() {
        console.log("click")
        resetTimeout();
    })
    $(window).keyup(function() {
        console.log("key")
        resetTimeout();
    })
    $(window).mousemove(function() {
        console.log("mouse")
       resetTimeout(timeout);
    })
});
// 5 min is 18000000 ms
var timeout;

function lockScreen() {
    window.location.assign("/lockout?error");
}

function resetTimeout(){
    clearTimeout(timeout);
    timeout = setTimeout(lockScreen, 18000000);
}