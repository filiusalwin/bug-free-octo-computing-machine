package nl.miwgroningen.se.ch3.bacchux.model;


import javax.persistence.*;
import java.util.List;

@Entity
public class Category implements Comparable<Category> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer categoryId;

    @Column(unique = true)
    private String name;

    @OneToMany (cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "category")
    private List<Product> products;

    @Override
    public int compareTo(Category category) {
        return name.toLowerCase().compareTo(category.getName().toLowerCase());
    }


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

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
