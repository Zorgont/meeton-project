package com.meeton.core.services;

import com.meeton.core.entities.TagGroup;
import com.meeton.core.entities.User;

import java.util.List;

public interface TagGroupService {
    TagGroup createTagGroup(TagGroup tagGroup, User user);

    List<TagGroup> getByUser(User user);

    List<TagGroup> getNotifiableByUser(User user);

    void deleteByGroupId(Long id);

    TagGroup setNotifiable(Long id, boolean isNotifiable);
}
