package com.example.meetontest.converters;

import com.example.meetontest.dto.CommentDTO;
import com.example.meetontest.entities.Comment;
import com.example.meetontest.services.MeetingService;
import com.example.meetontest.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class CommentConverter implements Converter<Comment, CommentDTO> {
    private final UserService userService;
    private final MeetingService meetingService;
    private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public Comment convert(CommentDTO entity) throws ParseException {
        Comment comment = new Comment();
        comment.setUser(userService.getUserById(entity.getUser_id()));
        comment.setMeeting(meetingService.getMeetingById(entity.getMeeting_id()));
        comment.setDate(new Date());
//        entity.getDate()==null?df.parse(entity.getDate()):new Date()
        comment.setContent(entity.getContent());
        return comment;
    }

    @Override
    public CommentDTO convertBack(Comment entity) {
        return new CommentDTO(entity.getComment_id(), entity.getMeeting().getId(), entity.getMeeting().getName(), entity.getUser().getId(),
                entity.getUser().getUsername(), entity.getContent(),df.format(entity.getDate()));
    }
}