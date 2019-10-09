package org.bmsource.dwh.sse;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    public static final List<SseEmitter> emitters = Collections.synchronizedList(new ArrayList<>());

    @Override
    public SseEmitter initSseEmitters() {
        SseEmitter emitter = new SseEmitter(new Long(1000 * 60));
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        try {
            emitter.send(SseEmitter.event().id("init").data("foo").name("hello").build());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return emitter;
    }

    @Override
    public <M extends Object> void sendSseEvent(M message) {
        List<SseEmitter> sseEmitterListToRemove = new ArrayList<>();
        this.emitters.forEach((SseEmitter emitter) -> {
            try {
                emitter.send(message, MediaType.APPLICATION_JSON);
            } catch (IOException e) {
                emitter.complete();
                sseEmitterListToRemove.add(emitter);
                e.printStackTrace();
            }
        });
        this.emitters.removeAll(sseEmitterListToRemove);
    }
}
