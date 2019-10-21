package org.bmsource.dwh.common.appstate.pushnotification;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {
    SseEmitter initSseEmitters(String tenant, String projectId);

    <M extends Object> void sendSseEvent(M message);
}
