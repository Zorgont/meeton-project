package com.meeton.core.controllers;

import com.meeton.core.converters.RequestConverter;
import com.meeton.core.dto.DTO;
import com.meeton.core.dto.MessageResponse;
import com.meeton.core.dto.RequestDTO;
import com.meeton.core.entities.Request;
import com.meeton.core.entities.RequestStatus;
import com.meeton.core.exceptions.ValidatorException;
import com.meeton.core.megabrainutils.CheckUserCompliance;
import com.meeton.core.services.MeetingService;
import com.meeton.core.services.RequestService;
import com.meeton.core.services.UserService;
import com.meeton.core.services.impl.UserDetailsImpl;
import com.meeton.core.validators.RequestValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "meeton-core/v1/requests")
@RequiredArgsConstructor
public class RequestController {
    private final MeetingService meetingService;
    private final UserService userService;
    private final RequestService requestService;
    private final RequestConverter requestConverter;
    private final RequestValidator requestValidator;
    @Autowired
    @Lazy
    private RequestController requestController;

    @GetMapping("/{id}")
    public ResponseEntity<?> getRequestById(@PathVariable Long id) {
        Optional<Request> request = requestService.getById(id);
        return request.isPresent() ? ResponseEntity.ok(requestConverter.convertBack(request.get())) :
                ResponseEntity.badRequest().body(new MessageResponse("Request not found!"));
    }

    @GetMapping("/byUser/{id}")
    public List<RequestDTO> getRequestsByUserId(@PathVariable Long id) {
        return requestService.getByUser(userService.getUserById(id)).stream().map(requestConverter::convertBack).collect(Collectors.toList());
    }

    @GetMapping("/byMeeting/{id}")
    public List<RequestDTO> getRequestsByMeetingId(@PathVariable Long id) {
        return requestService.getByMeeting(meetingService.getMeetingById(id)).stream().map(requestConverter::convertBack).collect(Collectors.toList());
    }

    @GetMapping("/by")
    public RequestDTO getRequestByMeetingAndUser(@RequestParam Long meetingId, @RequestParam Long userId) {
        return requestService.getByMeetingIdUserId(meetingId, userId).map(requestConverter::convertBack).orElse(null);
    }

    @GetMapping("/amount/{id}")
    public int getRequestAmountByMeetingId(@PathVariable Long id) {
        return requestService.getApprovedRequestsAmount(id);
    }

    @GetMapping("/pendingRequests/{id}")
    public List<RequestDTO> getPendingRequests(@PathVariable Long id) {
        return requestService.getByMeetingAndStatus(meetingService.getMeetingById(id), RequestStatus.PENDING).
                stream().map(requestConverter::convertBack).collect(Collectors.toList());
    }
    @CheckUserCompliance
    @PostMapping
    public ResponseEntity<?> createRequest(@RequestBody @DTO(name = "user_id") RequestDTO requestDTO) throws JsonProcessingException, IllegalAccessException, NoSuchFieldException {
        requestValidator.validate(requestDTO);
        return ResponseEntity.ok(requestConverter.convertBack(requestService.create(requestConverter.convert(requestDTO))));
    }

    @PutMapping("/changeStatus/{id}")
    public ResponseEntity<?> updateRequestStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            RequestDTO dto = requestConverter.convertBack(requestService.getById(id).get());

            // Если менеджер митинга изменяет статус:
            if (meetingService.getManager(meetingService.getMeetingById(dto.getMeeting_id())).getId().equals(((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId())) {
                dto.setStatus(status);
                requestService.changeStatus(requestService.getById(dto.getId()).get(), RequestStatus.valueOf(dto.getStatus().toUpperCase()));
                return ResponseEntity.ok(requestConverter.convertBack(requestService.getById(dto.getId()).get()));
            }
            throw new ValidatorException("Wrong action!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeRequestById(@PathVariable Long id) {
        try {
            return requestController.deleteRequestStatusWithDTO(requestConverter.convertBack(requestService.getById(id).get())) ?
                    ResponseEntity.ok("deleted") : ResponseEntity.badRequest().body(new MessageResponse("Wrong id!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
    @CheckUserCompliance
    public Boolean deleteRequestStatusWithDTO(@DTO(name = "user_id") RequestDTO requestDTO) {
        requestService.removeById(requestDTO.getId());
        return true;
    }
}