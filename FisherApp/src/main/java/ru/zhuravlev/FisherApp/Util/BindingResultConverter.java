package ru.zhuravlev.FisherApp.Util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.HashMap;

@Component
public class BindingResultConverter {

    public HashMap<String, String> convertToMessage(BindingResult bindingResult) {
        HashMap<String, String> message = new HashMap<>();
        for (FieldError error : bindingResult.getFieldErrors()) {
            message.put(error.getField(), error.getDefaultMessage());
        }
        return message;
    }
}
