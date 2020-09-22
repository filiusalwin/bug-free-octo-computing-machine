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

function addUser(userId) {
    $.ajax({
        type: "GET",
        url: "/user/" + userId,
        data: {
            userId: userId,
        },
    }).done(function getUserData (userData) {
        console.log(userData);
        $("#usernameInput").val(userData.username);
        $("#nameInput").val(userData.name);
        $("#Prepaid"). prop("checked", userData.prepaidAllowed);
        $("#prepaid_balance").val(userData.balance);
        $("#Credit").prop("checked",userData.creditAllowed);
        $("#credit_account").val(userData.creditPaymentBankAccountNumber);

        // to check radio boxes
        if(userData.roles === "ROLE_CUSTOMER") {
            $("#customer").prop("checked",true);
        } else if (userData.roles === "ROLE_CUSTOMER,ROLE_BARTENDER") {
            $("#bartender").prop("checked",true);
        } else if (userData.roles === "ROLE_CUSTOMER,ROLE_BARTENDER,ROLE_BARMANAGER") {
            $("#barmanager").prop("checked",true);
        }
    });
}


