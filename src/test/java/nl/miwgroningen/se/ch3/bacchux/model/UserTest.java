package nl.miwgroningen.se.ch3.bacchux.model;

import nl.miwgroningen.se.ch3.bacchux.model.Product;
import org.junit.Test;

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
}
