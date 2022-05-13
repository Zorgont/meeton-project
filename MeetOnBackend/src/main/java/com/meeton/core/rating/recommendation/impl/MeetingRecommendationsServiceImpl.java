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
import com.meeton.core.services.client.ServiceClient;
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
    private final ServiceClient<List<List<MeetingDTO>>, RecommendationRequestDTO> recommendationManagerClient;

    @Override
    public List<List<Meeting>> getRecommendations(List<Meeting> meetings, User target, int page) {
        List<List<Meeting>> list = new ArrayList<>();
        if (target != null) {
            meetings.removeAll(meetingService.getMeetingsByManager(target));
            for (TagGroup tagGroup : tagGroupService.getByUser(target)) {
                list.add(
                        getRecommendationsByTags(meetings, new ArrayList<>(tagGroup.getTags()), 10 * page)
                                .stream()
                                .map(this::getRecommendationsByRating)
                                .flatMap(Collection::stream)
                                .collect(Collectors.toList())
                );
            }
        }

        if (CollectionUtils.isEmpty(list)) {
            list.add(getRecommendationsByRating(meetings));
        }

        return list.stream()
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

        List<List<MeetingDTO>> recommendedMeetingsDTO = recommendationManagerClient.execute(dto);
        return recommendedMeetingsDTO.stream()
                .filter(list -> !CollectionUtils.isEmpty(list))
                .map(list -> list.stream()
                        .map(meeting -> meetingService.getMeetingById(meeting.getMeetingId()))
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }
}
