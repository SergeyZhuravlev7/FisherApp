package ru.zhuravlev.FisherApp.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.zhuravlev.FisherApp.Models.Like;
import ru.zhuravlev.FisherApp.Models.Post;
import ru.zhuravlev.FisherApp.Models.User;
import ru.zhuravlev.FisherApp.Repos.LikeRepository;

import java.util.Optional;

@Service
public class LikeService {

    private final LikeRepository likeRepository;

    @Autowired
    public LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    public void toggleLike(User user,Post post) {
        Optional<Like> likeOpt = likeRepository.findOneByUserIdAndPostId(user.getId(),post.getId());
        if (likeOpt.isEmpty()) {
            Like like = new Like();
            like.setPost(post);
            like.setUser(user);
            likeRepository.save(like);
            post.setLikesCount(post.getLikesCount() + 1);
        } else {
            likeRepository.delete(likeOpt.get());
            post.setLikesCount(post.getLikesCount() - 1);
        }
    }
}
