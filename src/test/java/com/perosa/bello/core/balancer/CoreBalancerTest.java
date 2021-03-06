package com.perosa.bello.core.balancer;

import com.perosa.bello.core.resource.ResourceHost;
import com.perosa.bello.core.resource.session.SessionCache;
import com.perosa.bello.core.channel.Channel;
import com.perosa.bello.core.resource.session.SessionInfo;
import com.perosa.bello.server.InRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoreBalancerTest {

    @Mock
    SessionCache sessionCache;
    @Mock
    Channel channel;

    @Test
    void findTarget() {

        when(channel.extract(isA(InRequest.class))).thenReturn("s01");

        InRequest request = new InRequest();
        request.setHost("localhost");
        request.setPayload("todo");

        String target = new LocalCoreBalancer(sessionCache, channel).findTarget(request);

        assertNotNull(target);
        verify(sessionCache, times(1)).get(isA(String.class));
        verify(channel, times(1)).extract(isA(InRequest.class));
        verify(sessionCache, times(1)).put(isA(String.class), isA(SessionInfo.class));

    }

    @Test
    void findTargetWIthSessionIdNotAvail() {

        when(channel.extract(isA(InRequest.class))).thenReturn(null);

        InRequest request = new InRequest();
        request.setHost("localhost");
        request.setPayload("todo");

        String target = new LocalCoreBalancer(sessionCache, channel).findTarget(request);

        assertNotNull(target);
        verify(sessionCache, times(0)).get(isA(String.class));
        verify(channel, times(1)).extract(isA(InRequest.class));

    }

    @Test
    void findTargetFromCache() {

        when(channel.extract(isA(InRequest.class))).thenReturn("s01");
        when(sessionCache.get(eq("s01"))).thenReturn(new SessionInfo("s01", "myhost"));

        InRequest request = new InRequest();
        request.setHost("localhost");
        request.setPayload("todo");

        String target = new LocalCoreBalancer(sessionCache, channel).findTarget(request);

        assertNotNull(target);
        assertEquals("myhost", target);
        verify(sessionCache, times(1)).get(isA(String.class));
        verify(channel, times(1)).extract(isA(InRequest.class));
        verify(sessionCache, times(1)).put(isA(String.class), isA(SessionInfo.class));

    }

    @Test
    void extractSessionId() {

        when(channel.extract(isA(InRequest.class))).thenReturn("s01");

        InRequest request = new InRequest();
        request.setHost("localhost");
        request.setPayload("todo");

        String sessionId = new LocalCoreBalancer(sessionCache, channel).extractSessionId(request);

        assertNotNull(sessionId);
        assertEquals("s01", sessionId);
        verify(channel, times(1)).extract(isA(InRequest.class));

    }

    @Test
    void getAvailableHosts() {

        List<ResourceHost> hosts = new ArrayList<>();

        hosts.add(new ResourceHost("localhost1"));
        hosts.add(new ResourceHost("localhost2"));
        hosts.add(new ResourceHost("localhost3", 0));

        List<ResourceHost> list = new LocalCoreBalancer(sessionCache, channel).getAvailableHosts(hosts);

        assertNotNull(list);
        assertEquals(2, list.size());
    }

    @Test
    void getAvailableHostsIgnoreOffline() {

        List<ResourceHost> hosts = new ArrayList<>();

        hosts.add(new ResourceHost("localhost1"));
        hosts.add(new ResourceHost("localhost2"));
        hosts.add(new ResourceHost("localhost3", 1, 0));

        List<ResourceHost> list = new LocalCoreBalancer(sessionCache, channel).getAvailableHosts(hosts);

        assertNotNull(list);
        assertEquals(2, list.size());
    }

    @Test
    void noAvailableHosts() {

        List<ResourceHost> hosts = new ArrayList<>();

        hosts.add(new ResourceHost("localhost1", 0));
        hosts.add(new ResourceHost("localhost2", 0));
        hosts.add(new ResourceHost("localhost3", 0));


        Assertions.assertThrows(RuntimeException.class, () -> {
            List<ResourceHost> list = new LocalCoreBalancer(sessionCache, channel).getAvailableHosts(hosts);
        });

    }

    @Test
    void get() {
        SessionInfo sessionInfo = new LocalCoreBalancer(sessionCache, channel).get("s01");
        verify(sessionCache, times(1)).get(isA(String.class));
    }

    @Test
    void put() {
        new LocalCoreBalancer(sessionCache, channel).put("s01", new SessionInfo("s01", "localhost"));
        verify(sessionCache, times(1)).put(isA(String.class), isA(SessionInfo.class));
    }

}

class LocalCoreBalancer extends CoreBalancer {

    LocalCoreBalancer(SessionCache sessionCache, Channel channel) {
        super(sessionCache, channel);
    }

    public ResourceHost findNext(List<ResourceHost> hosts) {
        return new ResourceHost("localhost");
    }
}