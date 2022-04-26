package com.meeton.core.repositories;

import com.meeton.core.entities.Comment;
import com.meeton.core.entities.Meeting;
import com.meeton.core.entities.User;
import com.meeton.core.entities.Comment;
import com.meeton.core.entities.Meeting;
import com.meeton.core.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByUser(User user);

    List<Comment> findByMeeting(Meeting meeting);
}
