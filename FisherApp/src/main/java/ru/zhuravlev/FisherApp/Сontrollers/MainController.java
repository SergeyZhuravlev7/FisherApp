package ru.zhuravlev.FisherApp.Сontrollers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.zhuravlev.FisherApp.Configuration.Security.UserImpl;
import ru.zhuravlev.FisherApp.DTOs.UserDTOOut;
import ru.zhuravlev.FisherApp.Models.User;
import ru.zhuravlev.FisherApp.Services.UserService;
import ru.zhuravlev.FisherApp.Util.BindingResultConverter;
import ru.zhuravlev.FisherApp.Util.UserErrorResponse;
import ru.zhuravlev.FisherApp.Util.UserNotFoundException;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class MainController {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final BindingResultConverter converter;

    @Autowired
    public MainController(UserService userService, ModelMapper modelMapper, BindingResultConverter converter) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.converter = converter;
    }

    @GetMapping("/{login}")
    public UserDTOOut getUserProfile(@PathVariable String login) {
        Optional<User> user = userService.findByLogin(login);
        if (user.isPresent()) return modelMapper.map(user.get(), UserDTOOut.class);
        throw new UserNotFoundException();
    }

    @ExceptionHandler
    public UserErrorResponse exceptionHandler(UserNotFoundException ex) {
        return new UserErrorResponse(ex.getMessage(), System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
    }
}
