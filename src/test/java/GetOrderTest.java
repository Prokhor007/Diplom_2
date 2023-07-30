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

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GetOrderTest {
    private OrderClient orderClient;
    private UserCreate user;
    private UserClient userClient;
    private List<String> ingredients;
    private OrderCreate order;
    private  String accessToken;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        userClient = new UserClient();
        user = UserGenerator.fullFieldsUser();
        ValidatableResponse responseCreate = userClient.createUser(user);
        accessToken = responseCreate.extract().path("accessToken").toString();
        ingredients = orderClient.getIngredients().extract().path("data._id");
        order = new OrderCreate(ingredients);
        orderClient.createOrderAuthorizedUser(accessToken, order);
    }

    @After
    public void cleanUp(){
        if (accessToken !=null)
            userClient.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Получение списка заказов авторизованным пользователем")
    @Description("Проверяем возможность получения списка заказов у авторизованного пользователя")
    public void getOrdersAuthorizedUser(){
        ValidatableResponse responseGetOrder = orderClient.getOrdersAuthorizedUser(accessToken);
        int statusCode = responseGetOrder.extract().statusCode();
        boolean messageResponse = responseGetOrder.extract().path("success");
        assertEquals(200, statusCode);
        assertTrue(messageResponse);
        ArrayList list = responseGetOrder.extract().path("orders");
        assertThat(list, notNullValue());
    }

    @Test
    @DisplayName("Получение списка заказов неавторизованным пользователем")
    @Description("Проверяем невозможность получения списка заказов у неавторизованного пользователя")
    public void getOrdersUnauthorizedUser(){
        ValidatableResponse responseGetOrder = orderClient.getOrdersUnauthorizedUser();
        int statusCode = responseGetOrder.extract().statusCode();
        String messageResponse = responseGetOrder.extract().path("message");
        assertEquals(401, statusCode);
        assertEquals("You should be authorised", messageResponse);
    }
}
