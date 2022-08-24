import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class UserAuthorizationTests {
    private UserCreationRequestPojo userCreationRequestPojo;
    private UserBaseSteps userBaseSteps;
    private String accessToken;
    ValidatableResponse response;

    @Before
    public void setUp() {
        userCreationRequestPojo = userCreationRequestPojo.getRandomUser();
        userBaseSteps = new UserBaseSteps();
    }

    @After
    public void tearDown() {
        accessToken = response.extract().body().path("accessToken");
        if (accessToken != null) {
            userBaseSteps.deleteUser(accessToken);
        }
    }


    @Test
    @DisplayName("Авторизация под существующим пользователем")
    @Description("Проверка, что возвращается код 200")
    public void userAuthorizationTest() {
        userBaseSteps.userCreationAndRegistration(userCreationRequestPojo);
        response = userBaseSteps.userAuthorization(UserAuthorizationRequestPojo.from(userCreationRequestPojo));
        response.assertThat().body("success", equalTo(true)).and().statusCode(200);
    }

    @Test
    @DisplayName("Авторизация пользователя без указания почты")
    @Description("Проверка, что возвращается код 401 и корректное тело сообщения")
    public void authorizationUserWithoutEmailTest() {
        userBaseSteps.creationAndRegistrationUserWithoutEmail(userCreationRequestPojo);
        response = userBaseSteps.userAuthorization(UserAuthorizationRequestPojo.from(userCreationRequestPojo));
        response.assertThat().body("success", equalTo(false))
                .and().assertThat().body("message", equalTo("email or password are incorrect"))
                .and().statusCode(401);
    }

    @Test
    @DisplayName("Авторизация пользователя без указания пароля")
    @Description("Проверка, что возвращается код 401 и корректное тело сообщения")
    public void authorizationUserWithoutPasswordTest() {
        userBaseSteps.creationAndRegistrationUserWithoutPassword(userCreationRequestPojo);
        response = userBaseSteps.userAuthorization(UserAuthorizationRequestPojo.from(userCreationRequestPojo));
        response.assertThat().body("success", equalTo(false))
                .and().assertThat().body("message", equalTo("email or password are incorrect"))
                .and().statusCode(401);
    }
}
