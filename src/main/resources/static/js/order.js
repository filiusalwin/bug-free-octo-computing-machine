// ---- global variables ---- \\
var currentBalance = 0;
var totalPrice = 0;


// ---- On document load --- \\
$(document).ready(function() {
    hideUserStuff();
    showPrepaidStuff(false);
    hidePayment();
    $("#paymentError, #paymentSuccess").hide();
    $("#categoryList > button:first-child").trigger("click");

    // event listeners
    $("#searchUser").change(getUserFromSearch);
    $("#searchUser").click(function() {
        hideUserStuff();
    });
});


// ---- Show and Hide --- \\
function showPrepaidStuff(hasPrepaid) {
    if (hasPrepaid) {
        $("#topup, #payPrepaidButton").show();
        $("#cashPayButton").hide();
    } else {
       $("#topup, #payPrepaidButton").hide();
       $("#cashPayButton").show();
    }
}

function hideUserStuff() {
    $("#searchUser").val("");
    $("#addPrepaidButton").show();
    showPrepaidStuff(false);
}

function showUserStuff() {
    $("#addPrepaidButton").hide();
}

function showPayment() {
    $("#payment").show();
}

function hidePayment() {
    $("#payment").hide();
}


// ---- User Selection --- \\
function getUserFromSearch() {
    var username = $("#searchUser").val();
    getCustomerByUsernameAnd(username, chooseCustomer);
}

function chooseCustomer(data) {
    if ($.isEmptyObject(data)) {
        hideUserStuff();
        return;
    }
    currentBalance = data.balance;
    updateCurrentBalance();
    showUserStuff();
    showPrepaidStuff(data.prepaidAllowed);
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
    var productsInBill = [];

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
function doCashPayment() {
    $("#paymentError").hide();
    var price = document.getElementById("totalPrice").innerHTML;
    if (confirm("The total is " + price + ".")) {
        successAndReload();
    }
}

function doPrepaidPayment() {
    $("#paymentError").hide();
    $.ajax({
        type: "POST",
        url: "/payment/prepaid/",
        data: {
            username: $("#searchUser").val(),
            amount: totalPrice,
        },
        success: successAndReload,
        error: function(jqXHR, textStatus, errorThrown) {
            $("#paymentError").text("Payment error: " + jqXHR.responseText);
            $("#paymentError").show();
        }
    });
}

function successAndReload() {
    $("#paymentError").hide();
    $("#paymentSuccess").show();
    setTimeout(function() {location.reload();}, 1000);
}


// ---- New Customer ---- \\
function addPrepaidCustomer() {
    window.open("/order/new/prepaid", "popUpWindow",
         'height=500, width=600, left=50, top=50, resizable=yes, scrollbars=yes, toolbar=yes, menubar=no, location=no, directories=no, status=yes');
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