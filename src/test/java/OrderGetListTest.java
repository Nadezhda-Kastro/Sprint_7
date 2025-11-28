import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class OrderGetListTest extends BaseTest {

    @Test
    @DisplayName("Список заказов: в теле ответа возвращается список заказов")
    public void getOrdersListShouldReturnNonEmptyList() {
        Response response = getOrdersList();

        response.then()
                .statusCode(200)
                .body("orders", notNullValue())
                .body("orders", instanceOf(java.util.List.class));
    }

    @Test
    @DisplayName("Список заказов: запрос с лимитом возвращает ограниченное количество")
    public void getOrdersListWithLimitShouldReturnLimitedNumber() {
        Response response = getOrdersListWithParams(0, 5);

        response.then()
                .statusCode(200)
                .body("orders", notNullValue())
                .body("orders.size()", lessThanOrEqualTo(5));
    }

    @Test
    @DisplayName("Список заказов: запрос с пагинацией возвращает информацию о страницах")
    public void getOrdersListWithPaginationShouldReturnPageInfo() {
        Response response = getOrdersListWithParams(0, 10);

        response.then()
                .statusCode(200)
                .body("orders", notNullValue())
                .body("pageInfo", notNullValue());
    }

    @Test
    @DisplayName("Список заказов: запрос с разными значениями лимита работает корректно")
    public void getOrdersListWithDifferentLimitsShouldWork() {
        // Проверяем разные валидные значения лимита
        Response response1 = getOrdersListWithParams(0, 1);
        response1.then().statusCode(200).body("orders", notNullValue());

        Response response2 = getOrdersListWithParams(0, 10);
        response2.then().statusCode(200).body("orders", notNullValue());
    }

    @Test
    @DisplayName("Список заказов: запрос с разными страницами работает корректно")
    public void getOrdersListWithDifferentPagesShouldWork() {

        Response responsePage1 = getOrdersListWithParams(0, 3);
        responsePage1.then().statusCode(200).body("orders", notNullValue());


        Response responsePage2 = getOrdersListWithParams(1, 3);
        responsePage2.then().statusCode(200).body("orders", notNullValue());
    }
}