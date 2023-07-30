package com.example.User;

import com.example.Client;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserClient extends Client {
    private static final String PATH="api/auth/";

    @Step("Создание пользователя")
    public ValidatableResponse createUser(UserCreate user){
        return given()
                .spec(spec())
                .body(user)
                .when()
                .post(PATH + "register")
                .then().log().all();
    }

    @Step("Логин пользователя")
    public ValidatableResponse loginUser(UserLogin login){
        return given()
                .spec(spec())
                .body(login)
                .when()
                .post(PATH + "login")
                .then().log().all();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse deleteUser(String accessToken){
        return given()
                .spec(spec())
                .header("Authorization", accessToken)
                .when()
                .delete(PATH + "user")
                .then().log().all();
    }

    @Step("Изменения данных у авторизованного пользователя")
    public ValidatableResponse updateAuthorizedUserData(String accessToken, UserCreate data){
        return given()
                .spec(spec())
                .header("Authorization", accessToken)
                .body(data)
                .when()
                .patch(PATH + "user")
                .then().log().all();
    }

    @Step("Изменения данных у неавторизованного пользователя")
    public ValidatableResponse updateUnauthorizedUserData(UserCreate data){
        return given()
                .spec(spec())
                .body(data)
                .when()
                .patch(PATH + "user")
                .then().log().all();
    }
}
