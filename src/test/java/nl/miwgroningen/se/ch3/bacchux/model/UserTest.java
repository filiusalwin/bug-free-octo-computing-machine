package nl.miwgroningen.se.ch3.bacchux.model;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {
    @Test
    public void testEuroPrice() {
        Product product = new Product();

        product.setPrice(100000);
        assertThat(product.euroPrice())
                .isEqualTo("€1000.00");

        product.setPrice(0);
        assertThat(product.euroPrice())
                .isEqualTo("€0.00");

        product.setPrice(1234);
        assertThat(product.euroPrice())
                .isEqualTo("€12.34");
    }

    @Test
    public void testIbanValidation() {

        IbanValidation ibanValidation = new IbanValidation();

        ibanValidation.setIban("NL31BOFA0123456789");
        assertThat(ibanValidation.validateIban(ibanValidation.getIban()))
                .isEqualTo(true);

        ibanValidation.setIban("NL31 BOFA 0123 4567 89");
        assertThat(ibanValidation.validateIban(ibanValidation.getIban()))
                .isEqualTo(true);

        ibanValidation.setIban("NL31 BOFA ");
        assertThat(ibanValidation.validateIban(ibanValidation.getIban()))
                .isEqualTo(false);

        ibanValidation.setIban("@#*! BOFA 0123 4567 89");
        assertThat(ibanValidation.validateIban(ibanValidation.getIban()))
                .isEqualTo(false);
    }
}
