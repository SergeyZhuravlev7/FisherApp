package ru.zhuravlev.FisherApp.Repos;


/* author--> 
Sergey Zhuravlev
*/

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.zhuravlev.FisherApp.Models.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByLogin(String login);

}
