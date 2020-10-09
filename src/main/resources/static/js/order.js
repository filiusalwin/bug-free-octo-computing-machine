// ---- global variables ---- \\
var currentBalance = 0;
var currentCredit = 0;
var totalPrice = 0;
var productsInBill;


// ---- On document load --- \\
$(document).ready(function() {
    showPaymentStuff(false, false);
    hidePayment();
    paymentError();
    paymentSuccess();
    savingCustomerSuccess();
    savingCustomerError();
    $("#categoryList > button:first-child").trigger("click");

    // event listeners
    $("#searchUser").change(getUserFromSearch);
    $("#searchUser").click(function() {
        clearUser();
    });
});


// ---- Show and Hide --- \\
function showPaymentStuff(hasPrepaid, hasCredit) {
    $("#prepaid, #credit").hide();
    if (hasPrepaid) {
        $("#prepaid").show();
    }
    if (hasCredit) {
        $("#credit").show();
    }
}

function clearUser() {
    $("#searchUser").val("");
    showPaymentStuff(false, false);
}

function showPayment() {
    $(".payment").show();
}

function hidePayment() {
    $(".payment").hide();
}

function disablePayments(value) {
    $("#payCreditButton, #payPrepaidButton, #cashPayButton").attr("disabled", value);
}


// ---- User Selection --- \\
function getUserFromSearch() {
    var username = $("#searchUser").val();
    getCustomerByUsernameAnd(username, chooseCustomer);
}

function chooseCustomer(data) {
    paymentError();
    if ($.isEmptyObject(data)) {
        clearUser();
        return;
    }
    currentBalance = data.balance;
    currentCredit = data.currentCredit;
    updateCurrentBalance();
    updateCurrentCredit();
    if (!data.prepaidAllowed && !data.creditAllowed) {
        paymentError("This user has no payment privileges.");
    }
    showPaymentStuff(data.prepaidAllowed, data.creditAllowed);
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
function paymentError(message) {
    var error = $("#paymentError");
    if (!message) {
        error.hide();
        return;
    }
    error.text(message);
    error.show();
}

function paymentSuccess(message) {
    var success = $("#paymentSuccess");
    if (!message) {
        success.hide();
        return;
    }
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
    disablePayments(false);

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
    doPaymentLog(paymentType = "prepaid");
    const message = "Payment successful. Remaining balance: " + formatCurrencyString(currentBalance);
    paymentSuccess(message);
    reloadAfter(3000);
}

function creditSuccessAndReload() {
    paymentError();
    currentCredit += totalPrice;
    updateCurrentCredit();
    doPaymentLog(paymentType = "credit");
    const message = "Payment successful. Credit total: " + formatCurrencyString(currentCredit);
    paymentSuccess(message);
    reloadAfter(3000);
}


// ---- New Customer ---- \\
function addPrepaidCustomer() {
    $("#usernameInput, #nameInput").val("");
    $("#newCustomerModal").modal('show');
}

function saveNewCustomer() {
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
            savingCustomerError("Adding a user error: " + jqXHR.responseText);
            disablePayments(false);
        }
    });
}


function savingCustomerSuccess(message) {
    console.log(!message);
    console.log(message);
        var success = $("#savingUserSuccess");
    if (!message) {

        success.hide();
        return;
    }
    success.text(message);
    success.show();
    setTimeout(success.hide(), 3000)
}
function savingCustomerError(message) {
    var error = $("#savingUserError");
    if (!message) {
        error.hide();
        return;
    }
    error.text(message);
    error.show();
    setTimeout(error.hide(), 3000)
}

function savingUserSuccess(){
    savingCustomerError();
    const message = "User " + $("#usernameInput").val() + " successfully added."
    savingCustomerSuccess(message);
    $("#newCustomerModal").modal('hide');
}

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
                $("#topUpAmount").val("");
            },
            404: function() {
                alert("User not found");
            }
        }
    });
}

// ---- Credit ---- \\
function updateCurrentCredit() {
    var message = "Current credit total: " + formatCurrencyString(currentCredit);
    $("#currentCreditText").text(message);
}
