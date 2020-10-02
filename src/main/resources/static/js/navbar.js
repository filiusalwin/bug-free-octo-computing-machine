var timeout;
const TIMEOUT_LENGTH = 3000000;
$(document).ready(function() {
    if (window.location.href.includes("lockout")) {
        return;
    }
    resetTimeout();
    $(window).click(resetTimeout);
    $(window).keyup(resetTimeout);
    $(window).mousemove(resetTimeout);
});

function goToLockScreen() {
    window.location.assign("/lockout");
}

function resetTimeout(){
    clearTimeout(timeout);
    timeout = setTimeout(goToLockScreen, TIMEOUT_LENGTH);
}