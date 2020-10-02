

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


    function resetNewCategory() {
        newCategory = false;
    }

    function resetNewProduct() {
        newProduct = false;
    }
}