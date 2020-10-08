package nl.miwgroningen.se.ch3.bacchux.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class User implements Comparable<User> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer userId;

    @Column(unique = true)
    private String username;

    @OneToMany (cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "user")
    private List<CreditPayment> creditPayments;

    private String name;

    private String password;

    private Boolean passwordNeedsChange = false;

    private String pin;

    private boolean active = true;

    private String roles;

    private boolean creditAllowed = false;

    private String creditPaymentBankAccountNumber;

    private boolean prepaidAllowed = true;

    private Integer balance;

    public Integer getCreditTotal() {
        int total = 0;
        for (CreditPayment payment : creditPayments) {
            if (!payment.isPaid()) {
                total += payment.getAmount();
            }
        }
        return total;
    }

    public String getDisplayRoles(){
        if (roles == null) {
            return "";
        }
        return  roles.replace("ROLE_BARMANAGER", "Manager")
                .replace("ROLE_BARTENDER", "Bartender")
                .replace("ROLE_CUSTOMER", "Customer")
                .replace(",", ", ");
    }

    public List<CreditPayment> getCreditPayments() {
        return creditPayments;
    }

    public void setCreditPayments(List<CreditPayment> creditPayments) {
        this.creditPayments = creditPayments;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getPasswordNeedsChange() {
        return passwordNeedsChange;
    }

    public void setPasswordNeedsChange(Boolean passwordNeedsChange) {
        this.passwordNeedsChange = passwordNeedsChange;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
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
        if (balance == null) {
            return 0;
        }
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public String getCreditPaymentBankAccountNumber() {
        return creditPaymentBankAccountNumber;
    }

    public void setCreditPaymentBankAccountNumber(String creditPaymentDetails) {
        this.creditPaymentBankAccountNumber = creditPaymentDetails;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    @Override
    public int compareTo(User user) {
        return name.compareTo(user.getName());
    }
}
