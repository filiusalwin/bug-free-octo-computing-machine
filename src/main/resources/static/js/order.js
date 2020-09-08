function calculateTotalPrice() {
    var productList = document.getElementById("productList");
    var products = productList.getElementsByTagName("li");

    for (let product of products) {
        var countBox = product.getElementsByClassName("productCountBox")[0];
        var count = parseInt(countBox.value);

        // TODO
    }
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