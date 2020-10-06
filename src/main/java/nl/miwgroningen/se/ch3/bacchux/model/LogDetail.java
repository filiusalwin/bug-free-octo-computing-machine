package nl.miwgroningen.se.ch3.bacchux.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class LogDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer logId;

    private LocalDateTime localDateTime;

    private String servingUser;

    private String directPaymentDetails;

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
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
