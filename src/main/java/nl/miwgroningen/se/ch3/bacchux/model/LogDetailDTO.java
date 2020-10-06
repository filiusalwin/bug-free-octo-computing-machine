package nl.miwgroningen.se.ch3.bacchux.model;

import java.time.LocalDate;

public class LogDetailDTO {

    public LogDetailDTO (LogDetail logDetail) {
        logId = logDetail.getLogId();
        localDate = logDetail.getLocalDate();
        servingUser = logDetail.getServingUser();
        directPaymentDetails =logDetail.getDirectPaymentDetails();
    }

    private Integer logId;

    private LocalDate localDate;

    private String servingUser;

    private String directPaymentDetails;

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public String getServingUser() {
        return servingUser;
    }

    public void setServingUser(String servingUser) {
        this.servingUser = servingUser;
    }

    public String getDirectPaymentDetails() {
        return directPaymentDetails;
    }

    public void setDirectPaymentDetails(String directPaymentDetails) {
        this.directPaymentDetails = directPaymentDetails;
    }
}
