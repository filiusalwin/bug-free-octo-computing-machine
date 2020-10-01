package nl.miwgroningen.se.ch3.bacchux.model;

//This file contains a method to validate a Dutch IBAN number

import java.math.BigInteger;

public class IbanValidation {

    private static final int IBAN_SIZE = 18;
    private static final BigInteger IBAN_MODULUS = new BigInteger("97");

    private String iban = "NL02 RABO 0306 6480 59";


//Check if the total IBAN length is 18. If not IBAN is invalid.

    private boolean ibanLengthCorrect(String iban) {

        iban = iban.replaceAll("\\s","");

        return iban.length() == IBAN_SIZE;
    }

    //Move the four initials to the end of the string

    private String moveInitialsIban(String iban) {

        iban = iban.replaceAll("\\s","");

        iban = iban.substring(4) + iban.substring(0, 4);

        return iban;
    }

    //Replace each letter in the string with two digits,
    //thereby expanding the string, where A = 10, B = 11, ..., Z = 35

    private BigInteger convertIbanToInt(String iban) {

        iban = moveInitialsIban(iban);

        String str = "";

        for (int i = 0; i < iban.length(); i++) {

            int charValue = Character.getNumericValue(iban.charAt(i));

            if (charValue < 0 || charValue > 35) {
                return new BigInteger("0");
            }
            str += charValue;
        }
        return new BigInteger(str);
    }

    //Interpret the string as a decimal integer and compute the remainder of that number on division by 97
    //If the remainder is 1, the check digit test is passed and the IBAN might be valid.

    public boolean validateIban (String iban) {

        if (!ibanLengthCorrect(iban)) {
            return false;
        }

        BigInteger bigIntegerIban = convertIbanToInt(iban);

        BigInteger modulusIban = bigIntegerIban.mod(IBAN_MODULUS);

        return modulusIban.equals(BigInteger.ONE);
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

}




















