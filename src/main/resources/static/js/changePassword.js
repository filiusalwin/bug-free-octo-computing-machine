function submitChangePassword(event) {
    event.preventDefault();
    $("#hintBox").hide();
    if ($("#newPassword").val() == $("#currentPassword").val()) {
        $("#hintBox").html("New and old password may not be the same");
        $("#hintBox").show();
        return;
    }
    if ($("#newPassword").val() != $("#newPassword2").val()) {
        $("#hintBox").html("Passwords do not match");
        $("#hintBox").show();
        return;
    }
    $("#changePasswordForm").submit();
}

$(document).ready(function() {
    $("#hintBox").hide();

});

function submitResetPassword(event) {
    event.preventDefault();
    $("#hintBox").hide();
    if ($("#newPassword").val() === "" || $("#newPassword2").val() === "") {
        $("#hintBox").html("Please fill out both fields");
        $("#hintBox").show();
        return;
    }
    if ($("#newPassword").val() != $("#newPassword2").val()) {
        $("#hintBox").html("Passwords do not match");
        $("#hintBox").show();
        return;
    }
    $("#resetPasswordForm").submit();
}


