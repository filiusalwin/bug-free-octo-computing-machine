package nl.miwgroningen.se.ch3.bacchux.model;


import javax.persistence.*;
import java.util.List;

@Entity
@Access(AccessType.PROPERTY)
public class Category {

    private Integer categoryId;

    private String name;

    private List<Product> products;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @OneToMany (cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "category")
    public List<Product> getProduct() {
        return products;
    }

    public void setProduct(List<Product> product) {
        this.products = product;
    }
}
