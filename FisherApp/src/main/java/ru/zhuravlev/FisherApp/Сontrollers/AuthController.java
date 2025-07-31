package ru.zhuravlev.FisherApp.Сontrollers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.zhuravlev.FisherApp.Configuration.Security.AuthManager;
import ru.zhuravlev.FisherApp.DTOs.LoginDTO;
import ru.zhuravlev.FisherApp.DTOs.UserDTOIn;
import ru.zhuravlev.FisherApp.Models.User;
import ru.zhuravlev.FisherApp.Services.UserService;
import ru.zhuravlev.FisherApp.Util.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final BindingResultConverter converter;
    private final ModelMapper modelMapper;
    private final AuthManager authenticationManager;

    @Autowired
    public AuthController(UserService userService, BindingResultConverter converter, ModelMapper modelMapper,
                          PasswordEncoder passwordEncoder, AuthManager authenticationManager) {
        this.userService = userService;
        this.converter = converter;
        this.modelMapper = modelMapper;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> registration(@RequestBody @Valid UserDTOIn userDTOIn, BindingResult bindingResult) {
        if (userService.findByLogin(userDTOIn.getLogin()).isPresent()) throw new UserAlreadyExistException();
        if (bindingResult.hasErrors()) throw new UserFieldsException(converter.convertToMessage(bindingResult));
        User user = modelMapper.map(userDTOIn, User.class);
        userService.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<HttpStatus> login(@RequestBody @Valid LoginDTO loginDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new UserFieldsException(converter.convertToMessage(bindingResult));
        try {
            Authentication authentication = authenticationManager.authenticate
                    (new UsernamePasswordAuthenticationToken(loginDTO.getLogin(), loginDTO.getPassword()));
        }
        catch (BadCredentialsException e) {
            throw new BadCredentialsException(e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<HashMap<String,String>> exceptionHandler(UserFieldsException ex) {
        return new ResponseEntity<>(ex.getErrors(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<UserErrorResponse>  exceptionHandler(UserAlreadyExistException ex) {
        return new ResponseEntity<>(new UserErrorResponse(ex.getMessage(), System.currentTimeMillis()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<UserErrorResponse>  exceptionHandler(BadCredentialsException ex) {
        return new ResponseEntity<>(new UserErrorResponse(ex.getMessage(), System.currentTimeMillis()), HttpStatus.BAD_REQUEST);
    }
}
