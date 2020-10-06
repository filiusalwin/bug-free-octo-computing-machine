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

    private String servingUser;

    private Integer totalAmount;

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

    public String getServingUser() {
        return servingUser;
    }

    public void setServingUser(String servingUser) {
        this.servingUser = servingUser;
    }

    public String getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(String directPaymentDetails) {
        this.paymentDetails = directPaymentDetails;
    }
}
