package org.bmsource.dwh.common.pushnotification;

import org.bmsource.dwh.common.appstate.AppState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

@Component
public class AppStateSubscriber implements MessageListener {

    GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();

    @Autowired
    NotificationService notificationService;

    public void onMessage(final Message message, final byte[] pattern) {
        AppState state = (AppState) serializer.deserialize(message.getBody());
        notificationService.sendSseEvent(state);
    }
}