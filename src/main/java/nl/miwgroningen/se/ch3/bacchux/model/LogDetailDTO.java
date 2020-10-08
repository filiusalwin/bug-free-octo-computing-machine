package nl.miwgroningen.se.ch3.bacchux.model;

import java.time.LocalDateTime;

public class LogDetailDTO {

    public LogDetailDTO (LogDetail logDetail) {
        logId = logDetail.getLogId();
        timestamp = logDetail.getTimestamp();
        servingUser = logDetail.getBartender();
        totalAmount = logDetail.getTotalAmount();
        paymentDetails =logDetail.getPaymentDetails();
    }

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

    public LocalDateTime getLocalDateTime() {
        return timestamp;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.timestamp = localDateTime;
    }

    public String getServingUser() {
        return servingUser;
    }

    public void setServingUser(String servingUser) {
        this.servingUser = servingUser;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(String paymentDetails) {
        this.paymentDetails = paymentDetails;
    }
}
