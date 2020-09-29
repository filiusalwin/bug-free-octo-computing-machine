$(document).ready(function() {
    $("#categoryList > button:first-child").trigger("click");

    $(document).on('change', 'input', function(){
        getUserFromSearch();
    });

    $("#searchUser").click(function() {
        this.value = "";
    });
    $("#usernameError, #ibanError").hide();
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

function checkCorrectRadioBox(userData) {
    if (userData.roles === "ROLE_CUSTOMER") {
        $("#customer").prop("checked", true);
    } else if (userData.roles === "ROLE_CUSTOMER,ROLE_BARTENDER") {
        $("#bartender").prop("checked", true);
    } else if (userData.roles === "ROLE_CUSTOMER,ROLE_BARTENDER,ROLE_BARMANAGER") {
        $("#barmanager").prop("checked", true);
    }
}

function fillOutForm(data) {
    document.getElementById("userForm").action = "/user/save";
    $("#modalLabel").html("Edit " + data.username);
    $("#usernameInput").val(data.username);
    $("#usernameError,#password_pincode").hide();
    $("#Prepaid-Choice-Label").show();
    $("#Prepaid-Choice-Label").html("The prepaid balance: " + data.balance);
    $("#userIdInput").val(data.userId);
    $("#nameInput").val(data.name);
    $("#Prepaid").prop("checked", data.prepaidAllowed);
    $("#prepaid_balance").val(data.balance);
    $("#Credit").prop("checked", data.creditAllowed);
    $("#credit_account").val(data.creditPaymentBankAccountNumber);
}
function addUserByUsername(username) {
    $.ajax({
        type: "GET",
        url: "/user/username/" + username,
        data: {
            username: username,
        },
    }).done(function (data) {
        fillOutForm(data);
        checkCorrectRadioBox(data);
    });
}
function deleteUser() {
   var userId = $("#userIdInput").val();
    console.log($("#userIdInput").val());
    window.location.href = "/user/delete/" + userId;
}
// see checkIfUsernameexists()
var newUser;

function resetNewUser() {
    newUser = false;
}

function openModalNewUser() {
    newUser = true;
    $('#maintainUserModal').modal('show');
    $("#modalLabel").html("New User");
    $("#usernameInput, #password, #userIdInput, #nameInput, #credit_account").val("");
    $("#usernameError, #Prepaid-Choice-Label").hide();
    $("#password_pincode").show();
    $("#password, #pin").attr("required", "");
    $("#customer").prop("checked",true);
    $("#bartender, #barmanager, #Prepaid, #Credit").prop("checked",false);
}

function checkIfUserNameExists() {
    if(newUser === true) {
        username1 = $("#usernameInput").val();
        $.ajax({
            type: "GET",
            url: "/user/byUsername/" + username1,
            data: {
                username: username1,
            },
        }).done(function getUserData(userData) {
            if (userData.username === username1) {
                $("#usernameError").show();
            } else {
                $("#usernameError").hide();
            }
        });
    }
}


function ibanValidation() {
    var iban = $("#credit_account").val();
    $.ajax({
        type: "GET",
        url: "/user/ibanValid/",
        data: {
            iban: iban,
        },
    }).done(function(ibanData) {

        console.log(ibanData);

        if (!ibanData) {
            $("#ibanError").show();
        } else {
            $("#ibanError").hide();
        }
    });
}