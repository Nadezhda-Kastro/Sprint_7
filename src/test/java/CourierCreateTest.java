import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;

public class CourierCreateTest extends BaseTest {

    @Test
    @DisplayName("Создание курьера: успешный запрос возвращает ok: true")
    public void createCourierWithValidDataShouldBeSuccessful() {
        Map<String, Object> courierData = new HashMap<>();
        courierData.put("login", "courier_" + System.currentTimeMillis());
        courierData.put("password", "password123");
        courierData.put("firstName", "Иван Иванов");

        Response response = createCourier(courierData);

        response.then()
                .statusCode(201)
                .body("ok", is(true));

        Map<String, Object> loginData = new HashMap<>();
        loginData.put("login", courierData.get("login"));
        loginData.put("password", courierData.get("password"));
        Response loginResponse = loginCourier(loginData);
        courierId = loginResponse.then().extract().path("id");
    }

    @Test
    @DisplayName("Создание курьера: нельзя создать двух одинаковых курьеров")
    public void createDuplicateCouriersShouldReturnError() {
        Map<String, Object> courierData = new HashMap<>();
        courierData.put("login", "duplicate_" + System.currentTimeMillis());
        courierData.put("password", "password123");
        courierData.put("firstName", "Петр Петров");

        createCourier(courierData);

        Response response = createCourier(courierData);

        response.then()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));

        Map<String, Object> loginData = new HashMap<>();
        loginData.put("login", courierData.get("login"));
        loginData.put("password", courierData.get("password"));
        Response loginResponse = loginCourier(loginData);
        courierId = loginResponse.then().extract().path("id");
    }

    @Test
    @DisplayName("Создание курьера: запрос без логина возвращает ошибку")
    public void createCourierWithoutLoginShouldReturnError() {
        Map<String, Object> courierData = new HashMap<>();
        courierData.put("login", "");
        courierData.put("password", "password123");
        courierData.put("firstName", "Алексей Алексеев");

        Response response = createCourier(courierData);

        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера: запрос без пароля возвращает ошибку")
    public void createCourierWithoutPasswordShouldReturnError() {
        Map<String, Object> courierData = new HashMap<>();
        courierData.put("login", "nopassword_" + System.currentTimeMillis());
        courierData.put("password", "");
        courierData.put("firstName", "Мария Мариева");

        Response response = createCourier(courierData);

        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера: запрос без имени выполняется успешно")
    public void createCourierWithoutFirstNameShouldBeSuccessful() {
        Map<String, Object> courierData = new HashMap<>();
        courierData.put("login", "noname_" + System.currentTimeMillis());
        courierData.put("password", "password123");
        courierData.put("firstName", "");

        Response response = createCourier(courierData);

        response.then()
                .statusCode(201)
                .body("ok", is(true));

        Map<String, Object> loginData = new HashMap<>();
        loginData.put("login", courierData.get("login"));
        loginData.put("password", courierData.get("password"));
        Response loginResponse = loginCourier(loginData);
        courierId = loginResponse.then().extract().path("id");
    }
}