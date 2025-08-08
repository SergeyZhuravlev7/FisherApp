package ru.zhuravlev.FisherApp.Validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.zhuravlev.FisherApp.DTOs.UserDTOIn;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class UserDTOInValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(UserDTOIn.class);
    }

    @Override
    public void validate(Object target,Errors errors) {
        UserDTOIn userDTOIn = (UserDTOIn) target;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        if (userDTOIn.getBirthDate() != null) {
            try {
                LocalDate date = LocalDate.parse(userDTOIn.getBirthDate(),formatter);
            } catch (DateTimeParseException exception) {
                errors.rejectValue("birthDate","","Дата рождения не соответствует ДД.ММ.ГГГГ формату.");
            }
            return;
        }
        errors.rejectValue("birthDate","","Дата рождения не может быть пустой.");
    }
}
