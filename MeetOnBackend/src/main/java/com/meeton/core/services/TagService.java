package com.meeton.core.services;

import com.meeton.core.entities.Tag;

import java.util.List;
import java.util.Set;

public interface TagService {
    List<String> getTags();

    Set<Tag> getTags(List<String> tags);
}