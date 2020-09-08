function updateTotalPrice(priceInCents) {
    const formatter = new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'EUR',
        minimumFractionDigits: 2
    })
    var formatted = formatter.format(priceInCents / 100);
    document.getElementById("totalPrice").innerHTML = formatted;
}

function calculateTotalPrice() {
    var productList = document.getElementById("productList");
    var products = productList.getElementsByTagName("li");

    var total = 0;

    for (let productLi of products) {
        var countBox = productLi.getElementsByClassName("productCountBox")[0];
        var count = parseInt(countBox.value);

        var price = parseInt(productLi.getAttribute("productPrice"));

        total += count * price;
    }

    updateTotalPrice(total);
}

function addProduct(id) {
    var field = document.getElementById("productCount" + id);
    field.value = parseInt(field.value) + 1;

    calculateTotalPrice();
}

function removeProduct(id) {
    var field = document.getElementById("productCount" + id);
    var value = field.value;
    if (value > 0) {
        field.value = parseInt(field.value) - 1;
    }

    calculateTotalPrice();
}