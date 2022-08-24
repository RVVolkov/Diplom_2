import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;


public class UserCreationTests {
    private UserCreationRequestPojo userCreationRequestPojo;
    private UserBaseSteps userBaseSteps;
    private String accessToken;
    ValidatableResponse response;

    @Before
    public void SetUp() {
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
    @DisplayName("Создание уникального пользователя")
    @Description("Проверка, что возвращается код 200")
    public void userCreationAndRegistrationTest() {
        response = userBaseSteps.userCreationAndRegistration(userCreationRequestPojo);
        response.assertThat().body("success", equalTo(true)).and().statusCode(200);
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    @Description("Проверка, что возвращается код 403 и корректное тело сообщения")
    public void userAlreadyExistsTest() {
        response = userBaseSteps.userCreationAndRegistration(userCreationRequestPojo);
        response = userBaseSteps.userCreationAndRegistration(userCreationRequestPojo);
        response.assertThat().body("success", equalTo(false))
                .and().assertThat().body("message", equalTo("User already exists"))
                .and().statusCode(403);
    }

    @Test
    @DisplayName("Создание пользователя без указания почты")
    @Description("Проверка, что возвращается код 403 и корректное тело сообщения")
    public void creationAndRegistrationUserWithoutEmailTest() {
        response = userBaseSteps.creationAndRegistrationUserWithoutEmail(userCreationRequestPojo);
        response.assertThat().body("success", equalTo(false))
                .and().assertThat().body("message", equalTo("Email, password and name are required fields"))
                .and().statusCode(403);
    }

    @Test
    @DisplayName("Создание пользователя без указания пароля")
    @Description("Проверка, что возвращается код 403 и корректное тело сообщения")
    public void creationAndRegistrationUserWithoutPasswordTest() {
        response = userBaseSteps.creationAndRegistrationUserWithoutPassword(userCreationRequestPojo);
        response.assertThat().body("success", equalTo(false))
                .and().assertThat().body("message", equalTo("Email, password and name are required fields"))
                .and().statusCode(403);
    }

    @Test
    @DisplayName("Создание пользователя без указания имени")
    @Description("Проверка, что возвращается код 403")
    public void creationAndRegistrationUserWithoutNameTest() {
        response = userBaseSteps.creationAndRegistrationUserWithoutName(userCreationRequestPojo);
        response.assertThat().body("success", equalTo(false))
                .and().assertThat().body("message", equalTo("Email, password and name are required fields"))
                .and().statusCode(403);
    }
}
