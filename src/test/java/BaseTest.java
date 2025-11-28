import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import com.google.gson.Gson;
import org.junit.After;
import org.junit.Before;

public class BaseTest {
    protected Integer courierId;
    protected final Gson gson = new Gson();

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Step("Создать курьера")
    protected Response createCourier(Object courierData) {
        String requestBody = gson.toJson(courierData);
        return RestAssured.given()
                .header("Content-type", "application/json")
                .body(requestBody)
                .when()
                .post("/api/v1/courier");
    }

    @Step("Авторизация курьера")
    protected Response loginCourier(Object loginData) {
        String requestBody = gson.toJson(loginData);
        return RestAssured.given()
                .header("Content-type", "application/json")
                .body(requestBody)
                .when()
                .post("/api/v1/courier/login");
    }

    @Step("Создать заказ")
    protected Response createOrder(Object orderData) {
        String requestBody = gson.toJson(orderData);
        return RestAssured.given()
                .header("Content-type", "application/json")
                .body(requestBody)
                .when()
                .post("/api/v1/orders");
    }

    @Step("Получить список заказов")
    protected Response getOrdersList() {
        return RestAssured.given()
                .when()
                .get("/api/v1/orders");
    }

    @Step("Получить список заказов с параметрами")
    protected Response getOrdersListWithParams(Integer page, Integer limit) {
        return RestAssured.given()
                .queryParam("page", page)
                .queryParam("limit", limit)
                .when()
                .get("/api/v1/orders");
    }

    @Step("Удалить курьера")
    protected void deleteCourier(Integer id) {
        if (id != null) {
            RestAssured.given()
                    .when()
                    .delete("/api/v1/courier/" + id);
        }
    }

    @After
    public void tearDown() {
        if (courierId != null) {
            deleteCourier(courierId);
        }
    }
}