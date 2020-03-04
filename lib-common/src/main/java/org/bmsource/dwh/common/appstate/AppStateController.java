package org.bmsource.dwh.common.appstate;

import org.bmsource.dwh.common.appstate.pushnotification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.websocket.server.PathParam;

@RestController
public class AppStateController {

    @Autowired
    NotificationService notificationService;

    @GetMapping("status")
    public SseEmitter streamEvents(@PathParam("tenant") String tenant,
                                   @PathParam("projectId") String projectId) {
        return notificationService.initSseEmitters(tenant, projectId);
    }

    @ExceptionHandler(value = AsyncRequestTimeoutException.class)
    public String asyncTimeout(AsyncRequestTimeoutException e) {
        return null; // ignoring SSE timeout by intention;
    }
}
