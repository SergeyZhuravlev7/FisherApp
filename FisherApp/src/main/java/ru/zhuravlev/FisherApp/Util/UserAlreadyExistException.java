package ru.zhuravlev.FisherApp.Util;

public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException() {
        super("Пользователь с таким логином уже зарегистрирован.");
    }
}
