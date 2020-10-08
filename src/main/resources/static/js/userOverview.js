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

// edit existing user
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

// Create new user
function openModalNewUser() {
    newUser = true;
    $("#originalUsername").val("");
    $("#userForm").attr("action", "/user/add");
    $('#maintainUserModal').modal('show');
    $("#modalLabel").html("New User");
    $("#usernameInput, #password, #userIdInput, #nameInput, #credit_account").val("");
    $("#usernameError, #Prepaid-Choice-Label, #resetPassword").hide();
    $("#bartender, #barmanager, #Prepaid, #Credit").prop("checked",false);
    $("#customer").prop("checked",true);
    $("#password_pincode").hide();
    $('input[type=radio][name=roles]').change(function() {
        if (this.value === 'ROLE_CUSTOMER') {
            $("#password_pincode").hide();
            $("#password").prop('required',false);
            $("#pin").prop('required',false);
        }
        else {
            $("#password_pincode").show();
            $("#password").prop('required',true);
            $("#pin").prop('required',true);
        }
    });
}

function keyPressed(){
    var key = event.keyCode || event.charCode || event.which ;
    return key;
}

// ---- Modal Checks ---- \\
function checkIfUserNameExists() {
    var originalUsername = $("#originalUsername").val();
    username = $("#usernameInput").val();
    $.ajax({
        type: "GET",
        url: "/user/byUsername/" + username,
        data: {
            username: username,
        },
    }).done(function getUserData(userData) {
        if (userData.username === username && userData.username !== originalUsername) {
            $("#usernameError").show();
        } else {
            $("#usernameError").hide();
        }
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
