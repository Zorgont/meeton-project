package com.example.meetontest.converters;

import com.example.meetontest.dto.TagGroupDTO;
import com.example.meetontest.entities.Tag;
import com.example.meetontest.entities.TagGroup;
import com.example.meetontest.services.TagService;
import com.example.meetontest.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TagGroupConverter implements Converter<TagGroup, TagGroupDTO> {
    private final UserService userService;
    private final TagService tagService;

    @Override
    public TagGroup convert(TagGroupDTO entity) throws ParseException {
        TagGroup tagGroup = new TagGroup();
        tagGroup.setIsNotifiable(entity.getIsNotifiable());
        tagGroup.setUser(userService.getUserById(entity.getUserId()));
        tagGroup.setTags(tagService.getTags(entity.getTags()));
        return tagGroup;
    }

    @Override
    public TagGroupDTO convertBack(TagGroup entity) {
        return new TagGroupDTO(entity.getTags().stream().map(Tag::getName).sorted().collect(Collectors.toList()), entity.getId(),
                entity.getUser().getId(), entity.getIsNotifiable());
    }
}
