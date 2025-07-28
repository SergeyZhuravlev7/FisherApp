package ru.zhuravlev.FisherApp.Repos;


/* author--> 
Sergey Zhuravlev
*/

import org.hibernate.annotations.Fetch;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.zhuravlev.FisherApp.Models.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

//    @EntityGraph(attributePaths = {"posts", "achievements"})
    Optional<User> findByLogin(String login);


}
