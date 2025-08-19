package ru.zhuravlev.FisherApp.Validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.zhuravlev.FisherApp.DTOs.UserDTOFilling;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class UserDTOFillingValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(UserDTOFilling.class);
    }

    @Override
    public void validate(Object target,Errors errors) {
        checkBirthdate(target,errors);
        checkGender(target,errors);
    }

    private void checkBirthdate(Object target,Errors errors) {
        UserDTOFilling userDTOFilling = (UserDTOFilling) target;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        if (userDTOFilling.getBirthDate() != null) {
            try {
                LocalDate date = LocalDate.parse(userDTOFilling.getBirthDate(),formatter);
            } catch (DateTimeParseException exception) {
                errors.rejectValue("birthDate","","Дата рождения не соответствует ДД.ММ.ГГГГ формату.");
            }
            return;
        }
        errors.rejectValue("birthDate","","Дата рождения не может быть пустой.");
    }

    private void checkGender(Object target,Errors errors) {
        UserDTOFilling userDTOFilling = (UserDTOFilling) target;
        String gender = userDTOFilling.getGender();
        if (gender == null) {
            errors.rejectValue("gender","","Пол может быть мужским или женским.");
            return;
        }
        gender = gender.toLowerCase();
        if (! gender.equals("мужской") && ! gender.equals("женский"))
            errors.rejectValue("gender","","Пол может быть мужским или женским.");
    }
}
