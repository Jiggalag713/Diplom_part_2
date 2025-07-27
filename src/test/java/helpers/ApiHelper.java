package helpers;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ApiHelper {
    String baseURI = "https://stellarburgers.nomoreparties.site";

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

    @Step("Send POST to /api/password-reset")
    public Response sendRequestResetPassword(String json) {
        return sendPost("/api/password-reset", json);
    }

    @Step("Send POST to /api/auth/register")
    public Response sendRequestCreateUser(String json) {
        return sendPost("/api/auth/register", json);
    }

    @Step("Send POST to /api/auth/login")
    public Response sendRequestLoginUser(String json) {
        return sendPost("/api/auth/login", json);
    }

    @Step("Send POST to /api/auth/logout")
    public Response sendRequestLogoutUser(String json) {
        return sendPost("/api/auth/logout", json);
    }

    @Step("Send POST to /api/auth/token")
    public Response sendRequestRefreshToken(String json) {
        return sendPost("/api/auth/token", json);
    }

    @Step("Send GET to /api/auth/user")
    public Response sendRequestGetUserInfo(String token) {
        return given()
                .header("Authorization", "token")
                .get(baseURI + "/api/auth/user");
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

    @Step("Send GET to /api/orders/all")
    public Response sendRequestGetAllOrders() {
        return given()
                .get(baseURI + "/api/orders/all");
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
