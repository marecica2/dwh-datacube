package org.bmsource.dwh.common.appstate;

import org.bmsource.dwh.common.appstate.pushnotification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.testcontainers.shaded.javax.ws.rs.HeaderParam;

@RestController
public class AppStateController {

    @Autowired
    NotificationService notificationService;

    @GetMapping("/{tenant}/{projectId}/status")
    public SseEmitter streamEvents(@PathVariable("tenant") String tenant,
                                   @PathVariable("projectId") String projectId) {
        return notificationService.initSseEmitters(tenant, projectId);
    }
}
