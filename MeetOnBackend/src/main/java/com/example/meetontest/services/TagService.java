package com.example.meetontest.services;

import com.example.meetontest.entities.Tag;

import java.util.List;
import java.util.Set;

public interface TagService {
    List<String> getTags();

    Set<Tag> getTags(List<String> tags);
}