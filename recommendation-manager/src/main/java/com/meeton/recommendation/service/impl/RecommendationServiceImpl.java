package com.meeton.recommendation.service.impl;

import com.meeton.recommendation.model.dto.MeetingDTO;
import com.meeton.recommendation.model.dto.RecommendationRequestDTO;
import com.meeton.recommendation.service.RecommendationService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendationServiceImpl implements RecommendationService {
    @Override
    public List<List<MeetingDTO>> getRecommendationsByTags(RecommendationRequestDTO recommendationRequest) {
        List<List<MeetingDTO>> recommendedMeetings = new ArrayList<>();
        for (int i = recommendationRequest.getTags().size(); i > 0; i--) {
            recommendedMeetings.add(getRecommendationsSubGroup(recommendationRequest.getMeetings(), recommendationRequest.getTags(), i));
            if (recommendationRequest.getMeetings().isEmpty()) break;
            if (recommendationRequest.getMaxNumber() <= recommendedMeetings.stream().mapToInt(List::size).sum())
                return recommendedMeetings;
        }
        if (!recommendationRequest.getMeetings().isEmpty())
            recommendedMeetings.add(recommendationRequest.getMeetings());
        return recommendedMeetings;
    }

    private List<MeetingDTO> getRecommendationsSubGroup(List<MeetingDTO> meetings, List<String> tags, int size) {
        List<MeetingDTO> sortedMeetings = new ArrayList<>();
        recursiveMethod(meetings, sortedMeetings, tags, new ArrayList<>(), 0, size);
        return sortedMeetings;
    }

    private void recursiveMethod(List<MeetingDTO> meetings, List<MeetingDTO> sortedMeetings, List<String> tags, List<String> changingTags, int index, int fullSize) {
        if (index == fullSize) {
            if (changingTags.stream().distinct().count() < changingTags.size()) return;
            List<MeetingDTO> temporalList = meetings.stream()
                    .filter(meeting -> meeting.getTags().containsAll(changingTags))
                    .collect(Collectors.toList());
            sortedMeetings.addAll(temporalList);
            meetings.removeAll(temporalList);
        } else {
            for (int i = 0; i < tags.size(); i++) {
                if (changingTags.size() > index) {
                    changingTags.set(index, tags.get(i));
                } else {
                    changingTags.add(index, tags.get(i));
                }
                recursiveMethod(meetings, sortedMeetings, tags, changingTags, index + 1, fullSize);
            }
        }
    }
}
