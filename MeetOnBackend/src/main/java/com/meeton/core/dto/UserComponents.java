package com.meeton.core.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class UserComponents {
    Long id;
    String username;
    String email;
    List<MeetingComponents> meetingComponents;
}
