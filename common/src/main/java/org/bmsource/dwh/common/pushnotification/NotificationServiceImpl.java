package org.bmsource.dwh.common.pushnotification;

import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    private static final List<SseEmitter> emitters = Collections.synchronizedList(new ArrayList<>());

    @Scheduled(fixedRate = 30000)
    public void heartbeat() {
        List<SseEmitter> sseEmitterListToRemove = new ArrayList<>();
        emitters.forEach((SseEmitter emitter) -> {
            try {
                emitter.send("heartbeat", MediaType.TEXT_PLAIN);
            } catch (IOException e) {
                emitter.complete();
                sseEmitterListToRemove.add(emitter);
                e.printStackTrace();
            }
        });
        emitters.removeAll(sseEmitterListToRemove);
    }

    @Override
    public SseEmitter initSseEmitters() {
        SseEmitter emitter = new SseEmitter((long) (1000 * 60));
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        return emitter;
    }

    @Override
    public <M> void sendSseEvent(M message) {
        List<SseEmitter> sseEmitterListToRemove = new ArrayList<>();
        emitters.forEach((SseEmitter emitter) -> {
            try {
                emitter.send(message, MediaType.APPLICATION_JSON);
            } catch (IOException e) {
                emitter.complete();
                sseEmitterListToRemove.add(emitter);
                e.printStackTrace();
            }
        });
        emitters.removeAll(sseEmitterListToRemove);
    }
}
