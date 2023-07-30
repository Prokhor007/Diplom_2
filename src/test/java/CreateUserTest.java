import com.example.User.UserClient;
import com.example.User.UserCreate;
import com.example.User.UserGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CreateUserTest {
    private UserCreate user;
    private UserClient userClient;
    private UserCreate userWithoutLogin;
    private UserCreate userWithoutPassword;
    private UserCreate userWithoutName;
    private UserCreate sameUser;
    private  String accessToken;

    @Before
    public void setUp() {
        user = UserGenerator.fullFieldsUser();
        userClient = new UserClient();
        userWithoutLogin = UserGenerator.withoutLoginUser();
        userWithoutPassword = UserGenerator.withoutPasswordUser();
        userWithoutName = UserGenerator.withoutNameUser();
        sameUser = new UserCreate(user.getEmail(), user.getPassword(), user.getName());
    }

    @After
    public void cleanUp(){
        if (accessToken !=null)
        userClient.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Создание нового пользователя")
    @Description("Проверяем, что запрос возвращает правильные код ответ и тело success: true")
    public void userCreated(){
        ValidatableResponse responseCreate = userClient.createUser(user);
        int statusCode = responseCreate.extract().statusCode();
        boolean messageResponse = responseCreate.extract().path("success");
        assertEquals(200, statusCode);
        assertTrue(messageResponse);
        accessToken = responseCreate.extract().path("accessToken").toString();
    }

    @Test
    @DisplayName("Создание пользователя без логина")
    @Description("Проверяем, что нельзя создать пользователя без логина")
    public void userWithoutLogin(){
        ValidatableResponse responseCreate = userClient.createUser(userWithoutLogin);
        int statusCode = responseCreate.extract().statusCode();
        String messageResponse = responseCreate.extract().path("message");
        assertEquals(403, statusCode);
        assertEquals("Email, password and name are required fields", messageResponse);
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    @Description("Проверяем, что нельзя создать пользователя без пароля")
    public void userWithoutPassword(){
        ValidatableResponse responseCreate = userClient.createUser(userWithoutPassword);
        int statusCode = responseCreate.extract().statusCode();
        String messageResponse = responseCreate.extract().path("message");
        assertEquals(403, statusCode);
        assertEquals("Email, password and name are required fields", messageResponse);
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    @Description("Проверяем, что нельзя создать пользователя без имени")
    public void userWithoutName(){
        ValidatableResponse responseCreate = userClient.createUser(userWithoutName);
        int statusCode = responseCreate.extract().statusCode();
        String messageResponse = responseCreate.extract().path("message");
        assertEquals(403, statusCode);
        assertEquals("Email, password and name are required fields", messageResponse);
    }
    @Test
    @DisplayName("Создание двух одинаковых пользователей")
    @Description("Проверяем, что нельзя создать двух одинаковых пользователей")
    public void userCreatedTwice(){
        ValidatableResponse responseCreate1 = userClient.createUser(user);
        accessToken = responseCreate1.extract().path("accessToken").toString();

        ValidatableResponse responseCreate2 = userClient.createUser(sameUser);
        int statusCode = responseCreate2.extract().statusCode();
        String messageResponse = responseCreate2.extract().path("message");
        assertEquals(403, statusCode);
        assertEquals("User already exists", messageResponse);
    }
}