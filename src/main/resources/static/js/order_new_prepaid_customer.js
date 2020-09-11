function submitUser(event) {
    // prevent form from being submitted normally
    event.preventDefault();

    // get username and check if empty
    var username = document.getElementById("username").value;
    if (username == "") {
        document.getElementById("errorBox").innerHTML = "Enter a username first!";
        return;
    }

    // $.ajax is a function from the jQuery library that makes a request and can process the response.
    $.ajax({
        type: 'POST',

        // get the url from the form's action attribute
        url: $("#prepaidForm").attr("action"),

        // get all form data
        data: $("#prepaidForm").serialize(),

        // execute a different function based on the response received
        statusCode: {
            // status OK 200: Give username back to order screen
            200: function() {
                opener.loadCustomer(username);
                close();
            },

            // status 500 ERROR: Server error such as duplicate username
            500: function() {
                document.getElementById("errorBox").innerHTML = "Unable to save user!";
            }
        },
    });
}