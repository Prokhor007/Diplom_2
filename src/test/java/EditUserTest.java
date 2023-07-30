import com.example.User.UserClient;
import com.example.User.UserCreate;
import com.example.User.UserGenerator;
import com.example.User.UserLogin;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EditUserTest {
    private UserCreate user;
    private UserCreate userEdit;
    private UserClient userClient;
    private  String accessToken;

    @Before
    public void setUp() {
        user = UserGenerator.fullFieldsUser();
        userClient = new UserClient();
        userEdit = UserGenerator.editFieldsUser();
        ValidatableResponse responseCreate = userClient.createUser(user);
        accessToken = responseCreate.extract().path("accessToken").toString();
    }

    @After
    public void cleanUp(){
        if (accessToken !=null)
            userClient.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Изменение данных у авторизованного пользователя")
    @Description("Проверяем успешное редактирование пользователя")
    public  void userEditAuthorized(){
        userClient.loginUser(UserLogin.from(user));
        ValidatableResponse responseEdit = userClient.updateAuthorizedUserData(accessToken, userEdit);
        int statusCode = responseEdit.extract().statusCode();
        boolean messageResponse = responseEdit.extract().path("success");
        assertEquals(200, statusCode);
        assertTrue(messageResponse);
    }

    @Test
    @DisplayName("Изменение данных у неавторизованного пользователя")
    @Description("Проверяем невозможность редактирования неавторизованного пользователя")
    public  void userEditUnauthorized(){
        ValidatableResponse responseEdit = userClient.updateUnauthorizedUserData(userEdit);
        int statusCode = responseEdit.extract().statusCode();
        String messageResponse = responseEdit.extract().path("message");
        assertEquals(401, statusCode);
        assertEquals("You should be authorised", messageResponse);
    }
}
