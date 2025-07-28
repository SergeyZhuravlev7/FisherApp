package ru.zhuravlev.FisherApp.Util;

public class UserDoesNotHaveRightsException extends RuntimeException {
    public UserDoesNotHaveRightsException(String message) {
        super(message);
    }
}
