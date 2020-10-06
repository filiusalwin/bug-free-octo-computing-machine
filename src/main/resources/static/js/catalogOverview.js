

var newCategory;
var newProduct;

function openModalNewCategory() {
    newCategory = true;
    $('#maintainCategoryModal').modal('show');
    $("#modalLabel").html("New Category");
}

function openModalNewProduct() {
    newProduct = true;
}

function fillOutForm(data) {
    $("#userForm").attr("action", "/catalog/add");
    $("#modalLabel").html("Edit " + data.categoryName);
    $("#categoryNameInput, #originalCategoryName").val(data.categoryName);
}

//TODO this method is not correct yet
function addCategoryByCategoryName(categoryName) {
    $.ajax({
        type: "GET",
        url: "/catalog/byCategoryName/" + categoryName,
        data: {
            categoryName: categoryName,
        },
    }).done(function (data) {
        $('#maintainCategoryModal').show();
        fillOutForm(data);
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

function resetNewCategory() {
    newCategory = false;
}

function resetNewProduct() {
    newProduct = false;
}