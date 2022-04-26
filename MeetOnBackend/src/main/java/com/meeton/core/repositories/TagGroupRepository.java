package com.meeton.core.repositories;

import com.meeton.core.entities.TagGroup;
import com.meeton.core.entities.User;
import com.meeton.core.entities.TagGroup;
import com.meeton.core.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagGroupRepository extends JpaRepository<TagGroup, Long> {
    List<TagGroup> findByUser(User user);

    List<TagGroup> findByUserAndIsNotifiable(User user, boolean isNotifiable);
}
