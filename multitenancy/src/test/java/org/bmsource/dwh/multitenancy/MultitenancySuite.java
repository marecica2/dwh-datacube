package org.bmsource.dwh.multitenancy;

import org.bmsource.dwh.multitenancy.groups.GroupRepositoryTest;
import org.bmsource.dwh.multitenancy.users.UserRepositoryTest;
import org.bmsource.dwh.schemas.controllers.MessageControllerIT;
import org.bmsource.dwh.schemas.controllers.TenantControllerIT;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        GroupRepositoryTest.class,
        UserRepositoryTest.class,
        MessageControllerIT.class,
        TenantControllerIT.class,
})

public class MultitenancySuite {
}
