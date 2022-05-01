package com.meeton.core.rating.recommendation.impl;

import com.meeton.core.converters.MeetingConverter;
import com.meeton.core.dto.MeetingDTO;
import com.meeton.core.dto.RecommendationRequestDTO;
import com.meeton.core.entities.Meeting;
import com.meeton.core.entities.Tag;
import com.meeton.core.entities.TagGroup;
import com.meeton.core.entities.User;
import com.meeton.core.rating.recommendation.MeetingRecommendationsService;
import com.meeton.core.rating.service.UserRatingProvider;
import com.meeton.core.services.MeetingService;
import com.meeton.core.services.TagGroupService;
import com.meeton.core.services.client.RecommendationManagerClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetingRecommendationsServiceImpl implements MeetingRecommendationsService {
    private final UserRatingProvider userRatingProvider;
    private final TagGroupService tagGroupService;
    private final MeetingService meetingService;
    private final MeetingConverter meetingConverter;
    private final RecommendationManagerClient recommendationManagerClient;

    @Override
    public List<List<Meeting>> getRecommendations(List<Meeting> meetings, User target, int page) {
        List<List<Meeting>> list = new ArrayList<>();
        if (target != null) {
            meetings.removeAll(meetingService.getMeetingsByManager(target));
            for (TagGroup tagGroup : tagGroupService.getByUser(target)) {
                list.add(
                        getRecommendationsByTags(meetings, new ArrayList<>(tagGroup.getTags()), 10 * page)
                                .stream()
                                .flatMap(Collection::stream)
                                .collect(Collectors.toList())
                );
            }
        }

        if (CollectionUtils.isEmpty(list)) {
            list.add(meetings);
        }

        return list.stream()
                .map(this::getRecommendationsByRating)
                .limit(10L * (page))
                .skip(10L * (page - 1))
                .collect(Collectors.toList());
    }

    private List<Meeting> getRecommendationsByRating(List<Meeting> meetings) {
        meetings.sort(Comparator.comparing(meeting -> userRatingProvider.getUserRating(meetingService.getManager((Meeting) meeting))).reversed());
        return meetings;
    }

    private List<List<Meeting>> getRecommendationsByTags(List<Meeting> meetings, List<Tag> tags, int endIndex) {
        RecommendationRequestDTO dto = new RecommendationRequestDTO(
                meetings.stream().map(meetingConverter::convertBack).collect(Collectors.toList()),
                tags.stream().map(Tag::getName).collect(Collectors.toList()), endIndex);

        List<List<MeetingDTO>> recommendedMeetingsDTO = recommendationManagerClient.calculateRecommendations(dto);
        return recommendedMeetingsDTO.stream()
                .filter(list -> !CollectionUtils.isEmpty(list))
                .map(list -> list.stream()
                        .map(meeting -> meetingService.getMeetingById(meeting.getMeetingId()))
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    private List<Meeting> getRecommendationsSubGroup(List<Meeting> meetings, List<Tag> tags, int size) {
        List<Meeting> sortedMeetings = new ArrayList<>();
        recursiveMethod(meetings, sortedMeetings, tags, new ArrayList<Tag>(), 0, size);
        return sortedMeetings;
    }

    private void recursiveMethod(List<Meeting> meetings, List<Meeting> sortedMeetings, List<Tag> tags, List<Tag> changingTags, int index, int fullSize) {
        if (index == fullSize) {
            if (changingTags.stream().distinct().count() < changingTags.size()) return;
            List<Meeting> temporalList = meetings.stream().filter(meeting -> meeting.getTags().stream().map(Tag::getName).collect(Collectors.toList()).containsAll(changingTags.stream().map(Tag::getName).collect(Collectors.toList()))).collect(Collectors.toList());
            sortedMeetings.addAll(temporalList);
            meetings.removeAll(temporalList);
        } else {
            for (int i = 0; i < tags.size(); i++) {
                if (changingTags.size() > index)
                    changingTags.set(index, tags.get(i));
                else
                    changingTags.add(index, tags.get(i));
                recursiveMethod(meetings, sortedMeetings, tags, changingTags, index + 1, fullSize);
            }
        }
    }
}
