// ---- global variables ---- \\
var currentBalance = 0;
var currentCredit = 0;
var totalPrice = 0;
var productsInBill;
var paymentType = null;


// ---- On document load --- \\
$(document).ready(function() {
    showPaymentStuff(false, false);
    hidePayment();
    paymentError();
    paymentSuccess();
    savingCustomerSuccess();
    savingCustomerError();
    $("#usernameError, #profileFoto, #chooseUser").hide();
    $("#categoryList > button:first-child").trigger("click");

    // event listeners
    $("#searchUser").change(getUserFromSearch);
    $("#searchUser").click(function() {
        clearUser();
    });

    resetPicture();
});


// ---- Show and Hide --- \\
function showPaymentStuff(hasPrepaid, hasCredit) {
    $("#prepaid, #payPrepaidButton, #credit, #payCreditButton").hide();
    if (hasPrepaid) {
        $("#prepaid, #payPrepaidButton").show();
    }
    if (hasCredit) {
        $("#credit, #payCreditButton").show();
    }
}

function clearUser() {
    $("#noPaymentError").hide();
    $("#searchUser").val("");
    showCustomerInfo();
    showPaymentStuff(false, false);
    resetPicture();
    $("#profileFoto,#chooseUser").hide();
}

function resetPicture(){
    $("#profileFoto").attr('src','/images/defaultPicture.png');
    $("#profileFoto2").attr('src','/images/defaultPicture.png');
}

function showPayment() {
    $(".payment").attr("disabled", false);
}

function hidePayment() {
    $(".payment").attr("disabled", true);
}

function disablePayments(value) {
    $("#confirmPayButton").attr("disabled", value);
}


// ---- User Selection --- \\
function getUserFromSearch() {
    var username = $("#searchUser").val();
    getCustomerByUsernameAnd(username, chooseCustomer);
}

function chooseCustomer(data) {
    $("#noPaymentError").hide();
    paymentError();
    if ($.isEmptyObject(data)) {
        clearUser();
        return;
    }
    currentBalance = data.balance;
    currentCredit = data.currentCredit;
    showCustomerInfo(data);
    updateCurrentBalance();
    if (!data.prepaidAllowed && !data.creditAllowed) {
        $("#noPaymentError").show();
    }
    showPaymentStuff(data.prepaidAllowed, data.creditAllowed);
    $("#chooseUser").show();
    if (data.username == null) {
        $("#profileFoto").attr('src','/images/defaultPicture.png');
    }
    showPicture();

}

function showCustomerInfo(data) {
    if (!data) {
        $("#customerName, #customerInfo").text(null);
        return;
    }
    $("#customerName").text(data.name);
    showBalanceAndCredit();
    $("#profileFoto").show();
    $("#profileFoto").attr('src','data:image/png;base64,' + data.picture);
    $("#profileFoto2").attr('src','data:image/png;base64,' + data.picture);

}

function showBalanceAndCredit() {
    var info = "Balance "
            + formatCurrencyString(currentBalance)
            + "<br>Credit "
            + formatCurrencyString(currentCredit);
    $("#customerInfo").html(info);
}

// Change picture when new user is chosen
function showPicture(){
    $("#searchUser").on("change", function() {
        $("#profileFoto").attr('src','data:image/png;base64,' + data.picture);
        $("#profileFoto2").attr('src','data:image/png;base64,' + data.picture);
    });
}

function chooseThisUser() {
    $('.modal').modal('hide');
}

function getCustomerByUsernameAnd(username, callback) {
    $.ajax({
        type: "GET",
        url: "/user/username/" + username,
        statusCode: {
            404: function() {return;}
        }
    }).done(function(data) {
        callback(data);
    });
}


// ---- Update Bill ---- \\
function updateProductList(products) {
    choosePayment();
    var productBox = document.getElementById("billProductList");
    productBox.innerHTML = "";

    for (product of products) {
        var newitem = document.createElement("li");
        newitem.classList.add("list-group-item", "list-group-item-action", "d-flex");

        var amount = document.createElement("div");
        amount.innerHTML = product.amount;
        amount.style.width = "30px";
        newitem.append(amount);

        var name = document.createElement("div");
        name.innerHTML = product.name;
        newitem.append(name);

        var subtotal = document.createElement("div");
        subtotal.innerHTML = formatter.format(product.subtotal / 100);
        subtotal.classList.add("ml-auto");
        newitem.append(subtotal);

        // function factory to prevent closure issues
        function removeFunctionMaker(productId) {
            return function() {removeProduct(productId)};
        }
        newitem.addEventListener("click", removeFunctionMaker(product.id));

        productBox.append(newitem);
    }
}

