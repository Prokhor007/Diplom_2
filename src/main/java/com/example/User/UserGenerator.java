package com.example.User;

import io.qameta.allure.Step;
import org.apache.commons.lang3.RandomStringUtils;

public class UserGenerator {
    /** Пользователь со всеми полями **/
    public static UserCreate fullFieldsUser() {
        String email = RandomStringUtils.randomAlphanumeric(10) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphanumeric(10);
        String firstName = RandomStringUtils.randomAlphabetic(10);
        return new UserCreate(email, password, firstName);
    }

    /** Пользователь без логина **/
    public static UserCreate withoutLoginUser() {
        String password = RandomStringUtils.randomAlphanumeric(10);
        String firstName = RandomStringUtils.randomAlphabetic(10);
        return new UserCreate(null, password, firstName);
    }

    /** Пользователь без пароля **/
    public static UserCreate withoutPasswordUser() {
        String email = RandomStringUtils.randomAlphanumeric(10) + "@yandex.ru";
        String firstName = RandomStringUtils.randomAlphabetic(10);
        return new UserCreate(email, null, firstName);
    }

    /** Пользователь без имени **/
    public static UserCreate withoutNameUser() {
        String email = RandomStringUtils.randomAlphanumeric(10) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(10);
        return new UserCreate(email, password, null);
    }

    /** Пользователь с некорректными данными **/
    public static UserCreate incorrectUser() {
        String email = RandomStringUtils.randomAlphabetic(10) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(10);
        return new UserCreate(email, password, "");
    }

    /** Данные для редактирования **/
    public static UserCreate editFieldsUser() {
        String email = RandomStringUtils.randomAlphanumeric(10) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphanumeric(10);
        String firstName = RandomStringUtils.randomAlphabetic(10);
        return new UserCreate(email, password, firstName);
    }
}
