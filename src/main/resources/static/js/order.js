// Run once DOM is loaded
$(document).ready(function() {
    $("#payment").hide();
    $("#customer").hide();
    $("#categoryList > button:first-child").trigger("click");

    $(document).on('change', 'input', function(){
        getUserFromSearch();
    });

    $("#searchUser").click(function() {
        this.value = "";
    });
});

// get user from searchUser input
function getUserFromSearch() {
    var options = $('#userList')[0].options;
    for (var i=0; i < options.length; i++){
       if (options[i].value == $("#searchUser").val()) {
           $("#customer").show();
           $("#ShowInfoUser").html(options[i].label);
           return;
       }
    }
    // if not matching any username
    $("#searchUser").val("");
    $("#customer").hide();
}

// formats number as currency
const formatter = new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'EUR',
    minimumFractionDigits: 2
})

// helper for updateBill()
function updateProductList(products) {
    var productBox = document.getElementById("billProductList");
    productBox.innerHTML = "";

    for (product of products) {
        var newitem = document.createElement("li");
        newitem.classList.add("list-group-item");
        newitem.classList.add("list-group-item-action");
        newitem.classList.add("d-flex");

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

        newitem.addEventListener("click", removeFunctionMaker(product.id));

        productBox.append(newitem);
    }
}

// function factory to prevent closure issues
function removeFunctionMaker(productId) {
    return function() {removeProduct(productId)};
}

// helper for updateBill()
function updateTotalPrice(priceInCents) {

    // format number as currency
    var formatted = formatter.format(priceInCents / 100);

    // put formatted string in <span id="totalPrice"> tag
    document.getElementById("totalPrice").innerHTML = formatted;
}

// updates the displayed Bill with items, counts and total Price
function updateBill() {
    // get all tags in the productList
    var productList = document.getElementById("productList");
    var products = productList.getElementsByClassName("productListItem");

    // prepare totalprice, productnames and counts
    var total = 0;
    var productsInBill = [];

    // go through each list tag
    for (let product of products) {
        // Get the value of the <input type="hidden" /> tag for each product
        var countBox = product.getElementsByClassName("productCountBox")[0];
        var count = parseInt(countBox.value);

        // get product name and price, added as attributes via thymeleaf
        var productName = product.getAttribute("productName");
        var price = parseInt(product.getAttribute("productPrice"));

        var productId = parseInt(product.getAttribute("productId"));

        // add to total, and if more than 0, add to list of products and counts
        total += count * price;
        if (count != 0) {
            productsInBill.push({
                id: productId,
                amount: count,
                name: productName,
                subtotal: price * count
            });
        }
    }

    updateTotalPrice(total);
    updateProductList(productsInBill);

    if (total == 0) {
        $("#payment").hide();
    } else {
        $("#payment").show();
    }
}

// increase product count by 1
function addProduct(id) {
    var field = document.getElementById("productCount" + id);
    field.value = parseInt(field.value) + 1;

    updateBill();
}

// decrease product count by 1
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

// Perform direct payment
function doCashPayment() {
    var price = document.getElementById("totalPrice").innerHTML;
    if (confirm("The total is " + price + ".")) {
        location.reload();
    }
}

// Open popup window for new prepaid customer
function addPrepaidCustomer() {
    window.open("/order/new/prepaid", "popUpWindow",
         'height=500, width=600, left=50, top=50, resizable=yes, scrollbars=yes, toolbar=yes, menubar=no, location=no, directories=no, status=yes');
}

// get customer from popup window
function loadCustomer(username, fullname) {
    var newOpt = document.createElement("option");
    newOpt.value = username;
    newOpt.innerHTML = fullname;
    $("#userList").append(newOpt);

    $("#searchUser").val(username);
    getUserFromSearch();
}

// show balance etc for selected user
function showInfoSelectedUser() {
    var username = $("#searchUser").val();
    if (username == "") {
        document.getElementById("errorBox").innerHTML = "Select a user first!";
        return;
    }
    var url2 = "/order/user/info/" + username;
    window.open(url2, "popUpWindow",
        'height=500, width=600, left=50, top=50, resizable=yes, scrollbars=yes, toolbar=yes, menubar=no, location=no, directories=no, status=yes');
}

// perform topup of user
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
                alert("Topup complete.");
            },
            440: function() {
                alert("User not found");
            }
        }
    });
}