package users;

import com.google.gson.Gson;
import helpers.ApiHelper;
import helpers.Steps;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class LoginUserTest {
    private static final String BASE_URI = "https://stellarburgers.nomoreparties.site";
    private static final ApiHelper API = new ApiHelper(BASE_URI);
    private static final Steps STEPS = new Steps(API);
    private static final Gson GSON = new Gson();

    @Test
    @Description("Check login with existed user")
    void loginUser() {
        User user = new User("pktest@mail.ru", "password", "Test User");
        String json = GSON.toJson(user);
        STEPS.createUser(json);
        Response loginResponse = STEPS.loginUser(json);
        STEPS.compareStatusCode(loginResponse, 200);
        STEPS.deleteUser(loginResponse);
    }

    @ParameterizedTest
    @CsvSource({
            "pktest1@mail.ru, password, 'Test User', 401",
            "pktest@mail.ru, password!, 'Test User', 401",
    })
    @Description("Check if it's impossible to login with incorrect login or password")
    void loginWithIncorrectCredentials(String email, String password, String name, String statusCode) {
        User user = new User("pktest@mail.ru", "password", "Test User");
        String json = GSON.toJson(user);
        STEPS.createUser(json);
        User incorrectUser = new User(email, password, name);
        String loginJson = GSON.toJson(incorrectUser);
        Response loginResponse = STEPS.loginUser(loginJson);
        STEPS.compareStatusCode(loginResponse, Integer.parseInt(statusCode));
    }
}
