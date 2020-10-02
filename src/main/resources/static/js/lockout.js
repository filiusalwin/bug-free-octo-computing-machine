$(document).ready(function() {
    // In load - no back
    noBack();
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

function lockscreen() {
    window.location.assign("/lockout?error");
}

function noBack() {
    window.history.forward();
}

function goBack() {
    window.history.back();
}
