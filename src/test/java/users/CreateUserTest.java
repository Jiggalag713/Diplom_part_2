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

public class CreateUserTest {
    private static final String BASE_URI = "https://stellarburgers.nomoreparties.site";
    private static final ApiHelper API = new ApiHelper(BASE_URI);
    private static final Steps STEPS = new Steps(API);
    private static final Gson GSON = new Gson();

    @Test
    @Description("Check success user creation")
    void createUniqueUser() {
        User user = new User("pktest@mail.ru", "password", "Test User");
        String json = GSON.toJson(user);
        Response response = STEPS.createUser(json);
        STEPS.compareStatusCode(response, 200);
        STEPS.deleteUser(response);
    }

    @Test
    @Description("Check that it's impossible to create already existed user")
    void createExistedUser() {
        User user = new User("pktest@mail.ru", "password", "Test User");
        String json = GSON.toJson(user);
        Response response = STEPS.createUser(json);
        Response createSameUserResponse = STEPS.createUser(json);
        STEPS.compareStatusCode(createSameUserResponse, 403);
        STEPS.deleteUser(response);
    }

    @ParameterizedTest
    @CsvSource({
            "'', '', '', 403",
            "pktest@mail.ru, '', '', 403",
            "'', password, '', 403",
            "'', '', Test User, 403",
            "'', '', 'Test User', 403",
            "pktest@mail.ru, password, '', 403",
            "pktest@mail.ru, '', Test User, 403",
            "'', password, Test User, 403"
    })
    @Description("Check that it's impossible to create courier without any of mandatory field")
    void createUserWithoutMandatoryField(String email, String password, String name, String statusCode) {
        User user = new User(email, password, name);
        String json = GSON.toJson(user);
        Response response = STEPS.createUser(json);
        STEPS.compareStatusCode(response, Integer.parseInt(statusCode));
    }
}
