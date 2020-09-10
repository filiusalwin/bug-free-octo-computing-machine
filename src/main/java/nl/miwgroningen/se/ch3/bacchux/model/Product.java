package nl.miwgroningen.se.ch3.bacchux.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String productId;
    private String name;
    private int price;


    public String euroPrice(){
        double priceInEuro;
        priceInEuro = price/100.0;
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

    public void setProductId(String id) {
        this.productId = id;
    }

    @Id
    public String getProductId() {
        return productId;
    }
}
