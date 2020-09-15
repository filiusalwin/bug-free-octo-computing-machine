$(document).ready(function(){
    console.log($("#CreditAllowedHidden").val());
    if ($("#CreditAllowedHidden").val() == 'true') {
        $("#Credit-Choice").show();
        $("#Credit-Choice-Label").show();
    } else {
        $("#Credit-Choice").hide();
        $("#Credit-Choice-Label").hide();
    }
    if ($("#PrepaidAllowedHidden").val() == 'true') {
        $("#Prepaid-Choice").show();
        $("#Prepaid-Choice-Label").show();
    } else {
        $("#Prepaid-Choice").hide();
        $("#Prepaid-Choice-Label").hide();
    }
    $("#Credit").click(function() {
        $("#Credit-Choice").toggle();
        $("#Credit-Choice-Label").toggle();
    });
    $("#Prepaid").click(function() {
        $("#Prepaid-Choice").toggle();
        $("#Prepaid-Choice-Label").toggle();
    });
});

function updateSelectedUser(){
    var username = $("#searchUser").val();
    var url = "/user/update/username/" + username;
    window.location = url;
}
