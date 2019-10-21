package org.bmsource.dwh.common.appstate.pushnotification;

import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;

@Service
public class NotificationServiceImpl implements NotificationService {
    private static final Map<String, List<SseEmitter>> emittersMap = Collections.synchronizedMap(new HashMap<>());

    @Scheduled(fixedRate = 30000)
    public void heartbeat() {
        List<SseEmitter> sseEmitterListToRemove = new ArrayList<>();
        for (List<SseEmitter> emitters : emittersMap.values()) {
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
    }

    @Override
    public SseEmitter initSseEmitters(String tenant, String projectId) {
        String key = tenant + projectId;
        List<SseEmitter> emmiters = emittersMap.get(key);
        if (emmiters == null) {
            emmiters = Collections.synchronizedList(new ArrayList<>());
            emittersMap.put(key, emmiters);
        }
        SseEmitter emitter = new SseEmitter((long) (1000 * 60));
        emitter.onCompletion(() -> emittersMap.get(key).remove(emitter));
        emmiters.add(emitter);
        return emitter;
    }

    @Override
    public <M> void sendSseEvent(String tenant, String projectId, M message) {
        String key = tenant + projectId;
        List<SseEmitter> emitters = emittersMap.get(key);
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
