package nl.miwgroningen.se.ch3.bacchux.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Product {

    private static final int CENTS_PER_EURO = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String productId;

    private String name;

    private int price;

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

    @Id
    public String getProductId() {
        return productId;
    }
}
