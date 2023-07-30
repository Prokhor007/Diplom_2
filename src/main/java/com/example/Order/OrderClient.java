package com.example.Order;

import com.example.Client;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient extends Client {
    private static final String PATH_ORDERS = "/api/orders/";
    private static final String PATH_INGREDIENTS = "/api/ingredients";

    @Step("Создание заказа авторизованным пользователем")
    public ValidatableResponse createOrderAuthorizedUser(String accessToken, OrderCreate order){
        return given()
                .spec(spec())
                .header("Authorization", accessToken)
                .body(order)
                .when()
                .post(PATH_ORDERS)
                .then().log().all();
    }

    @Step("Создание заказа неавторизованным пользователем")
    public ValidatableResponse createOrderUnauthorizedUser(OrderCreate order){
        return given()
                .spec(spec())
                .body(order)
                .when()
                .post(PATH_ORDERS)
                .then().log().all();
    }

    @Step("Получение заказ авторизованным пользователем")
    public ValidatableResponse getOrdersAuthorizedUser(String accessToken){
        return given()
                .spec(spec())
                .header("Authorization", accessToken)
                .when()
                .get(PATH_ORDERS)
                .then().log().all();
    }

    @Step("Получение заказа неавторизованным пользователем")
    public ValidatableResponse getOrdersUnauthorizedUser(){
        return given()
                .spec(spec())
                .when()
                .get(PATH_ORDERS)
                .then().log().all();
    }

    @Step("Получение списка ингредиентов")
    public ValidatableResponse getIngredients() {
        return given()
                .spec(spec())
                .get(PATH_INGREDIENTS)
                .then().log().all();
    }
}