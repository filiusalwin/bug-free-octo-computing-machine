package nl.miwgroningen.se.ch3.bacchux.controller;

import nl.miwgroningen.se.ch3.bacchux.model.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PrepaidRestControllerTest {
    @Test
    public void testUserCanPay() {
        User user0 = new User();
        user0.setBalance(0);
        User user500 = new User();
        user500.setBalance(500);
        PrepaidRestController controller = new PrepaidRestController();

        assertThat(controller.userCanPay(user0, 100))
                .isEqualTo(false);
        assertThat(controller.userCanPay(user500, 100))
                .isEqualTo(true);
        assertThat(controller.userCanPay(user500, 500))
                .isEqualTo(true);
        assertThat(controller.userCanPay(user500, 600))
                .isEqualTo(false);

    }
}
