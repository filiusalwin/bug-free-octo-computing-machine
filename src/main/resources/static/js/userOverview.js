$(document).ready(function(){
    $(document).ready(function() {
        $("#categoryList > button:first-child").trigger("click");

        $(document).on('change', 'input', function(){
            getUserFromSearch();
        });

        $("#searchUser").click(function() {
            this.value = "";
        });
        $("#usernameError").hide();
    });
});

function getUserFromSearch() {
    var options = $('#userList')[0].options;
    for (var i=0; i < options.length; i++){
        if (options[i].value == $("#searchUser").val()) {
            addUserByUsername($("#searchUser").val())
            $('#maintainUserModal').modal('show');
            return;
        }
    }
}

function addUser(userId) {
    $.ajax({
        type: "GET",
        url: "/user/" + userId,
        data: {
            userId: userId,
        },
    }).done(function getUserData (userData) {
            $("#usernameInput").val(userData.username);
            $("#usernameError").hide();
            $("#userIdInput").val(userData.userId);
            $("#nameInput").val(userData.name);
            $("#Prepaid").prop("checked", userData.prepaidAllowed);
            $("#prepaid_balance").val(userData.balance);
            $("#Credit").prop("checked", userData.creditAllowed);
            $("#credit_account").val(userData.creditPaymentBankAccountNumber);

            // to check radio boxes
            if (userData.roles === "ROLE_CUSTOMER") {
                $("#customer").prop("checked", true);
            } else if (userData.roles === "ROLE_CUSTOMER,ROLE_BARTENDER") {
                $("#bartender").prop("checked", true);
            } else if (userData.roles === "ROLE_CUSTOMER,ROLE_BARTENDER,ROLE_BARMANAGER") {
                $("#barmanager").prop("checked", true);
            }
    });
}
function addUserByUsername(username) {
    $.ajax({
        type: "GET",
        url: "/user/byUsername/" + username,
        data: {
            username: username,
        },
    }).done(function getUserData (userData) {
        $("#usernameInput").val(userData.username);
        $("#usernameError").hide();
        $("#userIdInput").val(userData.userId);
        $("#nameInput").val(userData.name);
        $("#Prepaid").prop("checked", userData.prepaidAllowed);
        $("#prepaid_balance").val(userData.balance);
        $("#Credit").prop("checked", userData.creditAllowed);
        $("#credit_account").val(userData.creditPaymentBankAccountNumber);

        // to check radio boxes
        if (userData.roles === "ROLE_CUSTOMER") {
            $("#customer").prop("checked", true);
        } else if (userData.roles === "ROLE_CUSTOMER,ROLE_BARTENDER") {
            $("#bartender").prop("checked", true);
        } else if (userData.roles === "ROLE_CUSTOMER,ROLE_BARTENDER,ROLE_BARMANAGER") {
            $("#barmanager").prop("checked", true);
        }
    });
}
function deleteUser() {
   var userId = $("#userIdInput").val();
    console.log($("#userIdInput").val());
    window.location.href = "/user/delete/" + userId;
}

function openModalNewUser() {
    $('#maintainUserModal').modal('show');
    $("#usernameInput").val("");
    $("#usernameError").hide();
    $("#password").val("");
    $("#userIdInput").val("");
    $("#nameInput").val("");
    $("#customer").prop("checked",false);
    $("#bartender").prop("checked",false);
    $("#barmanager").prop("checked",false);
    $("#Prepaid").prop("checked", false);
    $("#prepaid_balance").val("");
    $("#Credit").prop("checked", false);
    $("#credit_account").val("");
}

function checkIfUserNameExists() {
    username1 = $("#usernameInput").val();
    console.log(username1);
    $.ajax({
        type: "GET",
        url: "/user/byUsername/" + username1,
        data: {
            username: username1,
        },
    }).done(function getUserData (userData) {
        if (userData.username === username1) {
            $("#usernameError").show();
        }
    });
}

