package ru.zhuravlev.FisherApp.Сontrollers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.zhuravlev.FisherApp.DTOs.UserDTOIn;
import ru.zhuravlev.FisherApp.Models.User;
import ru.zhuravlev.FisherApp.Services.UserService;
import ru.zhuravlev.FisherApp.Util.BindingResultConverter;
import ru.zhuravlev.FisherApp.Util.InvalidUserException;
import ru.zhuravlev.FisherApp.Util.UserAlreadyExistException;
import ru.zhuravlev.FisherApp.Util.UserErrorResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final BindingResultConverter converter;
    private final ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(UserService userService, BindingResultConverter converter, ModelMapper modelMapper) {
        this.userService = userService;
        this.converter = converter;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> registration(@RequestBody @Valid UserDTOIn userDTOIn, BindingResult bindingResult) {
        if (userService.findByLogin(userDTOIn.getLogin()).isPresent()) throw new UserAlreadyExistException();

        if (bindingResult.hasErrors()) throw new InvalidUserException(converter.convertToMessage(bindingResult));

        User user = modelMapper.map(userDTOIn, User.class);
        userService.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler
    public UserErrorResponse exceptionHandler(InvalidUserException ex) {
        return new UserErrorResponse(ex.getMessage(), System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public UserErrorResponse exceptionHandler(UserAlreadyExistException ex) {
        return new UserErrorResponse(ex.getMessage(), System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
    }
}
