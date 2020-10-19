package nl.miwgroningen.se.ch3.bacchux.model;

import nl.miwgroningen.se.ch3.bacchux.utils.CurrencyFormatter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
public class Product {

    private static final int CENTS_PER_EURO = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer productId;

    @Column(unique = true)
    private String name;

    private int price;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categoryId", referencedColumnName = "categoryId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Category category;

    public String euroPrice(){
        return CurrencyFormatter.formatCurrency(price);
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

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
