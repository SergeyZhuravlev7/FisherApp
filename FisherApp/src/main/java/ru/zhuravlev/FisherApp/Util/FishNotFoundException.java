package ru.zhuravlev.FisherApp.Util;

public class FishNotFoundException extends RuntimeException {
    public FishNotFoundException() {
        super("Такой рыбы не существует.");
    }
}
