package com.perosa.bello.core.channel.platform;

import com.perosa.bello.core.channel.Channel;
import com.perosa.bello.core.channel.ChannelProcessor;
import com.perosa.bello.core.util.JsonUtil;
import com.perosa.bello.server.InRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ChatfuelChannel extends ChannelProcessor implements Channel {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatfuelChannel.class);

    private static final String ID = "messenger user id";

    public ChatfuelChannel() {
    }

    public String extract(InRequest request) {

        String ret = null;

        String id = extractFromParameters(request.getParameters());

        if (id == null) {
            ret = extractFromBody(request.getPayload());
        }

        return ret;
    }

    String extractFromParameters(Map<String, String[]> parameters) {
        String ret = null;

        if(parameters.get(ID) != null) {
            ret = parameters.get(ID)[0];
        }

        return ret;
    }

    String extractFromBody(String body) {
        String ret = null;

        String id = JsonUtil.findElement("/" + ID, body);

        if (!id.isEmpty()) {
            ret = id;
        }

        return ret;
    }

}
