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
    var products = productList.getElementsByTagName("li");

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

    console.log(productNames);
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