function updateTotalPrice(priceInCents) {
    var formatted = formatter.format(priceInCents / 100);
    document.getElementById("totalPrice").innerHTML = formatted;
}

function updateBill() {
    var products = document.querySelectorAll("#productList > .productListItem");

    totalPrice = 0;
    productsInBill = [];

    for (let product of products) {
        var countBox = product.getElementsByClassName("productCountBox")[0];
        var count = parseInt(countBox.value);

        var productName = product.getAttribute("productName");
        var price = parseInt(product.getAttribute("productPrice"));

        var productId = parseInt(product.getAttribute("productId"));

        totalPrice += count * price;
        if (count != 0) {
            productsInBill.push({
                id:       productId,
                amount:   count,
                name:     productName,
                price:    price,
                subtotal: price * count
            });
        }
    }

    updateTotalPrice(totalPrice);
    updateProductList(productsInBill);

    if (totalPrice == 0) {
        hidePayment();
    } else {
        showPayment();
    }
}


// ---- Product Selection ---- \\
function addProduct(id) {
    var field = document.getElementById("productCount" + id);
    field.value = parseInt(field.value) + 1;

    updateBill();
}

function removeProduct(id) {
    var field = document.getElementById("productCount" + id);
    var value = field.value;
    if (value > 0) {
        field.value = parseInt(field.value) - 1;
    }

    updateBill();
}

function selectCategory(id) {
    var highlightClass = "list-group-item-dark"

    // unhighlight all buttons
    $(".categoryButton").removeClass(highlightClass);

    // highlight button
    $("#category" + id).addClass(highlightClass);

    // get all product tags in the productList
    var productList = document.getElementById("productList");
    var products = productList.getElementsByClassName("productListItem");

    // show or hide each element.
    for (let product of products) {
        if (product.getAttribute("category") == id) {
            product.style.display = "block";
            product.classList.add("list-group-item", "d-flex");
        } else {
            product.style.display = "none";
            product.classList.remove("list-group-item", "d-flex");
        }
    }
}


// ---- Payment ---- \\
function choosePayment(type) {
    paymentType = type;
    if (!type) {
        $("#confirmPayButton").hide();
        return;
    }
    paymentError();
    $("#confirmPayButton").show();
}

function confirmPayment() {
    switch (paymentType) {
        case "direct":
            doCashPayment();
            break;
        case "prepaid":
            doPrepaidPayment();
            break;
        case "credit":
            doCreditPayment();
            break;
    }
}

function paymentError(message) {
    var error = $("#paymentError");
    if (!message) {
        error.hide();
        return;
    }
    choosePayment();
    error.text(message);
    error.show();
}

function paymentSuccess(message) {
    var success = $("#paymentSuccess");
    if (!message) {
        success.hide();
        return;
    }
    choosePayment();
    success.text(message);
    success.show();
}

function reloadAfter(duration) {
    setTimeout(_ => {location.reload();}, duration);
}

function doCashPayment() {
    disablePayments(true);
    doPaymentLog(paymentType = "direct/cash");
    paymentError();
    var price = document.getElementById("totalPrice").innerHTML;
    paymentSuccess("Payment successful. Price: " + price);
    reloadAfter(3000);
}

function doPrepaidPayment() {
    disablePayments(true);
    paymentError();
    $.ajax({
        type: "POST",
        url: "/payment/prepaid/",
        data: {
            username: $("#searchUser").val(),
            amount: totalPrice,
        },
        success: prepaidSuccessAndReload,
        error: function(jqXHR, textStatus, errorThrown) {
            paymentError("Payment error: " + jqXHR.responseText);
            disablePayments(false);
        }
    });
}

function doCreditPayment() {
    disablePayments(true);
    paymentError();
    $.ajax({
        type: "POST",
        url: "/payment/credit/",
        data: {
            username: $("#searchUser").val(),
            amount: totalPrice,
            order: JSON.stringify(productsInBill),
        },
        success: creditSuccessAndReload,
        error: function(jqXHR, textStatus, errorThrown) {
            paymentError("Payment error: " + jqXHR.responseText);
            disablePayments(false);
        }
    });
}

