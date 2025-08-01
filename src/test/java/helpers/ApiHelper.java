package helpers;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ApiHelper {
    String baseURI;

    public ApiHelper(String baseUri) {
        this.baseURI = baseUri;
    }

    @Step("Send GET to /api/ingredients")
    public Response sendRequestGetIngredients() {
        return given()
                .get(baseURI + "/api/ingredients");
    }

    @Step("Send POST to /api/orders")
    public Response sendRequestCreateOrder(String token, String json) {
        return given()
                .header("Authorization", token)
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post(baseURI + "/api/orders");
    }

    @Step("Send POST to /api/auth/register")
    public Response sendRequestCreateUser(String json) {
        return sendPost("/api/auth/register", json);
    }

    @Step("Send POST to /api/auth/login")
    public Response sendRequestLoginUser(String json) {
        return sendPost("/api/auth/login", json);
    }

    @Step("Send PATCH to /api/auth/user")
    public Response sendRequestUpdateUserInfo(String token, String json) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .body(json)
                .when()
                .patch(baseURI + "/api/auth/user");
    }

    @Step("Send DELETE to /api/auth/user")
    public Response sendRequestDeleteUser(String token) {
        return given()
                .header("Authorization", token)
                .when()
                .delete(baseURI + "/api/auth/user");
    }

    @Step
    public Response sendRequestGetUserOrders(String token) {
        return given()
                .header("Authorization", token)
                .when()
                .get(baseURI + "/api/orders");
    }

    private Response sendPost(String endpoint, String json) {
        return given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post(baseURI + endpoint);
    }
}
