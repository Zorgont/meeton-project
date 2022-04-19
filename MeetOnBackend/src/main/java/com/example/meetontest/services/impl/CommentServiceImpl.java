package com.example.meetontest.services.impl;

import com.example.meetontest.entities.Comment;
import com.example.meetontest.entities.Meeting;
import com.example.meetontest.entities.User;
import com.example.meetontest.repositories.CommentRepository;
import com.example.meetontest.services.CommentService;
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
