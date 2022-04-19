package com.example.meetontest.rating.recommendation;

import com.example.meetontest.entities.Meeting;
import com.example.meetontest.entities.User;

import java.util.List;

public interface MeetingRecommendationsService {
    List<List<Meeting>> getRecommendations(List<Meeting> meetings, User target, int page);

}
