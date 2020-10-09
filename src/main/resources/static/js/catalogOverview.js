

var newCategory;
var newProduct;

$(document).ready(function() {
    $("#categoryNameError").hide();
});

function openModalNewCategory() {
    newCategory = true;
    $("#modalLabelCategory").html("New Category");
    $("#categoryNameInput, #originalCategoryName, #categoryIdInput" ).val("");
    $('#maintainCategoryModal').modal('show');
    $('#deleteCategory').hide();
}

function openModalNewProduct() {
    newProduct = true;
}

function fillOutForm(data) {
    console.log("filloutform");
    $("#categoryForm").attr("action", "/catalog/add");
    $("#modalLabelCategory").html("Edit " + data.name);
    $("#categoryNameInput, #originalCategoryName").val(data.name);
    $("#categoryIdInput").val(data.categoryId);
}

function addCategoryByCategoryName(categoryName) {

    $.ajax({
        type: "GET",
        url: "/catalog/byCategoryName/" + categoryName,
        data: {
            categoryName: categoryName,
        },
    }).done(function (data) {
        console.log(data)
        $('#maintainCategoryModal').modal('show');
        fillOutForm(data);
    }).fail(function (data) {
    });
}

//TODO this method is not correct yet
function checkIfCategoryNameExists() {
    var originalCategoryName = $("#originalCategoryName").val();
    categoryName = $("#categoryNameInput").val();
    $.ajax({
        type: "GET",
        url: "/catalog/byCategoryName/" + categoryName,
        data: {
            categoryName: categoryName,
        },
    }).done(function getCategoryData(categoryData) {
        if (categoryData.categoryName === categoryData && categoryData.categoryName !== originalCategoryName) {
            $("#categoryNameError").show();
        } else {
            $("#categoryNameError").hide();
        }
    });
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

function deleteCategory() {
    var categoryId = $("#categoryIdInput").val();
    console.log($("#categoryIdInput").val());
    window.location.href = "/catalog/delete/" + categoryId;
}

function resetNewCategory() {
    newCategory = false;
}

function resetNewProduct() {
    newProduct = false;
}