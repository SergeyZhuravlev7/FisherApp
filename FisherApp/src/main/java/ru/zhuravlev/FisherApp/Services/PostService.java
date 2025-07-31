package ru.zhuravlev.FisherApp.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.zhuravlev.FisherApp.Models.Post;
import ru.zhuravlev.FisherApp.Repos.PostRepository;

import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public void save(Post post) {
        postRepository.save(post);
    }

    public void delete(Post post) {
        postRepository.delete(post);
    }

    public Optional<Post> findById(int id) {
        return postRepository.findById(id);
    }


}
