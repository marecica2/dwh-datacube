package org.bmsource.dwh.common.appstate;

import org.bmsource.dwh.common.appstate.pushnotification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class AppStateController {

    @Autowired
    NotificationService notificationService;

    @GetMapping("/status")
    public SseEmitter streamEvents() {
        return notificationService.initSseEmitters();
    }
}
