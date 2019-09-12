package com.perosa.bello.core.resource.session;

import com.perosa.bello.core.resource.session.provider.InMemSessionCache;

public interface SessionCache {

    SessionInfo get(String sessionId);

    void put(String sessionId, SessionInfo sessionInfo);

    static SessionCache make() {
        return new InMemSessionCache();
    }

}