package orders;

import com.google.gson.Gson;
import helpers.ApiHelper;
import helpers.Steps;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import model.Ingredients;
import model.User;
import org.junit.jupiter.api.Test;

public class GetOrderTest {
    private static final String BASE_URI = "https://stellarburgers.nomoreparties.site";
    private static final ApiHelper API = new ApiHelper(BASE_URI);
    private static final Steps STEPS = new Steps(API);
    private static final Gson GSON = new Gson();

    @Test
    @Description("Get order of authentificated user")
    void getOrderAuthUser() {
        User user = new User("pktest@mail.ru", "password", "Test User");
        String json = GSON.toJson(user);
        STEPS.createUser(json);
        Response loginResponse = STEPS.loginUser(json);
        Ingredients ingredients = new Ingredients(STEPS.getIngredientsIds(3));
        String ingredientsJson = GSON.toJson(ingredients);
        STEPS.createOrder(loginResponse, ingredientsJson);
        Response getOrderResponse = STEPS.getOrder(loginResponse);
        STEPS.compareStatusCode(getOrderResponse, 200);
        STEPS.deleteUser(loginResponse);
    }

    @Test
    @Description("Get order of nonauthentificated user")
    void getOrderNonAuthUser() {
        Response getOrderResponse = STEPS.getOrder(null);
        STEPS.compareStatusCode(getOrderResponse, 401);
    }
}
