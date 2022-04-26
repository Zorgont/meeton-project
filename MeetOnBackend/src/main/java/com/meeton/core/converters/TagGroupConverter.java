package com.meeton.core.converters;

import com.meeton.core.dto.TagGroupDTO;
import com.meeton.core.entities.Tag;
import com.meeton.core.entities.TagGroup;
import com.meeton.core.services.TagService;
import com.meeton.core.services.UserService;
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
