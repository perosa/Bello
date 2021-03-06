package com.perosa.bello.core.resource.healthcheck;

import com.perosa.bello.core.resource.ResourceHost;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResourceHealthCheckTest {

    @Mock
    private HealthCheckClient mock;

    @Test
    void ping() throws Exception {

        ResourceHealthCheck resourceHealthCheck = new ResourceHealthCheck(mock);
        when(mock.ping("http://host.perosa.com/ping")).thenReturn(200);

        ResourceHost resourceHost = new ResourceHost("host.perosa.com");
        resourceHost.setHealthCheck("/ping");

        resourceHealthCheck.ping(resourceHost);

    }

    @Test
    void pingUnavailable() throws Exception {

        ResourceHealthCheck resourceHealthCheck = new ResourceHealthCheck(mock);
        when(mock.ping("http://host3.perosa.com/ping")).thenReturn(404);

        ResourceHost resourceHost = new ResourceHost("host3.perosa.com");
        resourceHost.setHealthCheck("/ping");

        resourceHealthCheck.ping(resourceHost);

    }

    @Test
    void getHealthCheckUrl() {

        ResourceHealthCheck resourceHealthCheck = new ResourceHealthCheck(mock);

        ResourceHost resourceHost = new ResourceHost("host.perosa.com");
        resourceHost.setHealthCheck("/ping");

        assertEquals("http://host.perosa.com/ping", resourceHealthCheck.getHealthCheckUrl(resourceHost));
    }

    @Test
    void runHealthCheck() throws Exception {

        ResourceHealthCheck resourceHealthCheck = new ResourceHealthCheck(mock);
        when(mock.ping(isA(String.class))).thenReturn(200);

        resourceHealthCheck.runHealthCheck();

        verify(mock, times(4)).ping(isA(String.class));

    }

    @Test
    void getHealthCheckHttpScheme() throws Exception {

        ResourceHealthCheck resourceHealthCheck = new ResourceHealthCheck(mock);
        ResourceHost resourceHost = new ResourceHost("host.perosa.com");

        String scheme = resourceHealthCheck.getHealthCheckScheme(resourceHost);

        assertEquals("http", scheme);
    }

    @Test
    void getHealthCheckHttpsScheme() throws Exception {

        ResourceHealthCheck resourceHealthCheck = new ResourceHealthCheck(mock);
        ResourceHost resourceHost = new ResourceHost("host.perosa.com");
        resourceHost.setHealthCheckScheme("https");

        String scheme = resourceHealthCheck.getHealthCheckScheme(resourceHost);

        assertEquals("https", scheme);
    }
}