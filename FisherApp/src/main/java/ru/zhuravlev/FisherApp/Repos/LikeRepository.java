package ru.zhuravlev.FisherApp.Repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.zhuravlev.FisherApp.Models.Like;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Integer> {

    @Query ("SELECT COUNT(l) FROM Like l WHERE l.post.id = :id")
    long findAllById(@Param ("id") int id);

    @Query ("SELECT l.post.id FROM Like l where l.user.id = :userId and l.post.id IN :ids")
    List<Long> findLikedPostIds(int userId,List<Integer> ids);

    @Query ("SELECT l FROM Like l where l.user.id = :userId and l.post.id = :postId")
    Optional<Like> findOneByUserIdAndPostId(int userId,int postId);
}
