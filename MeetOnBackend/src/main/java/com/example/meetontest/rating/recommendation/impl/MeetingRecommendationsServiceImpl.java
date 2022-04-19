package com.example.meetontest.rating.recommendation.impl;

import com.example.meetontest.entities.Meeting;
import com.example.meetontest.entities.Tag;
import com.example.meetontest.entities.TagGroup;
import com.example.meetontest.entities.User;
import com.example.meetontest.rating.recommendation.MeetingRecommendationsService;
import com.example.meetontest.rating.service.UserRatingProvider;
import com.example.meetontest.services.MeetingService;
import com.example.meetontest.services.TagGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetingRecommendationsServiceImpl implements MeetingRecommendationsService {
    private final UserRatingProvider userRatingProvider;
    private final TagGroupService tagGroupService;
    private final MeetingService meetingService;

    @Override
    public List<List<Meeting>> getRecommendations(List<Meeting> meetings, User target,int page) {
        List<List<Meeting>> list = new ArrayList<>();
        List<TagGroup> tagGroups = target != null ? tagGroupService.getByUser(target) : null;
        if(tagGroups == null || tagGroups.isEmpty()) {
             list.add(getRecommendationsByRating(meetings).stream().filter(meeting -> target == null || !meetingService.getManager(meeting).getId().equals(target.getId())).limit(10L * (page)).skip(10L * (page - 1)).collect(Collectors.toList()));
        }
        else {
            tagGroups.forEach((tagGroup -> {
                list.add(getRecommendationsByTags(new ArrayList<>(meetings), new ArrayList<>(tagGroup.getTags()), 10 * page)
                        .stream().map(this::getRecommendationsByRating).flatMap(Collection::stream).filter(meeting -> !meetingService.getManager(meeting).getId().equals(target.getId())).limit(10L * (page)).skip(10L * (page - 1)).collect(Collectors.toList()));
            }));
        }
        return list;

    }

    private List<Meeting> getRecommendationsByRating(List<Meeting> meetings) {
        meetings.sort(Comparator.comparing(meeting -> userRatingProvider.getUserRating(meetingService.getManager((Meeting) meeting))).reversed());
        return meetings;
    }

    private List<List<Meeting>> getRecommendationsByTags(List<Meeting> meetings, List<Tag> tags,int endIndex) {
        List<List<Meeting>> recommendedMeetings = new ArrayList<>();
        for(int i = tags.size(); i > 0; i--){
            recommendedMeetings.add(getRecommendationsSubGroup(meetings, tags, i));
            if(meetings.isEmpty()) break;
            if(endIndex <= recommendedMeetings.stream().mapToInt(List::size).sum())
                return recommendedMeetings;
        }
        if(!meetings.isEmpty())
            recommendedMeetings.add(meetings);
        return recommendedMeetings;
    }

    private List<Meeting> getRecommendationsSubGroup(List<Meeting> meetings, List<Tag> tags, int size){
        List<Meeting> sortedMeetings = new ArrayList<>();
        recursiveMethod(meetings, sortedMeetings, tags, new ArrayList<Tag>(), 0, size);
        return sortedMeetings;
    }

    private void recursiveMethod(List<Meeting> meetings, List<Meeting> sortedMeetings, List<Tag> tags, List<Tag> changingTags, int index, int fullSize){
        if(index == fullSize)
        {   if(changingTags.stream().distinct().count() < changingTags.size()) return;
            List<Meeting> temporalList = meetings.stream().filter(meeting -> meeting.getTags().stream().map(Tag::getName).collect(Collectors.toList()).containsAll(changingTags.stream().map(Tag::getName).collect(Collectors.toList()))).collect(Collectors.toList());
            sortedMeetings.addAll(temporalList);
            meetings.removeAll(temporalList);
        }
        else{
            for(int i = 0; i < tags.size(); i++){
                if(changingTags.size() > index)
                    changingTags.set(index, tags.get(i));
                else
                    changingTags.add(index, tags.get(i));
                recursiveMethod(meetings, sortedMeetings, tags, changingTags, index + 1, fullSize);

            }
        }
    }
}
