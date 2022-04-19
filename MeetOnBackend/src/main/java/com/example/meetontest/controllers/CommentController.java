package com.example.meetontest.controllers;


import com.example.meetontest.converters.CommentConverter;
import com.example.meetontest.dto.CommentDTO;
import com.example.meetontest.dto.DTO;
import com.example.meetontest.dto.MessageResponse;
import com.example.meetontest.megabrainutils.CheckUserCompliance;
import com.example.meetontest.services.CommentService;
import com.example.meetontest.services.MeetingService;
import com.example.meetontest.services.UserService;
import com.example.meetontest.validators.CommentValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "api/v1/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final UserService userService;
    private final MeetingService meetingService;
    private final CommentConverter commentConverter;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final CommentValidator commentValidator;
    @Autowired
    @Lazy
    private CommentController commentController;

    @MessageMapping("/createComment")
    public void createComment(@Payload CommentDTO commentDTO) throws ParseException, NoSuchFieldException, IllegalAccessException {
        commentValidator.validate(commentDTO);
        simpMessagingTemplate.convertAndSend("/meeting/" + commentDTO.getMeeting_id() + "/queue/comments",
                commentConverter.convertBack(commentService.create(commentConverter.convert(commentDTO))));

    }


    @GetMapping("/byUser/{id}")
    public List<CommentDTO> getCommentsByUserId(@PathVariable Long id) {
        return commentService.getByUser(userService.getUserById(id)).stream().
                map(commentConverter::convertBack).collect(Collectors.toList());
    }

    @GetMapping("/byMeeting/{id}")
    public List<CommentDTO> getCommentsByMeeting(@PathVariable Long id) {
        return commentService.getByMeeting(meetingService.getMeetingById(id)).stream().
                map(commentConverter::convertBack).collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeCommentById(@PathVariable Long id) {

            return commentController.removeCommentByIdWithDTO(commentConverter.convertBack(commentService.getById(id)))?
                    ResponseEntity.ok("Comment deleted!") :  ResponseEntity.badRequest().body(new MessageResponse("Wrong comment id!"));

    }
    @CheckUserCompliance
    public Boolean removeCommentByIdWithDTO(@DTO(name = "user_id") CommentDTO commentDTO){
        commentService.removeById(commentDTO.getId());
        return true;
    }

}
