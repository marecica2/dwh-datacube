package org.bmsource.dwh.common.appstate;

import org.bmsource.dwh.common.pushnotification.AppState;
import org.bmsource.dwh.common.pushnotification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

@Component
public class Receiver implements MessageListener {

    GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();

    @Autowired
    NotificationService notificationService;

    public void onMessage(final Message message, final byte[] pattern) {
        System.out.println(new String(message.getBody()));
        AppState state = (AppState) serializer.deserialize(message.getBody());
        notificationService.sendSseEvent(state);
    }
}