package ru.zhuravlev.FisherApp.Util;

public class InvalidPostException extends RuntimeException {
    public InvalidPostException(String message) {
        super(message);
    }
}
