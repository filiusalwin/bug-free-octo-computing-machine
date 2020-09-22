function submitChangePassword(event) {
    event.preventDefault();
    $("#hintBox").hide();
    if ($("#newPassword").val() != $("#newPassword2").val()) {
        $("#hintBox").html("Passwords do not match!");
        $("#hintBox").show();
        return;
    }
    $("#changePasswordForm").submit();
}

$(document).ready(function() {
    $("#hintBox").hide();
});