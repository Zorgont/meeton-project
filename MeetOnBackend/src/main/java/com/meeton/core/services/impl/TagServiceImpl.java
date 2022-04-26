package com.meeton.core.services.impl;

import com.meeton.core.entities.Tag;
import com.meeton.core.repositories.TagRepository;
import com.meeton.core.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    public List<String> getTags() {
        return tagRepository.findAll().stream().map(tag -> tag.getName()).sorted().collect(Collectors.toList());
    }

    public Set<Tag> getTags(List<String> tags) {
        return tagRepository.findAll().stream().filter(tag -> tags.contains(tag.getName())).collect(Collectors.toSet());
    }
}