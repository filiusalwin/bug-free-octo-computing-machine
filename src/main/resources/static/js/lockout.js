const LOCKSCREEN_COUNTDOWN = 1; // 1 milliseconds then the app will be automatic locked
const LOG_OUT_COUNTDOWN = 7200000; // 2 hours (7200000 ms) then the app wil log out
const MESSAGE_COUNTDOWN = 5000; // 10 second then the locked message will be automatic disappear


$(document).ready(function() {
    // On load - no back
    noBack();

    // the screen is locked
    if (window.location.href.indexOf("lockout?error") > -1) {
        $("#sub").show();
        $("#lock, #logout").hide();
        setTimeout(logout, LOG_OUT_COUNTDOWN);
        setTimeout(hideMessage,MESSAGE_COUNTDOWN);
    } else {
        $("#sub").hide();
        $("#lock").hide();
        setTimeout(lockscreen, LOCKSCREEN_COUNTDOWN);
    }
});

function hideMessage() {
    $(".error").hide();
    $("#logout").show();
}

function lockscreen() {
    window.location.assign("/lockout?error");
}

function logout() {
    window.location.assign("/bacchuxLogout");
}

function noBack() {
    window.history.forward();
}

function logout(){
    window.location.assign("/logout")
}

$(function(){

    // Cache some selectors

    var clock = $('#clock'),
        ampm = clock.find('.ampm');

    // Map digits to their names (this will be an array)
    var digit_to_name = 'zero one two three four five six seven eight nine'.split(' ');

    // This object will hold the digit elements
    var digits = {};

    // Positions for the hours, minutes, and seconds
    var positions = [
        'h1', 'h2', ':', 'm1', 'm2', ':', 's1', 's2'
    ];

    // Generate the digits with the needed markup, and add them to the clock

    var digit_holder = clock.find('.digits');

    $.each(positions, function(){

        if(this == ':'){
            digit_holder.append('<div class="dots">');
        }
        else{

            var pos = $('<div>');

            for(var i=1; i<8; i++){
                pos.append('<span class="d' + i + '">');
            }

            // Set the digits as key:value pairs in the digits object
            digits[this] = pos;

            // Add the digit elements to the page
            digit_holder.append(pos);
        }

    });

    // Add the weekday names

    var weekday_names = 'MON TUE WED THU FRI SAT SUN'.split(' '),
        weekday_holder = clock.find('.weekdays');

    $.each(weekday_names, function(){
        weekday_holder.append('<span>' + this + '</span>');
    });

    var weekdays = clock.find('.weekdays span');

    // Run a timer every second and update the clock

    (function update_time(){

        // Use moment.js to output the current time as a string
        // hh is for the hours in 12-hour format,
        // mm - minutes, ss-seconds (all with leading zeroes),
        // d is for day of week and A is for AM/PM

        var now = moment().format("hhmmssdA");

        digits.h1.attr('class', digit_to_name[now[0]]);
        digits.h2.attr('class', digit_to_name[now[1]]);
        digits.m1.attr('class', digit_to_name[now[2]]);
        digits.m2.attr('class', digit_to_name[now[3]]);
        digits.s1.attr('class', digit_to_name[now[4]]);
        digits.s2.attr('class', digit_to_name[now[5]]);

        // The library returns Sunday as the first day of the week.
        // Lets shift all the days one position down and make Sunday last

        var dow = now[6];
        dow--;

        // Sunday!
        if(dow < 0){
            // Make it last
            dow = 6;
        }

        // Mark the active day of the week
        weekdays.removeClass('active').eq(dow).addClass('active');

        // Set the am/pm text:
        ampm.text(now[7]+now[8]);

        // Schedule this function to be run again in 1 sec
        setTimeout(update_time, 1000);

    })();

    // Switch the theme

    $('a.button').click(function(){
        clock.toggleClass('light dark');
        $("body").next('div').slideToggle('slow', function() {});
        $("body").toggleClass('active');
    });

});