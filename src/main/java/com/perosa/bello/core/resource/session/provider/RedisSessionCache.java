package com.perosa.bello.core.resource.session.provider;

import com.perosa.bello.core.config.Env;
import com.perosa.bello.core.resource.session.SessionCache;
import com.perosa.bello.core.resource.session.SessionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class RedisSessionCache implements SessionCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisSessionCache.class);

    private Env env;

    private Jedis jedis;

    public RedisSessionCache(Jedis jedis) {
        this.env = new Env();
        this.jedis = jedis;
    }

    @Override
    public SessionInfo get(String sessionId) {
        SessionInfo sessionInfo = null;

        String id = getJedis().hget(sessionId, "id");

        if(id != null) {
            sessionInfo = new SessionInfo();

            sessionInfo.setId(id);
            sessionInfo.setHost(getJedis().hget(sessionId, "host"));
            sessionInfo.setDate(writeToLocalDateTime(getJedis().hget(sessionId, "date")));
            sessionInfo.setChannel(getJedis().hget(sessionId, "channel"));
        }

        return sessionInfo;
    }

    @Override
    public void put(String sessionId, SessionInfo sessionInfo) {

        if (sessionId != null) {
            getJedis().hset(sessionId, "id", sessionInfo.getId());
            getJedis().hset(sessionId, "host", sessionInfo.getHost());
            getJedis().hset(sessionId, "date", writeToString(sessionInfo.getDate()));
            getJedis().hset(sessionId, "channel", sessionInfo.getChannel());

            getJedis().expire(sessionId, 60 * 30);
        }
    }

    @Override
    public void remove(String sessionId) {
        SessionInfo sessionInfo = null;

        long ret = getJedis().del(sessionId);

        if(ret != 1) {
            LOGGER.warn("Error while DEL from Redis keyy:" + sessionId + " ret:" + ret);
        }
    }


    @Override
    public int size() {
        return getJedis().keys("*").size();
    }

    @Override
    public Map<String, SessionInfo> getMap() {
        Map<String, SessionInfo> map = new HashMap<>();

        Set<String> set = getJedis().keys("*");

        set.stream().forEach(e -> map.put(e, get(e)));

        return map;
    }


    String writeToString(LocalDateTime localDateTime) {
        return (localDateTime != null ? localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : "");
    }

    LocalDateTime writeToLocalDateTime(String string) {
        return (string != null ? LocalDateTime.parse(string) : null);
    }

    public Env getEnv() {
        return env;
    }

    public void setEnv(Env env) {
        this.env = env;
    }

    public Jedis getJedis() {
        return jedis;
    }

    public void setJedis(Jedis jedis) {
        this.jedis = jedis;
    }
}
