package de.mycrocast.dtalisty.messaging.restcaller;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

import de.mycrocast.dtalisty.messaging.ServerConfig;

public abstract class AbstractRestCaller {

    private final String beanExtension;
    private final RequestQueue requestQueue;
    private final ServerConfig serverConfig;

    protected AbstractRestCaller(String beanExtension, RequestQueue requestQueue, ServerConfig serverConfig) {
        this.beanExtension = beanExtension;
        this.requestQueue = requestQueue;
        this.serverConfig = serverConfig;
    }

    protected String createRestEndpoint(String restExtension) {
        return this.serverConfig.createRestEndpoint(this.beanExtension, restExtension);
    }

    protected void sendRequest(Request<?> request) {
        this.requestQueue.add(request);
    }
}
