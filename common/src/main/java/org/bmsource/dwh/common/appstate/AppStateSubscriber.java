package org.bmsource.dwh.common.appstate;

import org.bmsource.dwh.common.appstate.pushnotification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AppStateSubscriber implements MessageListener {

    @Autowired
    NotificationService notificationService;

    @Autowired
    AppStateService appStateService;

    public void onMessage(final Message message, final byte[] pattern) {
        String[] params = new String(message.getBody()).split(":");
        String tenant = params[0];
        String project = params[1];
        Map<String, Map<String, Object>> state = appStateService.getState(tenant, project);
        notificationService.sendSseEvent(state);
    }
}