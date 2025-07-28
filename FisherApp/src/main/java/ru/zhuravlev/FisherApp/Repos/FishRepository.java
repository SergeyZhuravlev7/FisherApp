package ru.zhuravlev.FisherApp.Repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.zhuravlev.FisherApp.Models.Fish;

import java.util.Optional;

@Repository
public interface FishRepository extends JpaRepository<Fish, Integer> {

    Optional<Fish> findByName(String name);
}
