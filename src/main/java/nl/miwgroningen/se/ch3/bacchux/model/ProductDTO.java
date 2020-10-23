package nl.miwgroningen.se.ch3.bacchux.model;

public class ProductDTO {
    private int productId;
    private String name;

    public ProductDTO(Product product) {
        productId = product.getProductId();
        name = product.getName();
    }

    public int getCategoryId() {
        return productId;
    }

    public void setCategoryId(int categoryId) {
        this.productId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
