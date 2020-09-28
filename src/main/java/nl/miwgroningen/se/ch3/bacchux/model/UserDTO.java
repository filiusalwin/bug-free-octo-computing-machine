package nl.miwgroningen.se.ch3.bacchux.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class UserDTO {

    public UserDTO(User user) {
        userId = user.getUserId();
        username = user.getUsername();
        name = user.getName();
        active = user.isActive();
        roles = user.getRoles();
        prepaidAllowed = user.isPrepaidAllowed();
        creditAllowed = user.isCreditAllowed();
        balance = user.getBalance();
        creditPaymentBankAccountNumber = user.getCreditPaymentBankAccountNumber();
    }

    private Integer userId;

    private String username;

    private String name;

    private boolean active = true;

    private String roles;

    private boolean prepaidAllowed;

    private boolean creditAllowed;

    private Integer balance;

    private String creditPaymentBankAccountNumber;

    public String getCreditPaymentBankAccountNumber() {
        return creditPaymentBankAccountNumber;
    }

    public void setCreditPaymentBankAccountNumber(String creditPaymentBankAccountNumber) {
        this.creditPaymentBankAccountNumber = creditPaymentBankAccountNumber;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public boolean isCreditAllowed() {
        return creditAllowed;
    }

    public void setCreditAllowed(boolean creditAllowed) {
        this.creditAllowed = creditAllowed;
    }

    public boolean isPrepaidAllowed() {
        return prepaidAllowed;
    }

    public void setPrepaidAllowed(boolean prepaidAllowed) {
        this.prepaidAllowed = prepaidAllowed;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }
}
