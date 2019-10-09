package org.bmsource.dwh.sse;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {
    SseEmitter initSseEmitters();

    <M extends Object> void sendSseEvent(M message);
}
