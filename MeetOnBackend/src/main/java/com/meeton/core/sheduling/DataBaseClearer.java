package com.meeton.core.sheduling;

import com.meeton.core.entities.ConfirmationToken;
import com.meeton.core.entities.EventEntity;
import com.meeton.core.entities.EventStatus;
import com.meeton.core.repositories.ConfirmationTokenRepository;
import com.meeton.core.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DataBaseClearer {
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final EventRepository eventRepository;

    @Scheduled(fixedDelay = 60000)
    private void clearEntity(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -1);

        List<ConfirmationToken> tokensToRemove = confirmationTokenRepository.findAll().stream()
            .filter(token -> token.getCreatedDate().before(calendar.getTime())).collect(Collectors.toList());
        confirmationTokenRepository.deleteAll(tokensToRemove);

        List<EventEntity> eventsToRemove = eventRepository.findByStatus(EventStatus.HANDLED).stream()
            .filter(event -> event.getDate().before(calendar.getTime())).collect(Collectors.toList());
        eventRepository.deleteAll(eventsToRemove);
    }
}
