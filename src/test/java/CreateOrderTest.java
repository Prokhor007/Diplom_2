import com.example.Order.OrderClient;
import com.example.Order.OrderCreate;
import com.example.User.UserClient;
import com.example.User.UserCreate;
import com.example.User.UserGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.Assert.*;

public class CreateOrderTest {
    private UserCreate user;
    private UserClient userClient;
    private List<String> ingredients;
    private OrderCreate order;
    private OrderClient orderClient;
    private String accessToken;


    @Before
    public void setUp() {
        user = UserGenerator.fullFieldsUser();
        userClient = new UserClient();
        orderClient = new OrderClient();
        ValidatableResponse responseCreate = userClient.createUser(user);
        accessToken = responseCreate.extract().path("accessToken").toString();
    }

    @After
    public void cleanUp(){
        if (accessToken !=null)
            userClient.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    @Description("Проверяем возможность создания заказа авторизованным пользователем")
    public void createOrderWithAuthorizationTest() {
        ingredients = orderClient.getIngredients().extract().path("data._id");
        order = new OrderCreate(ingredients);
        ValidatableResponse responseOrder = orderClient.createOrderAuthorizedUser(accessToken, order);
        int statusCode = responseOrder.extract().statusCode();
        boolean messageResponse = responseOrder.extract().path("success");
        assertEquals(200, statusCode);
        assertTrue(messageResponse);
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Проверяем возможность создания заказа неавторизованным пользователем")
    public void createOrderWithoutAuthorizationTest() {
        ingredients = orderClient.getIngredients().extract().path("data._id");
        order = new OrderCreate(ingredients);
        ValidatableResponse responseOrder = orderClient.createOrderUnauthorizedUser(order);
        int statusCode = responseOrder.extract().statusCode();
        boolean messageResponse = responseOrder.extract().path("success");
        assertEquals(200, statusCode);
        assertTrue(messageResponse);
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами")
    @Description("Проверяем возможность создания заказа с ингредиентами")
    public void createOrderWithIngredientsTest() {
        ingredients = orderClient.getIngredients().extract().path("data._id");
        order = new OrderCreate(ingredients);
        ValidatableResponse responseOrder = orderClient.createOrderAuthorizedUser(accessToken, order);
        int statusCode = responseOrder.extract().statusCode();
        boolean booleanResponse = responseOrder.extract().path("success");
        assertEquals(200, statusCode);
        assertTrue(booleanResponse);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Проверяем невозможность создания заказа без ингредиентов")
    public void createOrderWithoutIngredientsTest() {
        ingredients = emptyList();
        OrderCreate order = new OrderCreate(ingredients);
        ValidatableResponse responseOrder = orderClient.createOrderAuthorizedUser(accessToken, order);
        int statusCode = responseOrder.extract().statusCode();
        boolean booleanResponse = responseOrder.extract().path("success");
        String messageResponse = responseOrder.extract().path("message");
        assertEquals(400, statusCode);
        assertFalse(booleanResponse);
        assertEquals("Ingredient ids must be provided", messageResponse);
    }

    @Test
    @DisplayName("Создание заказа с некорректным хэшом")
    @Description("Проверяем невозможность создания заказа с некорректным хэшом")
    public void createOrderWithIncorrectHashTest() {
        ingredients = List.of("incorrectHash");
        OrderCreate order = new OrderCreate(ingredients);
        ValidatableResponse responseOrder = orderClient.createOrderAuthorizedUser(accessToken, order);
        int statusCode = responseOrder.extract().statusCode();
        assertEquals(500, statusCode);
    }
}