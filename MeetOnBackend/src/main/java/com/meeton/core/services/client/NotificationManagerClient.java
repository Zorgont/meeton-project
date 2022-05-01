package com.meeton.core.services.client;

import com.meeton.core.config.AppConfig;
import com.meeton.core.dto.NotificationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class NotificationManagerClient extends AbstractRestClient<NotificationDTO> {
    private final RestTemplate restTemplate;
    private final AppConfig appConfig;

    public void sendNotification(NotificationDTO dto) {
        super.execute(restTemplate, dto, appConfig.getApiGatewayUrl() + "/notification-manager/v1/notification");
    }
}
