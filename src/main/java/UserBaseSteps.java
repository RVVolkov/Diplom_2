import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class UserBaseSteps extends BaseClass {
    @Step("Создание и регистрация пользователя")
    public ValidatableResponse userCreationAndRegistration(UserCreationRequestPojo userCreationRequestPojo) {
        return given()
                .spec(getBaseSpec())
                .body(userCreationRequestPojo)
                .when()
                .post(Endpoints.CREATING_USER.endpoint)
                .then();
    }

    @Step("Создание и регистрация пользователя без указания почты")
    public ValidatableResponse creationAndRegistrationUserWithoutEmail(UserCreationRequestPojo userCreationRequestPojo) {
        userCreationRequestPojo.setEmail("");
        return given()
                .spec(getBaseSpec())
                .body(userCreationRequestPojo)
                .post(Endpoints.CREATING_USER.endpoint)
                .then();
    }

    @Step("Создание и регистрация пользователя без указания пароля")
    public ValidatableResponse creationAndRegistrationUserWithoutPassword(UserCreationRequestPojo userCreationRequestPojo) {
        userCreationRequestPojo.setPassword("");
        return given()
                .spec(getBaseSpec())
                .body(userCreationRequestPojo)
                .post(Endpoints.CREATING_USER.endpoint)
                .then();
    }

    @Step("Создание и регистрация пользователя без указания имени")
    public ValidatableResponse creationAndRegistrationUserWithoutName(UserCreationRequestPojo userCreationRequestPojo) {
        userCreationRequestPojo.setName("");
        return given()
                .spec(getBaseSpec())
                .body(userCreationRequestPojo)
                .post(Endpoints.CREATING_USER.endpoint)
                .then();
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse userAuthorization(UserAuthorizationRequestPojo userAuthorizationRequestPojo) {
        return given()
                .spec(getBaseSpec())
                .body(userAuthorizationRequestPojo)
                .when()
                .post(Endpoints.LOGIN_USER.endpoint)
                .then();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse deleteUser(String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .when()
                .delete(Endpoints.USER_PATH.endpoint)
                .then()
                .assertThat().body("message", equalTo("User successfully removed"))
                .and().statusCode(202);
    }

    @Step("Изменение данных пользователя с авторизацией")
    public ValidatableResponse updateAuthUser(String accessToken, UserCreationRequestPojo userCreationRequestPojo) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .body(userCreationRequestPojo)
                .when()
                .patch(Endpoints.USER_PATH.endpoint)
                .then();
    }

    @Step("Изменение данных пользователя без авторизации")
    public ValidatableResponse updateUserWithoutAuth(UserCreationRequestPojo userCreationRequestPojo) {
        return given()
                .spec(getBaseSpec())
                .body(userCreationRequestPojo)
                .when()
                .patch(Endpoints.USER_PATH.endpoint)
                .then();
    }
}
