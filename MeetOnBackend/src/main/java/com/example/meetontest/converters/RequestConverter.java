package com.example.meetontest.converters;

import com.example.meetontest.dto.RequestDTO;
import com.example.meetontest.entities.MeetingRole;
import com.example.meetontest.entities.Request;
import com.example.meetontest.repositories.MeetingRepository;
import com.example.meetontest.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RequestConverter implements Converter<Request, RequestDTO> {
    private final UserService userService;
    private final MeetingRepository meetingService;

    @Override
    public Request convert(RequestDTO entity) {
        Request request = new Request();
        request.setAbout(entity.getAbout());
        request.setMeeting(meetingService.findById(entity.getMeeting_id()).get());
        request.setUser(userService.getUserById(entity.getUser_id()));
        if (entity.getRole() == null)
            request.setRole(MeetingRole.PARTICIPANT);
        else
            request.setRole(MeetingRole.valueOf(entity.getRole()));
        return request;
    }

    @Override
    public RequestDTO convertBack(Request entity) {
        return new RequestDTO(entity.getRequest_id(), entity.getAbout(), entity.getMeeting().getId(), entity.getMeeting().getName(), entity.getUser().getId(), entity.getUser().getUsername(), entity.getRole().toString(), entity.getStatus().toString());
    }
}
