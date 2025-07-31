package ru.zhuravlev.FisherApp.Util;

import java.util.HashMap;

public class UserFieldsException extends RuntimeException {

    HashMap<String,String> errors;

    public UserFieldsException(HashMap<String, String> errors) {
      this.errors = errors;
    }

    public HashMap<String, String> getErrors() {
      return errors;
    }
}
