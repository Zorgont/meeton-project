package com.meeton.core.services.impl;

import com.meeton.core.entities.Comment;
import com.meeton.core.entities.Meeting;
import com.meeton.core.entities.User;
import com.meeton.core.repositories.CommentRepository;
import com.meeton.core.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    public Comment create(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Comment getById(Long id){return commentRepository.findById(id).get();}

    @Override
    public List<Comment> getByUser(User user) {
        return commentRepository.findByUser(user);
    }

    @Override
    public List<Comment> getByMeeting(Meeting meeting) {
        return commentRepository.findByMeeting(meeting);
    }

    @Override
    public void removeById(Long id) {
        commentRepository.deleteById(id);
    }
}
