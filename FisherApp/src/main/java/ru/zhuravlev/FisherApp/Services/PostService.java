package ru.zhuravlev.FisherApp.Services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.zhuravlev.FisherApp.Models.Post;
import ru.zhuravlev.FisherApp.Repos.LikeRepository;
import ru.zhuravlev.FisherApp.Repos.PostRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;

    @Autowired
    public PostService(PostRepository postRepository,LikeRepository likeRepository) {
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
    }

    @Transactional
    public void save(Post post) {
        postRepository.save(post);
    }

    @Transactional
    public void delete(Post post) {
        postRepository.delete(post);
    }

    @Transactional
    public void delete(int id) {
        Optional<Post> optionalPost = findById(id);
        if (optionalPost.isEmpty()) return;
        postRepository.delete(optionalPost.get());
    }

    public Optional<Post> findById(int id) {
        return postRepository.findById(id);
    }

    public Set<Post> loadPosts(int userId) {
        return postRepository.findAllByUserId(userId);
    }

    public Set<Post> loadUserLikes(int authenticatedUserId,Set<Post> loadedUserPosts) {
        List<Integer> postIds = loadedUserPosts.stream().map(Post::getId).toList();
        List<Long> likedPostIds = likeRepository.findLikedPostIds(authenticatedUserId,postIds);
        loadedUserPosts.stream()
                .filter(post -> likedPostIds.contains(Integer.valueOf(post.getId()).longValue()))
                .sorted((post1,post2) -> post2.getId() - post1.getId())
                .forEach(post -> post.setLikedByThisUser(true));
        return loadedUserPosts;
    }
}
