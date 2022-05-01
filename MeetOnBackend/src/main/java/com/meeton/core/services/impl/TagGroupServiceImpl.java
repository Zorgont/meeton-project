package com.meeton.core.services.impl;

import com.meeton.core.entities.TagGroup;
import com.meeton.core.entities.User;
import com.meeton.core.repositories.TagGroupRepository;
import com.meeton.core.services.TagGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagGroupServiceImpl implements TagGroupService {
    private final TagGroupRepository tagGroupRepository;

    @Override
    public TagGroup createTagGroup(TagGroup tagGroup, User user) {
        tagGroup.setUser(user);
        return tagGroupRepository.save(tagGroup);
    }

    @Override
    public List<TagGroup> getByUser(User user) {
        List<TagGroup> tagGroups = tagGroupRepository.findByUser(user);
        if (CollectionUtils.isEmpty(tagGroups)) {
            return new ArrayList<>();
        } else {
            return tagGroups.stream().sorted(Comparator.comparing(TagGroup::getId)).collect(Collectors.toList());
        }
    }

    @Override
    public List<TagGroup> getNotifiableByUser(User user) {
        return tagGroupRepository.findByUserAndIsNotifiable(user, true);
    }

    @Override
    public void deleteByGroupId(Long id) {
        tagGroupRepository.deleteById(id);
    }

    @Override
    public TagGroup setNotifiable(Long id, boolean isNotifiable) {
        TagGroup tagGroup = tagGroupRepository.findById(id).get();
        tagGroup.setIsNotifiable(isNotifiable);
        return tagGroupRepository.save(tagGroup);
    }
}
