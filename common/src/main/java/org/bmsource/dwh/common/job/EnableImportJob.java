package org.bmsource.dwh.common.job;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value = RetentionPolicy.RUNTIME)
@Import(JobConfiguration.class)
public @interface EnableImportJob {
}