package com.smz.gymplus.repository;

import com.smz.gymplus.domain.Gym;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface GymRepositoryWithBagRelationships {
    Optional<Gym> fetchBagRelationships(Optional<Gym> gym);

    List<Gym> fetchBagRelationships(List<Gym> gyms);

    Page<Gym> fetchBagRelationships(Page<Gym> gyms);
}
