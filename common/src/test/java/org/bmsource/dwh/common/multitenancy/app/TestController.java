package org.bmsource.dwh.common.multitenancy.app;

import org.bmsource.dwh.common.multitenancy.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController("/test")
public class TestController {

    @Autowired
    PersonRepository repository;

    @GetMapping
    public Iterable<Person> testMultitenancyRequest(@RequestHeader(Constants.TENANT_HEADER) String tenantHeader) {
        return repository.findAll();
    }

}
