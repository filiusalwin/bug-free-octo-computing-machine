package nl.miwgroningen.se.ch3.bacchux.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
public class CreditPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer creditPaymentId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userId", referencedColumnName = "userId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    private Integer amount;
    private boolean paid;
    private String orderJson;

    public CreditPayment(){};

    public CreditPayment(User user, Integer amount, boolean paid, String orderJson) {
        this.user = user;
        this.amount = amount;
        this.paid = paid;
        this.orderJson = orderJson;
    }

    public CreditPayment(Integer creditPaymentId, User user, Integer amount, boolean paid, String orderJson) {
        this.creditPaymentId = creditPaymentId;
        this.user = user;
        this.amount = amount;
        this.paid = paid;
        this.orderJson = orderJson;
    }

    public Integer getCreditPaymentId() {
        return creditPaymentId;
    }

    public void setCreditPaymentId(Integer creditPaymentId) {
        this.creditPaymentId = creditPaymentId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
