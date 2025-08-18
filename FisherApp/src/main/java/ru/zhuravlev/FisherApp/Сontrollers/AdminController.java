package ru.zhuravlev.FisherApp.Сontrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.zhuravlev.FisherApp.Services.PostService;
import ru.zhuravlev.FisherApp.Services.UserService;

@RestController
@RequestMapping ("/api/admin")
public class AdminController {

    private final UserService userService;
    private final PostService postService;

    @Autowired
    public AdminController(UserService userService,PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @DeleteMapping ("/users/{login}")
    @PreAuthorize ("hasAuthority('ADMIN')")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable String login) {
        userService.deleteUser(login);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping ("/posts/{id}")
    @PreAuthorize ("hasAuthority('ADMIN')")
    public ResponseEntity<HttpStatus> deletePost(@PathVariable String id) {
        try {
            postService.delete(Integer.parseInt(id));
        } catch (NumberFormatException ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
