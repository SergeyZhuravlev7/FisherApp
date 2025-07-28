package ru.zhuravlev.FisherApp.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.zhuravlev.FisherApp.Models.Fish;
import ru.zhuravlev.FisherApp.Repos.FishRepository;

import java.util.Optional;

@Service
public class FishService {

    private final FishRepository fishRepository;

    @Autowired
    public FishService(FishRepository fishRepository) {
        this.fishRepository = fishRepository;
    }

    public Optional<Fish> findByName(String name) {
        return fishRepository.findByName(name);
    }
}
