package org.bmsource.dwh.common.appstate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class AppStateService {

    private static final String APP_STATE_CHANNEL = "appState";

    @Autowired
    RedisTemplate<String, Object> template;

    private String buildStateKey(String channel, String tenant, String project) {
        return buildKeyPrefix(tenant, project)
            .append(":channel#")
            .append(channel)
            .toString();
    }

    String buildTopicKey(String tenant, String project, String topic) {
        return buildKeyPrefix(tenant, project)
            .append(":topic#")
            .append(topic)
            .toString();
    }

    private StringBuilder buildKeyPrefix(String tenant, String project) {
        return new StringBuilder()
            .append(APP_STATE_CHANNEL)
            .append(":tenant#").append(tenant)
            .append(":project#").append(project);
    }

    public void updateState(String tenant, String project, String stateType, Map<String, Object> state) {
        String stateKey = buildStateKey(stateType, tenant, project);
        String topic = buildTopicKey(tenant, project, stateType);
        template.opsForHash().putAll(stateKey, state);
        template.expire(stateKey, 10, TimeUnit.SECONDS);
        template.convertAndSend(topic, tenant + ":" + project);
    }

    Map<String, Map<String, Object>> getState(String tenant, String project) {
        Map<String, Map<String, Object>> result = new LinkedHashMap<>();
        String key = buildKeyPrefix(tenant, project).append("*").toString();
        Set<String> keys = template.keys(key);
        for (String entryKey : keys) {
            String[] tokens = entryKey.split(":");
            String[] keyVals = tokens[tokens.length - 1].split("#");
            Map<String, Object> entry = template.<String, Object>opsForHash().entries(entryKey);
            result.put(keyVals[1], entry);
        }
        return result;
    }

    public Map<String, Object> getState(String tenant, String project, String stateType) {
        return template.<String, Object>opsForHash().entries(buildStateKey(stateType, tenant, project));
    }
}
