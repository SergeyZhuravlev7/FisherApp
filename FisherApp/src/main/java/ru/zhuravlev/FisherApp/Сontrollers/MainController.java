package ru.zhuravlev.FisherApp.Сontrollers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.zhuravlev.FisherApp.DTOs.PostDTO;
import ru.zhuravlev.FisherApp.DTOs.UserDTOOut;
import ru.zhuravlev.FisherApp.Models.Post;
import ru.zhuravlev.FisherApp.Models.User;
import ru.zhuravlev.FisherApp.Services.UserService;
import ru.zhuravlev.FisherApp.Util.*;
import ru.zhuravlev.FisherApp.Validators.PostDTOValidator;

import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping ("/api/users")
public class MainController {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final BindingResultConverter converter;
    private final PostDTOValidator postDTOValidator;

    @Autowired
    public MainController(UserService userService,ModelMapper modelMapper,BindingResultConverter converter,PostDTOValidator postDTOValidator) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.converter = converter;
        this.postDTOValidator = postDTOValidator;
    }

    @GetMapping ("/{login}")
    public UserDTOOut getUserProfile(@PathVariable String login) {
        Optional<User> optionalUser = userService.findByLogin(login);
        if (optionalUser.isPresent()) return modelMapper.map(optionalUser.get(),UserDTOOut.class);
        throw new UserNotFoundException();
    }

    @PreAuthorize ("#login == authentication.getName")
    @DeleteMapping ("/{login}")
    public ResponseEntity<HttpStatus> deleteUserProfile(@PathVariable String login) {
        userService.deleteUser(login);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize ("#login == authentication.getName")
    @PostMapping ("/{login}/posts")
    public ResponseEntity<HttpStatus> addPost(@PathVariable String login,@RequestBody @Valid PostDTO postDTO,BindingResult bindingResult) {
        postDTOValidator.validate(postDTO,bindingResult);
        if (bindingResult.hasErrors()) throw new PostFieldsException(converter.convertToMessage(bindingResult));
        userService.addPost(login,modelMapper.map(postDTO,Post.class));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize ("#login == authentication.getName")
    @DeleteMapping ("/{login}/posts/{postId}")
    public ResponseEntity<HttpStatus> deletePost(@PathVariable String login,@PathVariable int postId) {
        userService.deletePost(login,postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<UserErrorResponse> exceptionHandler(UserNotFoundException ex) {
        return new ResponseEntity<>(new UserErrorResponse(ex.getMessage(),System.currentTimeMillis()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<HashMap<String, String>> exceptionHandler(PostFieldsException ex) {
        return new ResponseEntity<>(ex.getErrors(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<PostErrorResponse> exceptionHandler(BadCredentialsException ex) {
        return new ResponseEntity<>(new PostErrorResponse(ex.getMessage(),System.currentTimeMillis()),HttpStatus.BAD_REQUEST);
    }

}
