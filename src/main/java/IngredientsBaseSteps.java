import io.qameta.allure.Step;

import static io.restassured.RestAssured.given;

public class IngredientsBaseSteps extends BaseClass {
    @Step("Получение данных об ингредиентах")
    public IngredientsResponsePojo getIngredients() {
        return given()
                .spec(getBaseSpec())
                .get(Endpoints.GET_INGREDIENTS_INFO.endpoint)
                .as(IngredientsResponsePojo.class);
    }
}
