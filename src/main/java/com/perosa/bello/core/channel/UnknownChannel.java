package com.perosa.bello.core.channel;

import com.perosa.bello.server.InRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnknownChannel extends ChannelProcessor implements Channel {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnknownChannel.class);

    public UnknownChannel() {
    }

    public String extract(InRequest request) {
        return null;
    }

}
