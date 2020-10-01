

var newCategory;

function openModalNewCategory() {
    newCategory = true;
    $('#maintainCategoryModal').modal('show');
    $("#modalLabel").html("New Category");
}

function openModalNewProduct() {

}



//TODO this method is not correct yet
function checkIfCategoryNameExists() {
    if(newCategory === true) {
        categoryName1 = $("#categoryNameInput").val();
        $.ajax({
            type: "GET",
            url: "/user/byUsername/" + categoryName1,
            data: {
                name: categoryName1,
            },
        }).done(function getCategoryData(categoryData) {
            if (categoryData.name === categoryName1) {
                $("#categoryNameError").show();
            } else {
                $("#categoryNameError").hide();
            }
        });
    }

    function resetNewCategory() {
        newCategory = false;
    }
}