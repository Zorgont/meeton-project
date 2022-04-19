package com.example.meetontest.services;

import com.example.meetontest.entities.TagGroup;
import com.example.meetontest.entities.User;

import java.util.List;

public interface TagGroupService {
    TagGroup createTagGroup(TagGroup tagGroup, User user);

    List<TagGroup> getByUser(User user);

    List<TagGroup> getNotifiableByUser(User user);

    void deleteByGroupId(Long id);

    TagGroup setNotifiable(Long id, boolean isNotifiable);
}
