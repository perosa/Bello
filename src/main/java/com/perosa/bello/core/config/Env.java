package com.perosa.bello.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Env {

    private static final Logger LOGGER = LoggerFactory.getLogger(Env.class);

    public String getConfig() {
        String config = System.getProperty("config");

        if (config == null || config.isEmpty()) {
            config = "config/";
        }

        if (!config.endsWith("/")) {
            config = config + "/";
        }

        return config;
    }

    public int getPort() {
        String port = System.getProperty("port");

        if (port == null || port.isEmpty()) {
            port = System.getenv("PORT");
        }

        if (port == null || port.isEmpty()) {
            port = "8888";
        }


        return Integer.valueOf(port);
    }

}
