$(document).ready(function() {
    var timeout = setTimeout(lockScreen,100000);
    if(window.event) {
        clearTimeout(timeout);
        timeout = setTimeout(lockScreen,100000);
    }

});
// 5 min is 18000000 ms

function lockScreen() {
    window.location("/lockout?error");
}