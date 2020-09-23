function submitChangePin(event) {
    event.preventDefault();
    $("#hintBox").hide();
    if ($("#newPin").val() == $("#currentPin").val()) {
        $("#hintBox").html("New and old pin may not be the same");
        $("#hintBox").show();
        return;
    }
    if ($("#newPin").val() != $("#newPin2").val()) {
        $("#hintBox").html("Pin do not match");
        $("#hintBox").show();
        return;
    }
    $("#changePinForm").submit();
}

$(document).ready(function() {
    $("#hintBox").hide();
});