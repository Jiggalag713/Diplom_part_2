package users;

import com.google.gson.Gson;
import helpers.ApiHelper;
import helpers.Steps;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import model.User;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class ChangeUserData {
    private static final String BASE_URI = "https://stellarburgers.nomoreparties.site";
    private static final ApiHelper API = new ApiHelper(BASE_URI);
    private static final Steps STEPS = new Steps(API);
    private static final Gson GSON = new Gson();

    @ParameterizedTest
    @CsvSource({
            "pktest1@mail.ru, password, 'Test User', 200",
            "pktest@mail.ru, password1, 'Test User', 200",
            "pktest@mail.ru, password, 'Just user', 200",
    })
    @Description("Check that it's possible to change user data if authorized")
    void changeUserDataWithAuth(String email, String password, String name, String statusCode) {
        User user = new User("pktest@mail.ru", "password", "Test User");
        String json = GSON.toJson(user);
        STEPS.createUser(json);
        Response loginResponse = STEPS.loginUser(json);
        User updateUser = new User(email, password, name);
        String updateJson = GSON.toJson(updateUser);
        Response changeDataResponse = STEPS.changeUserData(loginResponse, updateJson);
        STEPS.compareStatusCode(changeDataResponse, Integer.parseInt(statusCode));
        STEPS.deleteUser(loginResponse);
    }

    @ParameterizedTest
    @CsvSource({
            "pktest1@mail.ru, password, 'Test user', 401",
            "pktest@mail.ru, password1, 'Test user', 401",
            "pktest@mail.ru, password, 'Just user', 401",
    })
    @Description("Check that it's possible to change user data if authorized, and impossible if not")
    void changeUserDataWithoutAuth(String email, String password, String name, String statusCode) {
        User updateUser = new User(email, password, name);
        String updateJson = GSON.toJson(updateUser);
        Response changeDataResponse = STEPS.changeUserData(null, updateJson);
        STEPS.compareStatusCode(changeDataResponse, Integer.parseInt(statusCode));
    }
 }
