import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;

@RunWith(Parameterized.class)
public class OrderCreateTest extends BaseTest {

    private final List<String> colors;

    public OrderCreateTest(List<String> colors) {
        this.colors = colors;
    }

    @Parameterized.Parameters
    public static Object[][] getColorData() {
        return new Object[][] {
                {Arrays.asList("BLACK")},
                {Arrays.asList("GREY")},
                {Arrays.asList("BLACK", "GREY")},
                {Arrays.asList()}
        };
    }

    @Test
    @DisplayName("Создание заказа: тело ответа содержит track")
    public void createOrderWithColorShouldReturnTrackNumber() {
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("firstName", "Анна");
        orderData.put("lastName", "Петрова");
        orderData.put("address", "Москва, ул. Ленина, 1");
        orderData.put("metroStation", 4);
        orderData.put("phone", "+79991234567");
        orderData.put("rentTime", 5);
        orderData.put("deliveryDate", "2024-12-12T21:00:00.000Z");
        orderData.put("comment", "Тестовый заказ");

        if (!colors.isEmpty()) {
            orderData.put("color", colors);
        }

        Response response = createOrder(orderData);

        response.then()
                .statusCode(201)
                .body("track", notNullValue());
    }
}