package com.example.meetontest.repositories;

import com.example.meetontest.entities.Comment;
import com.example.meetontest.entities.Meeting;
import com.example.meetontest.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByUser(User user);

    List<Comment> findByMeeting(Meeting meeting);
}
