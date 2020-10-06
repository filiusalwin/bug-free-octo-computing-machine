const LOCKSCREEN_COUNTDOWN = 5000; // 5 second then the app will be automatic locked
const LOG_OUT_COUNTDOWN = 7200000; // 2 hours (7200000 ms) then the app wil log out

$(document).ready(function() {
    // On load - no back
    noBack();

    // the screen is locked
    if (window.location.href.indexOf("lockout?error") > -1) {
        $("#sub").show();
        $("#lock").hide();
        $("#cancel").hide();
        setTimeout(logout, LOG_OUT_COUNTDOWN);
    } else {
        $("#sub").hide();
        setTimeout(lockscreen, LOCKSCREEN_COUNTDOWN);
    }
});

function lockscreen() {
    window.location.assign("/lockout?error");
}

function noBack() {
    window.history.forward();
}

function goBack() {
    window.history.back();
}

function logout(){
    window.location.assign("/logout")
}
