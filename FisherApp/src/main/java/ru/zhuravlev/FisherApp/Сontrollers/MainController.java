package ru.zhuravlev.FisherApp.Сontrollers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.zhuravlev.FisherApp.Configuration.Security.UserImpl;
import ru.zhuravlev.FisherApp.DTOs.PostDTO;
import ru.zhuravlev.FisherApp.DTOs.UserDTOOut;
import ru.zhuravlev.FisherApp.Models.Post;
import ru.zhuravlev.FisherApp.Models.User;
import ru.zhuravlev.FisherApp.Services.UserService;
import ru.zhuravlev.FisherApp.Util.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
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
        Optional<User> optionalUser = userService.findByLogin(login);
        if (optionalUser.isPresent()) return modelMapper.map(optionalUser.get(), UserDTOOut.class);
        throw new UserNotFoundException();
    }

    @DeleteMapping("/{login}")
    public ResponseEntity<HttpStatus> deleteUserProfile(@PathVariable String login, Authentication authentication) {
        User user = getUserFromAuth(login, authentication);
        userService.deleteUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{login}/posts")
    public ResponseEntity<HttpStatus> addPost(@PathVariable String login, @RequestBody @Valid PostDTO postDTO, BindingResult bindingResult, Authentication authentication) {
        if (bindingResult.hasErrors()) throw new InvalidPostException(converter.convertToMessage(bindingResult));
        User user = getUserFromAuth(login, authentication);
        userService.addPost(user, modelMapper.map(postDTO, Post.class));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{login}/posts/{postId}")
    public ResponseEntity<HttpStatus> deletePost(@PathVariable String login, @PathVariable int postId, Authentication authentication) {
        User user = getUserFromAuth(login, authentication);
        userService.deletePost(user, postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public User getUserFromAuth(String login, Authentication authentication) {
        if (authentication.getName().equals(login)) {
            return ((UserImpl)authentication.getPrincipal()).getUser();
        }
        throw new UserDoesNotHaveRightsException("Отсутствует доступ к этой странице.");
    }

    @ExceptionHandler
    public ResponseEntity<UserErrorResponse> exceptionHandler(UserNotFoundException ex) {
        return new ResponseEntity<>(new UserErrorResponse(ex.getMessage(), System.currentTimeMillis()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<UserErrorResponse> exceptionHandler(UserDoesNotHaveRightsException ex) {
        return new ResponseEntity<>(new UserErrorResponse(ex.getMessage(), System.currentTimeMillis()), HttpStatus.BAD_REQUEST);
    }
}
