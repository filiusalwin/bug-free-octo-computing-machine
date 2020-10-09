// ---- Globals ---- \\
var newUser;


// ---- Onload ---- \\
$(document).ready(function() {
    setTimeout(function() {
        $(".alert").alert('close');
    }, 5000);

    $(document).on('change', 'input', function(){
        getUserFromSearch();
    });

    $("#searchUser").click(function() {
        this.value = "";
    });
    $("#usernameError, #ibanError").hide();
});


// ---- User Search ---- \\
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


// ---- Modal ---- \\
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
    $("#userForm").attr("action", "/user/save");
    $("#modalLabel").html("Edit " + data.username);
    $("#usernameInput, #originalUsername").val(data.username);
    $("#usernameError,#password_pincode").hide();
    $("#Prepaid-Choice-Label, #resetPassword").show();
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

function resetNewUser() {
    newUser = false;
}

function openModalNewUser() {
    newUser = true;
    $("#originalUsername").val("");
    $("#userForm").attr("action", "/user/add");
    $('#maintainUserModal').modal('show');
    $("#modalLabel").html("New User");
    $("#usernameInput, #password, #userIdInput, #nameInput, #credit_account").val("");
    $("#usernameError, #Prepaid-Choice-Label, #resetPassword").hide();
    $("#password_pincode").show();
    $("#password, #pin").attr("required", "");
    $("#customer").prop("checked",true);
    $("#bartender, #barmanager, #Prepaid, #Credit").prop("checked",false);
}


// ---- Modal Checks ---- \\
function checkIfUserNameExists() {
    username = $("#usernameInput").val();
    $.ajax({
        type: "GET",
        url: "/order/username/" + username,
        statusCode: {
            404: function () {
                return;
            }
        }
    }).done(function (data) {
        if (data !== null) {
            $("#usernameError").show();
            return;
        }
        $("#usernameError").hide();
    });
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
        if (!ibanData && iban) {
            $("#ibanError").show();
            $("#saveButton").prop("disabled", true);
        } else {
            $("#ibanError").hide();
            $("#saveButton").prop("disabled", false);
        }

    });
}


// ---- Direct Links ---- \\
function deleteUser() {
   var userId = $("#userIdInput").val();
    console.log($("#userIdInput").val());
    window.location.href = "/user/delete/" + userId;
}

function resetPassword() {
    userId = $("#userIdInput").val();
    window.location.assign("/profile/passwordreset/" + userId);
}
