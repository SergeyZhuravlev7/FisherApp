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
import ru.zhuravlev.FisherApp.Configuration.Security.CustomUserDetails;
import ru.zhuravlev.FisherApp.DTOs.LoginDTO;
import ru.zhuravlev.FisherApp.DTOs.TokenDTO;
import ru.zhuravlev.FisherApp.DTOs.UserDTOIn;
import ru.zhuravlev.FisherApp.Models.User;
import ru.zhuravlev.FisherApp.Services.JWTService;
import ru.zhuravlev.FisherApp.Services.UserService;
import ru.zhuravlev.FisherApp.Util.*;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final BindingResultConverter converter;
    private final ModelMapper modelMapper;
    private final AuthManager authenticationManager;
    private final JWTService jwtService;

    @Autowired
    public AuthController(UserService userService, BindingResultConverter converter, ModelMapper modelMapper,
                          PasswordEncoder passwordEncoder, AuthManager authenticationManager, JWTService jwtService) {
        this.userService = userService;
        this.converter = converter;
        this.modelMapper = modelMapper;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
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
    public ResponseEntity<Map<String, String>> login(@RequestBody @Valid LoginDTO loginDTO,BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new UserFieldsException(converter.convertToMessage(bindingResult));
        Map<String,String> keys = new TreeMap<>();
        try {
            Authentication authentication = authenticationManager.authenticate
                    (new UsernamePasswordAuthenticationToken(loginDTO.getLogin(), loginDTO.getPassword()));
            keys.put("AccessToken", jwtService.createAccessToken((CustomUserDetails) authentication.getPrincipal()));
            keys.put("RefreshToken", jwtService.createRefreshToken((CustomUserDetails) authentication.getPrincipal()));
        }
        catch (BadCredentialsException e) {
            throw new BadCredentialsException(e.getMessage());
        }
        return new ResponseEntity<>(keys, HttpStatus.OK);
    }

    @PostMapping("/token")
    public ResponseEntity<Map<String, String>> refreshAccessToken(@RequestBody TokenDTO token) {
        if (jwtService.isValidRefreshToken(token.getRefreshToken()))

            return new ResponseEntity<>(Map.of("AccessToken",jwtService.createAccessToken(token.getRefreshToken())), HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
