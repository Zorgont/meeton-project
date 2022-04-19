package com.example.meetontest.controllers;

import com.example.meetontest.converters.ScoreConverter;
import com.example.meetontest.dto.AggregatedScoreDTO;
import com.example.meetontest.dto.DTO;
import com.example.meetontest.dto.MessageResponse;
import com.example.meetontest.dto.ScoreDTO;
import com.example.meetontest.entities.MeetingScore;
import com.example.meetontest.exceptions.ValidatorException;
import com.example.meetontest.megabrainutils.CheckUserCompliance;
import com.example.meetontest.services.MeetingService;
import com.example.meetontest.services.ScoreService;
import com.example.meetontest.services.UserService;
import com.example.meetontest.validators.ScoreValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/score")
@RequiredArgsConstructor
public class ScoreController {
    private final ScoreService scoreService;
    private final UserService userService;
    private final MeetingService meetingService;
    private final ScoreConverter converter;
    private final ScoreValidator validator;

    @GetMapping("/{meetingId}/aggregated")
    public AggregatedScoreDTO getAggregatedScoreByMeeting(@PathVariable Long meetingId) {
        return scoreService.getAggregatedScoreByMeeting(meetingService.getMeetingById(meetingId));
    }

    @GetMapping("/{meetingId}/byUser/{userId}")
    public ScoreDTO getScoreByMeetingAndUser(@PathVariable Long meetingId, @PathVariable Long userId) {
        MeetingScore score = scoreService.getByMeetingAndUser(meetingService.getMeetingById(meetingId), userService.getUserById(userId));
        return score != null ? converter.convertBack(score) : null;
    }

    @PostMapping("/{meetingId}")
    @CheckUserCompliance
    public ResponseEntity<?> createScore(@PathVariable Long meetingId, @RequestBody @DTO(name = "user_id") ScoreDTO score) throws ValidatorException {
        try {
            score.setMeeting_id(meetingId);
            validator.validate(score);
            MeetingScore meetingScore = converter.convert(score);
            return ResponseEntity.ok(converter.convertBack(scoreService.createScore(meetingScore.getMeeting(), meetingScore.getUser(), meetingScore.getScore())));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}
