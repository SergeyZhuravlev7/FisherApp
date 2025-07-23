package ru.zhuravlev.FisherApp.Util;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

@Component
public class BindingResultConverter {

    public String convertToMessage(BindingResult bindingResult) {
        StringBuilder sb = new StringBuilder();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            sb.append(fieldError.getField()).append(" - ").append(fieldError.getDefaultMessage()).append("\n");
        }

        return sb.toString();
    }
}
