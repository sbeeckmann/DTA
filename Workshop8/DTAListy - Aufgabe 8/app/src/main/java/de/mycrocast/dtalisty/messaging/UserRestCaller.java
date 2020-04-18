package de.mycrocast.dtalisty.messaging;

import com.android.volley.RequestQueue;
import com.android.volley.Response;

import de.mycrocast.dtalisty.data.Entry;
import de.mycrocast.dtalisty.messaging.response.BasicResponse;

public class UserRestCaller {
    private final static String BEAN_EXTENSION = "user";

    private final RequestQueue requestQueue;
    private final ServerConfig serverConfig;

    public UserRestCaller(RequestQueue requestQueue, ServerConfig serverConfig) {
        this.requestQueue = requestQueue;
        this.serverConfig = serverConfig;
    }

    public void login(String username, String password, Response.Listener<BasicResponse<User>> successListener, Response.ErrorListener errorListener) {

    }
}
