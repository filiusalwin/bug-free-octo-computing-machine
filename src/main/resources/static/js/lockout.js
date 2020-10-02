$(document).ready(function() {
    // In load - no back
    noBack();

    // the screen is not locked
    if (lockUrl === false) {
        $("#sub").hide();
    }

    // the screen is locked
    if (window.location.href.indexOf("lockout?error") > -1) {
        $("#sub").show();
        $("#lock").hide();
        $("#cancel").hide();
    }
});

var lockUrl = false;

function lockscreen() {
    lockUrl = true;
    window.location("/lockout?error");
}

function noBack() {
    window.history.forward();
}

function goBack() {
    window.history.back();
}
