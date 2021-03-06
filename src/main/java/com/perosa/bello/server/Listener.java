package com.perosa.bello.server;

import com.perosa.bello.core.config.Env;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Listener {

    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);

    private static Undertow builder = null;
    private DispatchLogic dispatchLogic = null;

    public Listener(DispatchLogic dispatchLogic) {
        this.dispatchLogic = dispatchLogic;
    }

    public void setUp() {

        final String host = "0.0.0.0";
        final int port = getPort();

        try {

            if (builder == null) {
                LOGGER.info("listening on port " + port);

                builder = Undertow.builder()
                        .addHttpListener(port, host)
                        .setHandler(new HttpHandler() {
                            @Override
                            public void handleRequest(HttpServerExchange exchange) throws Exception {

                                LOGGER.debug("exchange " + exchange);

                                try {
                                    if(isTest(exchange)) {
                                        sendReply(exchange);
                                    } else {
                                        getDispatchLogic().dispatch(exchange);
                                    }
                                } catch (Exception e) {
                                    exchange.setStatusCode(404);
                                }

                            }
                        }).build();

                builder.start();
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

    }

    boolean isTest(HttpServerExchange exchange) {
        return exchange.getRequestPath() != null && exchange.getRequestPath().endsWith("/belloadc/test");
    }

    void sendReply(HttpServerExchange exchange) {
        exchange.setStatusCode(200);
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
        exchange.getResponseSender().send("Ok");
    }

    public int getPort() {
        return new Env().getPort();
    }

    public DispatchLogic getDispatchLogic() {
        return dispatchLogic;
    }

    public void setDispatchLogic(DispatchLogic dispatchLogic) {
        this.dispatchLogic = dispatchLogic;
    }
}
