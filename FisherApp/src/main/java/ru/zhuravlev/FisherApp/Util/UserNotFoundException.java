package ru.zhuravlev.FisherApp.Util;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("Пользователь с таким логином не найден.");
    }


}
