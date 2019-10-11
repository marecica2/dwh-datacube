package org.bmsource.dwh.common.appstate;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;


/**
 * Enables App state management for push notification to client using Server side events
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(AppStateConfiguration.class)
@Configuration
public @interface EnableAppStateManagement {
}