function prepaidSuccessAndReload() {
    paymentError();
    currentBalance -= totalPrice;
    updateCurrentBalance();
    showBalanceAndCredit();
    doPaymentLog(paymentType = "prepaid");
    const message = "Payment successful. Remaining balance: " + formatCurrencyString(currentBalance);
    paymentSuccess(message);
    reloadAfter(3000);
}

function creditSuccessAndReload() {
    paymentError();
    currentCredit += totalPrice;
    showBalanceAndCredit();
    doPaymentLog(paymentType = "credit");
    const message = "Payment successful. Credit total: " + formatCurrencyString(currentCredit);
    paymentSuccess(message);
    reloadAfter(3000);
}


// ---- Direct Payment Logging ---- \\
function doPaymentLog(paymentType) {
    $("#paymentError").hide();
    $.ajax({
        type: "POST",
        url: "/log/directPayment/",
        data: {
            totalAmount: totalPrice,
            customer: $("#searchUser").val(),
            paymentType: paymentType,
            paymentDetails: JSON.stringify(productsInBill)
        },
        error: function(jqXHR, textStatus, errorThrown) {
            $("#paymentError").text("Logging error: " + jqXHR.responseText);
            $("#paymentError").show();
        }
    });
}


// ---- New Customer ---- \\
function addPrepaidCustomer() {
    $("#usernameInput, #nameInput").val("");
    $("#newCustomerModal").modal('show');
}

function saveNewCustomer() {
    if ($("#usernameInput").val() === "" || $("#nameInput").val() === "") {
        $("#emptyfieldsError").html("Both fields are required.");
        $("#emptyfieldsError").show();
        return;
    }
    $.ajax({
        type: "PUT",
        url: "/order/newCustomer/",
        data: {
            username: $("#usernameInput").val(),
            name: $("#nameInput").val(),
            prepaidOn: $("#prepaidNewCustomer").prop("checked"),
        },
        success: savingUserSuccess,
        error: function(jqXHR, textStatus, errorThrown) {
            savingCustomerError("User not saved, user already exists. " + jqXHR.responseText);
            disablePayments(false);
        }
    });
}

function savingCustomerSuccess(message) {
    var success = $("#savingUserSucces");
    if (!message) {

        success.hide();
        return;
    }
    $("#savingUserSucces").html(message);
    $("#savingUserSucces").show();
    setTimeout(function () {success.hide()}, 3000)
}

function savingCustomerError(message) {
    var error = $("#savingUserError");
    if (!message) {
        error.hide();
        return;
    }
    error.text(message);
    error.show();
    $("#newCustomerModal").modal('hide');
    setTimeout(function () {error.hide()}, 3000)
}

function savingUserSuccess(){
    savingCustomerError();
    const message = "User " + $("#usernameInput").val() + " successfully added."
    $("#newCustomerModal").modal('hide');
    savingCustomerSuccess(message);
    loadCustomer($("#usernameInput").val(), $("#nameInput").val());

}

function checkIfUserNameExists() {
    username = $("#usernameInput").val();
    $.ajax({
        type: "GET",
        url: "/order/username/" + username,
        statusCode: {
            404: function() {return;}
        }
    }).done(function(data) {
        if (data !== "") {
            $("#usernameError").show();
            return;
        }
        $("#usernameError").hide();
    });
}

// to update the search list when a new user is added, the new added user is automatically selected
function loadCustomer(username, fullname) {
    var newOpt = document.createElement("option");
    newOpt.value = username;
    newOpt.innerHTML = fullname;
    $("#userList").append(newOpt);

    $("#searchUser").val(username);
    getUserFromSearch();
}

// --- Prepaid --- \\
function updateCurrentBalance() {
    $("#currentBalance").text("Balance: " + formatCurrencyString(currentBalance));
}

const formatter = new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'EUR',
    minimumFractionDigits: 2
})

function doTopUp() {
    var username = $("#searchUser").val();
    var amount = Number($("#topUpAmount").val().replace(/[^0-9]+/g, ""));
    if (isNaN(amount)) {
        alert("Invalid amount. Top-up not possible.");
    }

    $.ajax({
        type: "PUT",
        url: "/user/topup",
        data: {
            username: username,
            amount: amount,
        },
        statusCode: {
            200: function() {
                currentBalance += amount;
                updateCurrentBalance();
                showBalanceAndCredit();
                $("#topUpAmount").val("");
            },
            404: function() {
                alert("User not found");
            }
        }
    });
}