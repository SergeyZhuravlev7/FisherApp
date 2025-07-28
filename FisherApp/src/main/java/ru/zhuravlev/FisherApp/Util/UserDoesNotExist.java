package ru.zhuravlev.FisherApp.Util;

public class UserDoesNotExist extends RuntimeException {
      public UserDoesNotExist(String message) {
    super(message);
  }
}
