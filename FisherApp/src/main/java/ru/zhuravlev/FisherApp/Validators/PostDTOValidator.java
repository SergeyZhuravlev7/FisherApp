package ru.zhuravlev.FisherApp.Validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.zhuravlev.FisherApp.DTOs.PostDTO;
import ru.zhuravlev.FisherApp.Models.Fish;

@Component
public class PostDTOValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(PostDTO.class);
    }

    @Override
    public void validate(Object target,Errors errors) {
        PostDTO postDTO = (PostDTO) target;
        if (!Fish.getFishNames().contains(postDTO.getFish())) errors.rejectValue("fish", "", "Недопустимое название для рыбы.");
        if (postDTO.getFish_weight().doubleValue() <= 0.01) errors.rejectValue("fish_weight", "", "Вес рыбы должен быть больше 0.01");
    }
}
