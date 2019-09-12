package com.perosa.bello.core.resource.session;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemSessionCacheTest {

    InMemSessionCache sessionCache = new InMemSessionCache();

    @Test
    void get() {
        InMemSessionCache.map.put("01", new SessionInfo("01" ,"localhost"));

        assertNotNull(sessionCache.get("01"));
    }

    @Test
    void getFromEmptyMap() {
        InMemSessionCache.map.clear();

        assertNull(sessionCache.get("01"));
    }

    @Test
    void getWithNullKey() {
        assertNull(sessionCache.get(null));
    }

    @Test
    void put() {
        sessionCache.put("02", new SessionInfo("02" ,"host.perosa.com"));

        assertNotNull(InMemSessionCache.map.get("02"));
    }

    @Test
    void putWithNullKey() {
        InMemSessionCache.map.clear();

        sessionCache.put(null, new SessionInfo("02" ,"host.perosa.com"));

        assertTrue(InMemSessionCache.map.isEmpty());
    }

}