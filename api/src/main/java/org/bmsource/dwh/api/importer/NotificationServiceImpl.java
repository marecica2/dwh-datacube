package org.bmsource.dwh.api.importer;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class NotificationServiceImpl {
    public static final List<SseEmitter> emitters = Collections.synchronizedList(new ArrayList<>());

    public SseEmitter initSseEmitters() {
        SseEmitter emitter = new SseEmitter();
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        return emitter;
    }

    public void sendSseEvent(AppStatus message) {
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
