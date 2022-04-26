package com.meeton.core.services;

import com.meeton.core.entities.Comment;
import com.meeton.core.entities.Meeting;
import com.meeton.core.entities.User;

import java.util.List;

public interface CommentService {
    Comment create(Comment comment);

    Comment getById(Long id);

    List<Comment> getByUser(User user);

    List<Comment> getByMeeting(Meeting meeting);

    void removeById(Long id);
}
