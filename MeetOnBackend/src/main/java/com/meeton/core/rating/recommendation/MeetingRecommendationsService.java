package com.meeton.core.rating.recommendation;

import com.meeton.core.entities.Meeting;
import com.meeton.core.entities.User;

import java.util.List;

public interface MeetingRecommendationsService {
    List<List<Meeting>> getRecommendations(List<Meeting> meetings, User target, int page);
}
