package com.example.meetontest.repositories;

import com.example.meetontest.entities.TagGroup;
import com.example.meetontest.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagGroupRepository extends JpaRepository<TagGroup, Long> {
    List<TagGroup> findByUser(User user);

    List<TagGroup> findByUserAndIsNotifiable(User user, boolean isNotifiable);
}
