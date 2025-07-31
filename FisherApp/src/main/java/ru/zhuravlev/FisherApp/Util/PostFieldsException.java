package ru.zhuravlev.FisherApp.Util;

import java.util.HashMap;

public class PostFieldsException extends RuntimeException {

    HashMap<String,String> errors;

    public PostFieldsException(HashMap<String, String> errors) {
        this.errors = errors;
    }

    public HashMap<String, String> getErrors() {
        return errors;
    }
}
