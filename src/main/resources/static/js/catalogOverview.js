var newCategory;
var newProduct;

$(document).ready(function() {
    $("#categorySaveErrorFrontEnd").hide();
    $("#productNameError").hide();
    $("#priceError").hide();
    categoryId = $("#categoryId").text();
    selectCategory(categoryId);
    setTimeout(function () {
        $("#categorySaveError, #productSaveError").alert('close');
    }, 5000);
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
    $("#categoryForm").attr("action", "/catalog/add");
    $("#modalLabelCategory").html("Edit " + data.name);
    categoryName = data.name
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
    categoryNameAfterTyping = $("#categoryNameInput").val();
    if (categoryName == categoryNameAfterTyping ) {
        $("#categorySaveErrorFrontEnd").hide();
        return;
    }
    $.ajax({
        type: "GET",
        url: "/catalog/byCategoryName/" + categoryName,
        statusCode: {
            404: function () {
                return;
            }
        }
    }).done(function (data) {
        console.log(data);
        if (data !== "") {
            $("#categorySaveErrorFrontEnd").show();
            return;
        }
        console.log("test");
        $("#categorySaveErrorFrontEnd").hide();
    });
}

function selectCategory(id) {
    var highlightClass = "list-group-item-dark"
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
    console.log($("#categoryIdInput").val());
    window.location.href = "/catalog/delete/" + categoryId;
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