$(document).ready(function() {
    // the screen is locked
    if (window.location.href.indexOf("lockout?error") > -1) {
        $("#sub").show();
        $("#lock").hide();
        $("#cancel").hide();
    } else {
        $("#sub").hide();
        setTimeout(lockscreen, 3000);
    }
});

var lockUrl = false;

function lockscreen() {
    lockUrl = true;
    window.location.assign("/lockout?error");
}

function noBack() {
    window.history.forward();
}

function goBack() {
    window.history.back();
}
