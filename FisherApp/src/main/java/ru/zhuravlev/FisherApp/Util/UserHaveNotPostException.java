package ru.zhuravlev.FisherApp.Util;

public class UserHaveNotPostException extends RuntimeException {
    public UserHaveNotPostException() {
        super("У пользователя отсутствует пост с таким id.");
    }
}
