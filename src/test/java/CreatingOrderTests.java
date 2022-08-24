import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;

public class CreatingOrderTests {
    private UserCreationRequestPojo userCreationRequestPojo;
    private UserBaseSteps userBaseSteps;
    private OrderRequestPojo orderRequestPojo;
    private OrderBaseSteps orderBaseSteps;
    private IngredientsResponsePojo ingredientsResponsePojo;
    private IngredientsBaseSteps ingredientsBaseSteps;
    private List<String> ingredients;
    private String accessToken;
    ValidatableResponse response;
    ValidatableResponse responseToken;

    @Before
    public void setUp() {
        userCreationRequestPojo = UserCreationRequestPojo.getRandomUser();
        userBaseSteps = new UserBaseSteps();
        responseToken = userBaseSteps.userCreationAndRegistration(userCreationRequestPojo);
        accessToken = responseToken.extract().body().path("accessToken");
        ingredientsBaseSteps = new IngredientsBaseSteps();
        ingredientsResponsePojo = ingredientsBaseSteps.getIngredients();
        ingredients = new ArrayList<>();
        ingredients.add(ingredientsResponsePojo.getData().get(0).getId());
        ingredients.add(ingredientsResponsePojo.getData().get(2).getId());
        ingredients.add(ingredientsResponsePojo.getData().get(4).getId());
        ingredients.add(ingredientsResponsePojo.getData().get(13).getId());
        ingredients.add(ingredientsResponsePojo.getData().get(14).getId());
        ingredients.add(ingredientsResponsePojo.getData().get(0).getId());
        orderRequestPojo = new OrderRequestPojo(ingredients);
        orderBaseSteps = new OrderBaseSteps();

    }

    @After
    public void tearDown() {
        accessToken = responseToken.extract().body().path("accessToken");
        if (accessToken != null) {
            userBaseSteps.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Создание заказа с игредиентами пользователем с авторизацией")
    @Description("Проверка, что возвращается код 200")
    public void creatingOrderWithAuthTest() {
        response = orderBaseSteps.creatingOrderWithAuth(orderRequestPojo, accessToken);
        response.assertThat().body("success", equalTo(true)).and().statusCode(200);
    }

    @Test
    @DisplayName("Создание заказа с игредиентами пользователем без авторизации")
    @Description("Проверка, что возвращается код 200")
    public void creatingOrderWithoutAuthTest() {
        response = orderBaseSteps.creatingOrderWithoutAuth(orderRequestPojo);
        response.assertThat().body("success", equalTo(true)).and().statusCode(200);
    }

    @Test
    @DisplayName("Создание заказа без игредиентов пользователем с авторизацией")
    @Description("Проверка, что возвращается код 403 и корректное тело сообщения")
    public void creatingOrderWithoutFilling() {
        response = orderBaseSteps.creatingOrderWithoutFilling(accessToken);
        response.assertThat().body("success", equalTo(false))
                .and().assertThat().body("message", equalTo("Ingredient ids must be provided"))
                .and().statusCode(400);
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов пользователем с авторизацией")
    @Description("Проверка, что возвращается код 500 и корректное тело сообщения")
    public void creatingOrderWithIncorrectIngredientIdTest() {
        Faker faker = new Faker();
        ingredients.add(faker.rickAndMorty().location());
        response = orderBaseSteps.creatingOrderWithAuth(orderRequestPojo, accessToken).statusCode(500);
    }
}
