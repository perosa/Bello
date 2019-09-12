package com.perosa.bello.core.resource.session;

import java.util.HashMap;
import java.util.Map;

public class InMemSessionCache implements SessionCache {

    static Map<String, SessionInfo> map = new HashMap<>();

    @Override
    public SessionInfo get(String sessionId) {
        return map.get(sessionId);
    }

    @Override
    public void put(String sessionId, SessionInfo sessionInfo) {
        if(sessionId != null) {
            map.put(sessionId, sessionInfo);
        }
    }
}
