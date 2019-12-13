package org.bmsource.dwh.charts;

import com.google.common.hash.Hashing;
import org.springframework.cache.interceptor.SimpleKeyGenerator;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

public class TenantKeyGenerator extends SimpleKeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        return
            "tenant#"+ThreadLocalStorage.getTenant() +
            ":project#" + ThreadLocalStorage.getProject() +
            ":request#" + Hashing.sha256().hashString(super.generateKey(params).toString(), StandardCharsets.UTF_8).toString();
    }

}
