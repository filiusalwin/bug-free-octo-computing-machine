$(document).ready(function(){
    $(document).ready(function() {
        $("#customersearch").hide();
        $("#categoryList > button:first-child").trigger("click");

        $(document).on('change', 'input', function(){
            getUserFromSearch();
        });

        $("#searchUser").click(function() {
            this.value = "";
        });
    });
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

function getUserFromSearch() {
    var options = $('#userList')[0].options;
    for (var i=0; i < options.length; i++){
        if (options[i].value == $("#searchUser").val()) {
            $("#customersearch").show();
            var url2 = "user/update/" + username;

            $("#ShowInfoUser").html(options[i].label);
            return;
        }
    }
    // if not matching any username
    $("#searchUser").val("");
    $("#customersearch").hide();
}


