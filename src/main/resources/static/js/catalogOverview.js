var newCategory;
var newProduct;
var categoryId;
const priceToEuro = 100;

$(document).ready(function() {
    $("#categorySaveErrorFrontEnd, #productSaveErrorFrontEnd").hide();
    $("#productNameError").hide();
    $("#priceError").hide();
    categoryId = $("#categoryId").text();
    selectCategory(categoryId);
    setTimeout(function () {
        $("#categorySaveError, #categorySaveSuccess #productSaveError,#productSaveSuccess" ).alert('close');
    }, 5000);
});

function openModalNewCategory() {
    newCategory = true;
    $("#modalLabelCategory").html("New Category");
    $("#categoryNameInput, #originalCategoryName, #categoryIdInput" ).val("");
    categoryName = null;
    $('#maintainCategoryModal').modal('show');
    $('#deleteCategory').hide();
}

function openModalNewProduct() {
    newProduct = true;
    $("#modalLabelProduct").html("New product");
    $("#productNameInput,#productPriceInput, #productId").val("");
    productName = null;
    $("#productCategoryIdInput").val(categoryId);
    $("#deleteProduct").hide();
    $("#productForm").attr("action", "/catalog/product/" + categoryId + "/add");
}

function editProduct(id) {
    product = $('#product' + id);
    var data = {
        name : product.attr("productName"),
        productId : product.attr("productId"),
        price: product.attr("productPrice"),
    }
    $('#maintainProductModal').modal('show');
    fillOutProductModal(data);
}

function fillOutForm(data) {
    $("#categoryForm").attr("action", "/catalog/add");
    $("#modalLabelCategory").html("Edit " + data.name);
    categoryName = data.name
    $("#categoryNameInput, #originalCategoryName").val(data.name);
    $("#categoryId").val(data.categoryId);
    $('#deleteCategory').show();
}

function fillOutProductModal(data) {
    $("#modalLabelProduct").html("Edit " + data.name);
    $("#productNameInput, #originalProductName").val(data.name);
    productName= data.name;
    $("#productPriceInput").val(data.price / 100).trigger("blur");
    $("#productIdInput").val(data.productId);
    $("#productCategoryIdInput").val(categoryId);
    $("#deleteProduct").show();
}

function addCategoryByCategoryName(categoryName) {
    $.ajax({
        type: "GET",
        url: "/catalog/byCategoryName/" + categoryName,
        data: {
            categoryName: categoryName,
        },
    }).done(function (data) {
        $('#maintainCategoryModal').modal('show');
        fillOutForm(data);
    }).fail(function (data) {
    });
}

function checkIfCategoryNameExists() {
    categoryNameAfterTyping = $("#categoryNameInput").val();
    if (categoryName == categoryNameAfterTyping) {
        $("#categorySaveErrorFrontEnd").hide();
        return;
    }
    $.ajax({
        type: "GET",
        url: "/catalog/byCategoryName/" + categoryNameAfterTyping,
        statusCode: {
            404: function () {
                return;
            }
        }
    }).done(function (data) {
        if (data !== "") {
            $("#categorySaveErrorFrontEnd").show();
            return;
        }
        $("#categorySaveErrorFrontEnd").hide();
    });
}

function checkIfProductNameExists() {
    productNameAfterTyping = $("#productNameInput").val();
    if (productName == productNameAfterTyping) {
            $("#categorySaveErrorFrontEnd").hide();
            return;
        }
    $.ajax({
        type: "GET",
        url: "/catalog/productName/" + productNameAfterTyping,
        statusCode: {
            404: function () {
                return;
            }
        }
    }).done(function (data) {
        if (data !== "") {
            $("#productSaveErrorFrontEnd").show();
            return;
        }
        $("#productSaveErrorFrontEnd").hide();
    });
}

function selectCategory(id) {
    var highlightClass = "list-group-item-dark"
    categoryId = id;
    $("#productForm").attr("action", "/catalog/product/" + id + "/add");
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

function deleteCategory() {
    var categoryId = $("#categoryIdInput").val();
    window.location.href = "/catalog/delete/" + categoryId;
}

function deleteProduct() {
    var productId = $("#productIdInput").val();
    window.location.href = "/catalog/product/delete/" + productId;
}

function resetNewCategory() {
    newCategory = false;
}

function resetNewProduct() {
    newProduct = false;
}

function fixPrice() {
    var amount = Number($("#productPriceInput").val().replace(/[^0-9]+/g, ""));
    $("#price").val(amount);
}

$("#productForm").submit(function(event) {
    var form = this;
    $("#productPriceInput").trigger("blur");

    event.preventDefault();

    setTimeout( function () {
        fixPrice();
        form.submit();
    }, 300);
});