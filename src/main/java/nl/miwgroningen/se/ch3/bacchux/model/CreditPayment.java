package nl.miwgroningen.se.ch3.bacchux.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class CreditPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer creditPaymentId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customerId", referencedColumnName = "userId", nullable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private User customer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bartenderId", referencedColumnName = "userId", nullable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private User bartender;

    private String customerName;
    private Integer amount;
    private boolean paid;
    private Timestamp timestamp;

    @Column(length = 1023)
    private String orderJson;

    public CreditPayment(){};

    public CreditPayment(User customer, User bartender, Integer amount, String orderJson) {
        this.customer = customer;
        this.customerName = customer.getName();
        this.bartender = bartender;
        this.amount = amount;
        this.paid = false;
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.orderJson = orderJson;
    }

    public Integer getCreditPaymentId() {
        return creditPaymentId;
    }

    public void setCreditPaymentId(Integer creditPaymentId) {
        this.creditPaymentId = creditPaymentId;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public User getBartender() {
        return bartender;
    }

    public void setBartender(User bartender) {
        this.bartender = bartender;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public String getOrderJson() {
        return orderJson;
    }

    public void setOrderJson(String orderJson) {
        this.orderJson = orderJson;
    }
}
