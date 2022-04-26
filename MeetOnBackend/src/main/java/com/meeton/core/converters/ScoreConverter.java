package com.meeton.core.converters;

import com.meeton.core.dto.ScoreDTO;
import com.meeton.core.entities.Meeting;
import com.meeton.core.entities.MeetingScore;
import com.meeton.core.entities.User;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component
public class ScoreConverter implements Converter<MeetingScore, ScoreDTO> {

    @Override
    public MeetingScore convert(ScoreDTO entity) throws ParseException {
        MeetingScore score = new MeetingScore();
        if(entity.getId() != null)
        score.setScore_id(entity.getId());
        score.setScore(entity.getScore());
        if(entity.getDate() != null)
        score.setDate(entity.getDate());
        Meeting meeting = new Meeting();
        meeting.setId(entity.getMeeting_id());
        score.setMeeting(meeting);
        User user = new User();
        user.setId(entity.getUser_id());
        score.setUser(user);
        return score;
    }

    @Override
    public ScoreDTO convertBack(MeetingScore entity) {
        return new ScoreDTO(entity.getScore_id(), entity.getMeeting().getId(), entity.getUser().getId(), entity.getScore(), entity.getDate());
    }
}
