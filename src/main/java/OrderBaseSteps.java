import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderBaseSteps extends BaseClass {
    @Step("Создание заказа с авторизацией")
    public ValidatableResponse creatingOrderWithAuth(OrderRequestPojo orderRequestPojo, String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .when()
                .body(orderRequestPojo)
                .post(Endpoints.ORDER_PATH.endpoint)
                .then();
    }

    @Step("Создание заказа без авторизации")
    public ValidatableResponse creatingOrderWithoutAuth(OrderRequestPojo orderRequestPojo) {
        return given()
                .spec(getBaseSpec())
                .when()
                .body(orderRequestPojo)
                .post(Endpoints.ORDER_PATH.endpoint)
                .then();
    }

    @Step("Создание заказа без ингридиентов")
    public ValidatableResponse creatingOrderWithoutFilling(String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .when()
                .post(Endpoints.ORDER_PATH.endpoint)
                .then();
    }

    @Step("Получение заказов конкретного пользователя с авторизацией")
    public ValidatableResponse getOrderListWithAuth(OrderRequestPojo orderRequestPojo, String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .when()
                .body(orderRequestPojo)
                .get(Endpoints.ORDER_PATH.endpoint)
                .then();
    }

    @Step("Получение заказов конкретного пользователя без авторизации")
    public ValidatableResponse getOrderListWithoutAuth(OrderRequestPojo orderRequestPojo) {
        return given()
                .spec(getBaseSpec())
                .when()
                .body(orderRequestPojo)
                .get(Endpoints.ORDER_PATH.endpoint)
                .then();
    }
}
