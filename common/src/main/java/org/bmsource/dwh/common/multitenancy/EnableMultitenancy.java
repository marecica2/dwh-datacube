package org.bmsource.dwh.common.multitenancy;


import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(Multitenancy.class)
public @interface EnableMultitenancy {
    Class value();
}


