import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class ChangingUserTests {
    private UserCreationRequestPojo userCreationRequestPojo;
    private UserBaseSteps userBaseSteps;
    private String accessToken;
    ValidatableResponse response;
    Faker faker = new Faker();

    @Before
    public void setUp() {
        userCreationRequestPojo = userCreationRequestPojo.getRandomUser();
        userBaseSteps = new UserBaseSteps();
        userBaseSteps.userCreationAndRegistration(userCreationRequestPojo);
        response = userBaseSteps.userAuthorization(UserAuthorizationRequestPojo.from(userCreationRequestPojo));
        accessToken = response.extract().body().path("accessToken");
    }

    @After
    public void tearDown() {
        accessToken = response.extract().body().path("accessToken");
        if (accessToken != null) {
            userBaseSteps.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Изменение электронной почты пользователя с авторизацией")
    @Description("Проверка, что возвращается код 200")
    public void updateEmailAuthUserTest() {
        userCreationRequestPojo.setEmail(faker.internet().emailAddress());
        userBaseSteps.updateAuthUser(accessToken, new UserCreationRequestPojo(userCreationRequestPojo.getEmail(), userCreationRequestPojo.getName()))
                .assertThat()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Изменение имени пользователя с авторизацией")
    @Description("Проверка, что возвращается код 200")
    public void updateNameAuthUserTest() {
        userCreationRequestPojo.setName(faker.name().firstName());
        userBaseSteps.updateAuthUser(accessToken, new UserCreationRequestPojo(userCreationRequestPojo.getEmail(), userCreationRequestPojo.getName()))
                .assertThat()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Применение электронной почты, которая уже используется")
    @Description("Проверка, что возвращается код 403 и корректное тело сообщения")
    public void updateAlreadyExistsEmailAuthUserTest() {
        UserCreationRequestPojo userCreationRequestPojoNew = UserCreationRequestPojo.getRandomUser();
        userBaseSteps.userCreationAndRegistration(userCreationRequestPojoNew);
        userCreationRequestPojo.setEmail(userCreationRequestPojoNew.getEmail());
        userBaseSteps.updateAuthUser(accessToken, userCreationRequestPojoNew)
                .assertThat().body("success", equalTo(false))
                .and().assertThat().body("message", equalTo("User with such email already exists"))
                .and().statusCode(403);
    }

    @Test
    @DisplayName("Изменение электронной почты пользователя без авторизацией")
    @Description("Проверка, что возвращается код 401 и корректное тело сообщения")
    public void updateUserEmailWithoutAuthTest() {
        userCreationRequestPojo.setEmail(faker.internet().emailAddress());
        userBaseSteps.updateUserWithoutAuth(new UserCreationRequestPojo(userCreationRequestPojo.getEmail(), userCreationRequestPojo.getName()))
                .assertThat().body("success", equalTo(false))
                .and().assertThat().body("message", equalTo("You should be authorised"))
                .and().statusCode(401);
    }

    @Test
    @DisplayName("Изменение имени пользователя без авторизацией")
    @Description("Проверка, что возвращается код 401 и корректное тело сообщения")
    public void updateUserNameWithoutAuthTest() {
        userCreationRequestPojo.setName(faker.name().firstName());
        userBaseSteps.updateUserWithoutAuth(new UserCreationRequestPojo(userCreationRequestPojo.getEmail(), userCreationRequestPojo.getName()))
                .assertThat().body("success", equalTo(false))
                .and().assertThat().body("message", equalTo("You should be authorised"))
                .and().statusCode(401);
    }
}
