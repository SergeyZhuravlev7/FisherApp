package ru.zhuravlev.FisherApp.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.zhuravlev.FisherApp.Models.Fish;
import ru.zhuravlev.FisherApp.Models.Post;
import ru.zhuravlev.FisherApp.Repos.PostRepository;
import ru.zhuravlev.FisherApp.Util.FishNotFoundException;

import java.util.Optional;

@Service
public class PostService {

    private final FishService fishService;
    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository, FishService fishService) {
        this.postRepository = postRepository;
        this.fishService = fishService;
    }

    public void setFish(Post post) {
        Optional<Fish> optionalFish = fishService.findByName(post.getFish().getName());
        if (optionalFish.isEmpty()) throw new FishNotFoundException();
        Fish fish = optionalFish.get();
        post.setFish(fish);
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
