package nl.miwgroningen.se.ch3.bacchux.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class LogDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer logId;

    private LocalDateTime timestamp;

    private String bartender;

    private String customer;

    private Integer totalAmount;

    private String paymentType;

    private String paymentDetails;

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime localDateTime) {
        this.timestamp = localDateTime;
    }

    public String getBartender() {
        return bartender;
    }

    public void setBartender(String servingUser) {
        this.bartender = servingUser;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(String directPaymentDetails) {
        this.paymentDetails = directPaymentDetails;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }
}
