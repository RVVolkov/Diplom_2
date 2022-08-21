import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class GetOrderListTests {
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
        ingredients.add(ingredientsResponsePojo.getData().get(0).get_id());
        ingredients.add(ingredientsResponsePojo.getData().get(2).get_id());
        ingredients.add(ingredientsResponsePojo.getData().get(4).get_id());
        ingredients.add(ingredientsResponsePojo.getData().get(13).get_id());
        ingredients.add(ingredientsResponsePojo.getData().get(14).get_id());
        ingredients.add(ingredientsResponsePojo.getData().get(0).get_id());
        orderRequestPojo = new OrderRequestPojo(ingredients);
        orderBaseSteps = new OrderBaseSteps();
        orderBaseSteps.creatingOrderWithAuth(orderRequestPojo, accessToken);

    }

    @After
    public void tearDown() {
        accessToken = responseToken.extract().body().path("accessToken");
        if (accessToken != null) {
            userBaseSteps.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Получение списка заказов конкретного пользователя с авторизацией")
    @Description("Проверка, что возвращается код 200")
    public void creatingOrderWithAuthTest() {
        response = orderBaseSteps.getOrderListWithAuth(orderRequestPojo, accessToken);
        response.assertThat().body("success", equalTo(true))
                .and().assertThat().body("orders", notNullValue())
                .and().assertThat().body("total", notNullValue())
                .and().assertThat().body("totalToday", notNullValue())
                .and().statusCode(200);
    }

    @Test
    @DisplayName("Получение списка заказов конкретного пользователя без авторизации")
    @Description("Проверка, что возвращается код 401 и корректное тело сообщения")
    public void creatingOrderWithoutAuthTest() {
        response = orderBaseSteps.getOrderListWithoutAuth(orderRequestPojo);
        response.assertThat().body("success", equalTo(false))
                .and().assertThat().body("message", equalTo("You should be authorised"))
                .and().statusCode(401);
    }
}
