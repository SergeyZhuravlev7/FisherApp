package ru.zhuravlev.FisherApp.Repos;


/* author--> 
Sergey Zhuravlev
*/

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.zhuravlev.FisherApp.Models.Post;

import java.util.Set;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query ("SELECT p FROM Post p where p.user.id = :userId")
    Set<Post> findAllByUserId(int userId);
}
