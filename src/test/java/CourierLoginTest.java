import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;

public class CourierLoginTest extends BaseTest {

    private String login;
    private String password;
    private String firstName;

    @Before
    public void prepareTestData() {
        login = "auth_courier_" + System.currentTimeMillis();
        password = "password123";
        firstName = "Сергей Сергеев";

        Map<String, Object> courierData = new HashMap<>();
        courierData.put("login", login);
        courierData.put("password", password);
        courierData.put("firstName", firstName);

        createCourier(courierData);
    }

    @Test
    @DisplayName("Логин курьера: успешный запрос возвращает id")
    public void loginWithValidCredentialsShouldBeSuccessful() {
        Map<String, Object> loginData = new HashMap<>();
        loginData.put("login", login);
        loginData.put("password", password);

        Response response = loginCourier(loginData);

        response.then()
                .statusCode(200)
                .body("id", notNullValue());

        courierId = response.then().extract().path("id");
    }

    @Test
    @DisplayName("Логин курьера: неверный пароль возвращает ошибку")
    public void loginWithWrongPasswordShouldReturnError() {
        Map<String, Object> loginData = new HashMap<>();
        loginData.put("login", login);
        loginData.put("password", "wrongpassword");

        Response response = loginCourier(loginData);

        response.then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Логин курьера: неверный логин возвращает ошибку")
    public void loginWithWrongLoginShouldReturnError() {
        Map<String, Object> loginData = new HashMap<>();
        loginData.put("login", "nonexistentlogin");
        loginData.put("password", password);

        Response response = loginCourier(loginData);

        response.then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Логин курьера: запрос без логина возвращает ошибку")
    public void loginWithoutLoginShouldReturnError() {
        Map<String, Object> loginData = new HashMap<>();
        loginData.put("login", "");
        loginData.put("password", password);

        Response response = loginCourier(loginData);

        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Логин курьера: запрос без пароля возвращает ошибку")
    public void loginWithoutPasswordShouldReturnError() {
        Map<String, Object> loginData = new HashMap<>();
        loginData.put("login", login);
        loginData.put("password", "");

        Response response = loginCourier(loginData);

        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Логин курьера: несуществующий пользователь возвращает ошибку")
    public void loginNonExistentUserShouldReturnError() {
        Map<String, Object> loginData = new HashMap<>();
        loginData.put("login", "nonexistent_" + System.currentTimeMillis());
        loginData.put("password", "password");

        Response response = loginCourier(loginData);

        response.then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }
}