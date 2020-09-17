package nl.miwgroningen.se.ch3.bacchux.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
public class Product {

    private static final int CENTS_PER_EURO = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String productId;

    private String name;

    private int price;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categoryId", referencedColumnName = "categoryId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Category category;

    public String euroPrice(){
        double priceInEuro;
        priceInEuro =  (double) price / CENTS_PER_EURO;
        return String.format("%.2f €", priceInEuro);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
