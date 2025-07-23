package ru.zhuravlev.FisherApp.Repos;

/* author--> 
Sergey Zhuravlev
*/

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.zhuravlev.FisherApp.Models.Achievement;

@Repository
public interface AchievementsRepository extends JpaRepository<Achievement, Integer> {
}
