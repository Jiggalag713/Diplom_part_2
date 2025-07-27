package helpers;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Random;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.in;

public class Steps {
    private final ApiHelper api;

    public Steps(ApiHelper api) {
        this.api = api;
    }

    @Step("Compare actual status code with expected")
    public void compareStatusCode(Response response, Integer statusCode) {
        response.then().assertThat().statusCode(statusCode);
    }

    @Step("Check if actual field value equals to expected value")
    public void checkResponseFieldValue(Response response, String key, Object value) {
        response.then().assertThat().body(key, equalTo(value));
    }

    @Step("Create user")
    public Response createUser(String json) {
        return api.sendRequestCreateUser(json);
    }

    @Step("Login user")
    public Response loginUser(String json) {
        return api.sendRequestLoginUser(json);
    }

    @Step("Get accessToken")
    public String getAccessToken(Response response) {
        return response.path("accessToken");
    }

    @Step("Get refreshToken")
    public String getRefreshToken(Response response) {
        return response.path("refreshToken");
    }

    @Step("Delete existed user")
    public Response deleteUser(Response response) {
        String accessToken = getAccessToken(response);
        return api.sendRequestDeleteUser(accessToken);
    }

    @Step("Change user data")
    public Response changeUserData(Response response, String json) {
        if (response != null) {
            String accessToken = getAccessToken(response);
            return api.sendRequestUpdateUserInfo(accessToken, json);
        } else {
            return api.sendRequestUpdateUserInfo("", json);
        }
    }

    @Step("Getting ingredients")
    public ArrayList<LinkedHashMap<String, String>> getIngredients() {
        return api.sendRequestGetIngredients().path("data");
    }

    @Step
    public ArrayList<String> getIngredientsIds(Integer amount) {
        Random random = new Random();
        ArrayList<String> result = new ArrayList<>();
        ArrayList<LinkedHashMap<String, String>> ingredients = getIngredients();
        for (int i = 0; i < amount; i++) {
            String ingredientHash = ingredients.get(random.nextInt(ingredients.size() - 1)).get("_id");
            result.add(ingredientHash);
        }
        return result;
    }

    @Step("Creating order")
    public Response createOrder(Response response, String json) {
        if (response != null) {
            String accessToken = getAccessToken(response);
            return api.sendRequestCreateOrder(accessToken, json);
        } else {
            return api.sendRequestUpdateUserInfo("", json);
        }
    }

    @Step("Getting order")
    public Response getOrder(Response response) {
        if (response != null) {
            String accessToken = getAccessToken(response);
            return api.sendRequestGetUserOrders(accessToken);
        } else {
            return api.sendRequestGetUserOrders("");
        }
    }
}
