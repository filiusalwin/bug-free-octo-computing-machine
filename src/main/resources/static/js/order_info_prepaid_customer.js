$(document).ready(function () {
    var balance = $("#Prepaid-Choice").val();
    var string = "This user has no balance in his/her account";
    console.log(balance);
    if (balance === "") {
        $("#Prepaid-Choice").hide();
        $("#Prepaid-Choice-Label").hide();
        document.getElementById("no_balance").innerHTML = string;
    }

});

function closeScreen() {
    close();
}


