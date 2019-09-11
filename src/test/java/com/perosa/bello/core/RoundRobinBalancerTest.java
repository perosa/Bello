package com.perosa.bello.core;

import com.perosa.bello.core.resource.ResourceHost;
import com.perosa.bello.core.resource.SessionCache;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoundRobinBalancerTest {

    @Mock
    SessionCache sessionCache;

    @Test
    void findNext() {
        List<ResourceHost> hosts = new ArrayList<>();

        RoundRobinBalancer roundRobinBalancer = new RoundRobinBalancer(sessionCache);
        roundRobinBalancer.reset();

        hosts.add(new ResourceHost("localhost1"));
        hosts.add(new ResourceHost("localhost2"));
        hosts.add(new ResourceHost("localhost3"));

        ResourceHost resourceHost = roundRobinBalancer.findNext(hosts);

        assertNotNull(resourceHost);
        assertEquals("localhost1", resourceHost.getHost());

    }

    @Test
    void findAfterNext() {
        List<ResourceHost> hosts = new ArrayList<>();

        RoundRobinBalancer roundRobinBalancer = new RoundRobinBalancer(sessionCache);
        roundRobinBalancer.reset();

        hosts.add(new ResourceHost("localhost1"));
        hosts.add(new ResourceHost("localhost2"));
        hosts.add(new ResourceHost("localhost3"));

        ResourceHost resourceHost = null;
        resourceHost = roundRobinBalancer.findNext(hosts);
        resourceHost = roundRobinBalancer.findNext(hosts);

        assertNotNull(resourceHost);
        assertEquals("localhost2", resourceHost.getHost());

    }

    @Test
    void findAfterRestartQueue() {
        List<ResourceHost> hosts = new ArrayList<>();

        RoundRobinBalancer roundRobinBalancer = new RoundRobinBalancer(sessionCache);
        roundRobinBalancer.reset();

        hosts.add(new ResourceHost("localhost1"));
        hosts.add(new ResourceHost("localhost2"));

        ResourceHost resourceHost = null;
        resourceHost = roundRobinBalancer.findNext(hosts);
        resourceHost = roundRobinBalancer.findNext(hosts);
        resourceHost = roundRobinBalancer.findNext(hosts);

        assertNotNull(resourceHost);
        assertEquals("localhost1", resourceHost.getHost());

    }

}