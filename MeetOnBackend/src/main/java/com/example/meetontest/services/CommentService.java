package com.example.meetontest.services;

import com.example.meetontest.entities.Comment;
import com.example.meetontest.entities.Meeting;
import com.example.meetontest.entities.User;

import java.util.List;

public interface CommentService {
    Comment create(Comment comment);

    Comment getById(Long id);

    List<Comment> getByUser(User user);

    List<Comment> getByMeeting(Meeting meeting);

    void removeById(Long id);
}
