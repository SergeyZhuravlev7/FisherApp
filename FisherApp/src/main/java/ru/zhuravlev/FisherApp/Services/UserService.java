package ru.zhuravlev.FisherApp.Services;


/* author--> 
Sergey Zhuravlev
*/

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.zhuravlev.FisherApp.DTOs.UserDTOFilling;
import ru.zhuravlev.FisherApp.Models.Gender;
import ru.zhuravlev.FisherApp.Models.Post;
import ru.zhuravlev.FisherApp.Models.User;
import ru.zhuravlev.FisherApp.Repos.UserRepository;
import ru.zhuravlev.FisherApp.Util.UserNotFoundException;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PostService postService;
    private final LikeService likeService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public UserService(UserRepository userRepository,PostService postService,LikeService likeService) {
        this.userRepository = userRepository;
        this.postService = postService;
        this.likeService = likeService;
    }

    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Transactional
    public User loadUser(String login) {
        User loadedUser = findByLogin(login).get();
        loadedUser.setPosts(postService.loadPosts(loadedUser.getId()));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (! (authentication instanceof AnonymousAuthenticationToken)) {
            User authencicatedUser = findByLogin(authentication.getName()).get();
            loadedUser.setPosts(postService.loadUserLikes(authencicatedUser.getId(),loadedUser.getPosts()));
        }

        Hibernate.initialize(loadedUser.getAchievements());
        return loadedUser;
    }

    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void addPost(String login,Post post) {
        User user = findByLogin(login).get();
        Hibernate.initialize(user.getPosts());
        user.addPost(post);
    }

    @Transactional
    public void deletePost(String login,int postId) {
        Optional<Post> optionalPost = postService.findById(postId);
        if (optionalPost.isEmpty()) return;
        Post post = optionalPost.get();
        User user = findByLogin(login).get();
        user.deletePost(post);
        post.setUser(null);
    }

    @Transactional
    public void deleteUser(String login) {
        User user = findByLogin(login).get();
        Hibernate.initialize(user.getPosts());
        for (Post p : user.getPosts()) p.setUser(null);
        userRepository.delete(user);
    }

    @Transactional
    public void fillUser(String login,UserDTOFilling userDTOFilling) {
        User user = findByLogin(login).get();
        user.setName(userDTOFilling.getName());
        user.setBirthDate(userDTOFilling.getBirthDate());
        String gender = userDTOFilling.getGender().toLowerCase();
        user.setGender(gender.equals("мужской") ? Gender.MALE : Gender.FEMALE);
    }

    @Transactional
    public boolean toggleLike(String login,int postId) {
        if (! checkUserPost(login,postId)) return false;
        String userWhoLikedLogin = SecurityContextHolder.getContext().getAuthentication().getName();
        User userWhoLiked = findByLogin(userWhoLikedLogin).get();
        Post post = postService.findById(postId).get();
        likeService.toggleLike(userWhoLiked,post);
        return true;
    }

    public boolean checkUserPost(String login,int postId) {
        Optional<User> userOptional = findByLogin(login);
        if (userOptional.isEmpty()) throw new UserNotFoundException();
        User user = userOptional.get();
        return user.getPosts().stream().map(Post::getId).anyMatch(id -> id == postId);
    }
}