// formats number as currency
const formatter = new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'EUR',
    minimumFractionDigits: 2
})

// helper for updateBill()
function updateProductList(names, counts, subtotals) {
    var productBox = document.getElementById("billProductList");
    productBox.innerHTML = "";

    for (var i = 0; i < names.length; i++) {
        productBox.innerHTML += counts[i] + " "
                                + names[i] + " "
                                + formatter.format(subtotals[i] / 100) + "<br>"
    }
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
    // get all <li> tags in the productList
    var productList = document.getElementById("productList");
    var products = productList.getElementsByClassName("productListItem");

    // prepare totalprice, productnames and counts
    var total = 0;
    var productNames = [];
    var productCounts = [];
    var subtotals = [];

    // go through each <li> tag
    for (let productLi of products) {
        // Get the value of the <input type="hidden" /> tag in each <li>
        var countBox = productLi.getElementsByClassName("productCountBox")[0];
        var count = parseInt(countBox.value);

        // get product name and price, added as attributes via thymeleaf
        var productName = productLi.getAttribute("productName");
        var price = parseInt(productLi.getAttribute("productPrice"));

        // add to total, and if more than 0, add to list of products and counts
        total += count * price;
        if (count != 0) {
            productCounts.push(count);
            productNames.push(productName);
            subtotals.push(price * count);
        }
    }

    updateTotalPrice(total);
    updateProductList(productNames, productCounts, subtotals);

    if (total == 0) {
        document.getElementById("cashPayButton").style.display = "none";
        document.getElementById("addPrepaidButton").style.display = "none";
    } else {
        document.getElementById("cashPayButton").style.display = "inline";
        document.getElementById("addPrepaidButton").style.display = "inline";
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
    // get all <li> tags in the productList
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
function loadCustomer(username) {
    alert("Customer " + username + " chosen. The new customer has been added. Prepaid payment option will be added in a future release.");
}