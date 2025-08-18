package ru.zhuravlev.FisherApp.Services;


/* author--> 
Sergey Zhuravlev
*/

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.zhuravlev.FisherApp.Models.Post;
import ru.zhuravlev.FisherApp.Models.User;
import ru.zhuravlev.FisherApp.Repos.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PostService postService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public UserService(UserRepository userRepository,PostService postService) {
        this.userRepository = userRepository;
        this.postService = postService;
    }

    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Transactional
    public User loadUser(String login) {
        User user = userRepository.findByLogin(login).get();
        Hibernate.initialize(user.getPosts());
        Hibernate.initialize(user.getAchievements());
        return user;
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
}