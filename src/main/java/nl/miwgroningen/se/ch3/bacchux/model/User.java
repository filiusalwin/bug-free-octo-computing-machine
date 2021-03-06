package nl.miwgroningen.se.ch3.bacchux.model;

import javax.persistence.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Entity
public class User implements Comparable<User> {

    private static final int CENTS_PER_EURO = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer userId;

    @Column(unique = true)
    private String username;

    @OneToMany (cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "customer")
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

    @Lob
    private byte[] picture;

    public String balanceEuro(){
        double balanceInEuro;
        if (balance == null) {
            balanceInEuro = 0;
        } else {
            balanceInEuro = (double) balance / CENTS_PER_EURO;
        }
        return String.format("€%.2f", balanceInEuro);
    }

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

    public String convertToBase64() {
        String imageInBase64 = "";
        if (this.getPicture() == null){
            try {
                // Set a default image
                File image = new File("src/main/resources/static/images/defaultPicture.png");
                FileInputStream imageInFile = new FileInputStream(image);
                byte[] imageInBytes = imageInFile.readAllBytes();
                imageInBase64 += Base64.getEncoder().encodeToString(imageInBytes);
            }
            catch (IOException ioe) {
                System.out.println("Exception while reading the Image " + ioe);
            }
            return imageInBase64;
        }
        //Use custom image
        imageInBase64 += Base64.getEncoder().encodeToString(this.getPicture());
        return imageInBase64;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
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
        return name.toLowerCase().compareTo(user.getName().toLowerCase());
    }
}
