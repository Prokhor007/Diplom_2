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

public class LoginUserTest {
    private UserCreate user;
    private UserClient userClient;
    private UserLogin userCredsWithoutLogin;
    private UserLogin userCredsWithoutPassword;
    private UserLogin userIncorrectCreds;
    private  String accessToken;

    @Before
    public void setUp() {
        user = UserGenerator.fullFieldsUser();
        userClient = new UserClient();
        userCredsWithoutLogin = new UserLogin(user.getEmail(), "");
        userCredsWithoutPassword = new UserLogin("", user.getPassword());
        userIncorrectCreds = UserLogin.from(UserGenerator.incorrectUser());
    }

    @After
    public void cleanUp(){
        if (accessToken !=null)
            userClient.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Успешная авторизация пользователя")
    @Description("Проверяем успешный вход в систему с помощью емейла и пароля")
    public  void userLogin(){
        userClient.createUser(user);
        ValidatableResponse responseLogin = userClient.loginUser(UserLogin.from(user));
        int statusCode = responseLogin.extract().statusCode();
        boolean messageResponse = responseLogin.extract().path("success");
        assertEquals(200, statusCode);
        assertTrue(messageResponse);
        accessToken = responseLogin.extract().path("accessToken").toString();
    }

    @Test
    @DisplayName("Авторизации без указания емейла")
    @Description("Проверяем невозможность входа в систему без указания емейла")
    public  void  userCredsWithoutLogin(){
        userClient.createUser(user);
        ValidatableResponse responseLogin = userClient.loginUser(userCredsWithoutLogin);
        int statusCode = responseLogin.extract().statusCode();
        String messageResponse = responseLogin.extract().path("message");
        assertEquals(401, statusCode);
        assertEquals("email or password are incorrect", messageResponse);
    }

    @Test
    @DisplayName("Авторизации без указания пароля")
    @Description("Проверяем невозможность входа в систему без указания пароля")
    public  void  userCredsWithoutPassword(){
        userClient.createUser(user);
        ValidatableResponse responseLogin = userClient.loginUser(userCredsWithoutPassword);
        int statusCode = responseLogin.extract().statusCode();
        String messageResponse = responseLogin.extract().path("message");
        assertEquals(401, statusCode);
        assertEquals("email or password are incorrect", messageResponse);
    }

    @Test
    @DisplayName("Некорректные данные авторизации")
    @Description("Проверяем невозможность входа в систему с некорректными данными авторизации")
    public  void  userIncorrectCreds(){
        ValidatableResponse responseLogin = userClient.loginUser(userIncorrectCreds);
        int statusCode = responseLogin.extract().statusCode();
        String messageResponse = responseLogin.extract().path("message");
        assertEquals(401, statusCode);
        assertEquals("email or password are incorrect", messageResponse);
    }
}