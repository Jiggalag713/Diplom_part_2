package orders;

import com.google.gson.Gson;
import helpers.ApiHelper;
import helpers.Steps;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import model.Ingredients;
import model.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class CreateOrderTest {
    private static final String BASE_URI = "https://stellarburgers.nomoreparties.site";
    private static final ApiHelper API = new ApiHelper(BASE_URI);
    private static final Steps STEPS = new Steps(API);
    private static final Gson GSON = new Gson();

    @Test
    @Description("Check creation order with authentification and correct ingredients")
    void createOrderWithAuthAndIngredients() {
        User user = new User("pktest@mail.ru", "password", "Test User");
        String json = GSON.toJson(user);
        STEPS.createUser(json);
        Response loginResponse = STEPS.loginUser(json);
        Ingredients ingredients = new Ingredients(STEPS.getIngredientsIds(3));
        String ingredientsJson = GSON.toJson(ingredients);
        Response createOrder = STEPS.createOrder(loginResponse, ingredientsJson);
        STEPS.compareStatusCode(createOrder, 200);
        STEPS.deleteUser(loginResponse);
    }

    @Test
    @Description("Check creation order without authentification and correct ingredients")
    void createOrderWithoutAuthAndWithIngredients() {
        Ingredients ingredients = new Ingredients(STEPS.getIngredientsIds(3));
        String ingredientsJson = GSON.toJson(ingredients);
        Response createOrder = STEPS.createOrder(null, ingredientsJson);
        STEPS.compareStatusCode(createOrder, 401);
    }

    @Test
    @Description("Check creation order with authentification and without ingredients")
    void createOrderWithAuthAndWithoutIngredients() {
        User user = new User("pktest@mail.ru", "password", "Test User");
        String json = GSON.toJson(user);
        STEPS.createUser(json);
        Response loginResponse = STEPS.loginUser(json);
        Ingredients ingredients = new Ingredients(STEPS.getIngredientsIds(0));
        String ingredientsJson = GSON.toJson(ingredients);
        Response createOrder = STEPS.createOrder(loginResponse, ingredientsJson);
        STEPS.compareStatusCode(createOrder, 400);
        STEPS.deleteUser(loginResponse);
    }

    @Test
    @Description("Check creation order without authentification and ingredients")
    void createOrderWithoutAuthAndIngredients() {
        Ingredients ingredients = new Ingredients(STEPS.getIngredientsIds(0));
        String ingredientsJson = GSON.toJson(ingredients);
        Response createOrder = STEPS.createOrder(null, ingredientsJson);
        STEPS.compareStatusCode(createOrder, 401);
    }

    @Test
    @Description("Check creation order with authentication and incorrect ingredients")
    void createOrderWithAuthAndIncorrectIngredients(){
        User user = new User("pktest@mail.ru", "password", "Test User");
        String json = GSON.toJson(user);
        STEPS.createUser(json);
        Response loginResponse = STEPS.loginUser(json);
        Ingredients ingredients = new Ingredients(new ArrayList<>(Arrays.asList("21412421", "trololo")));
        String ingredientsJson = GSON.toJson(ingredients);
        Response createOrder = STEPS.createOrder(loginResponse, ingredientsJson);
        STEPS.compareStatusCode(createOrder, 500);
        STEPS.deleteUser(loginResponse);
    }
}
