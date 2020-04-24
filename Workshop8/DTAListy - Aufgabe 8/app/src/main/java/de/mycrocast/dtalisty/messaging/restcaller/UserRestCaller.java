package de.mycrocast.dtalisty.messaging.restcaller;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import de.mycrocast.dtalisty.data.Entry;
import de.mycrocast.dtalisty.data.User;
import de.mycrocast.dtalisty.messaging.ServerConfig;
import de.mycrocast.dtalisty.messaging.request.PostRequest;
import de.mycrocast.dtalisty.messaging.request.parameter.user.AuthenticateUserRequestParameter;
import de.mycrocast.dtalisty.messaging.request.parameter.user.RegisterUserRequestParameter;
import de.mycrocast.dtalisty.messaging.response.BasicResponse;

public class UserRestCaller extends AbstractRestCaller {
    private final static String BEAN_EXTENSION = "user";

    public UserRestCaller(RequestQueue requestQueue, ServerConfig serverConfig) {
        super(BEAN_EXTENSION, requestQueue, serverConfig);
    }

    public void authenticate(String username, String password, Response.Listener<BasicResponse<User>> successListener, Response.ErrorListener errorListener) {
        String url = this.createRestEndpoint("authenticate");

        AuthenticateUserRequestParameter requestParameter = new AuthenticateUserRequestParameter(username, password);
        String body = new Gson().toJson(requestParameter);

        Type type = new TypeToken<BasicResponse<Entry>>() {}.getType();
        PostRequest<BasicResponse<User>> postRequest = new PostRequest<>(url, body, type, successListener, errorListener);
        this.sendRequest(postRequest);
    }

    public void register(String username, String password, Response.Listener<BasicResponse<User>> successListener, Response.ErrorListener errorListener) {
        String url = this.createRestEndpoint("register");

        RegisterUserRequestParameter requestParameter = new RegisterUserRequestParameter(username, password);
        String body = new Gson().toJson(requestParameter);

        Type type = new TypeToken<BasicResponse<Entry>>() {}.getType();
        PostRequest<BasicResponse<User>> postRequest = new PostRequest<>(url, body, type, successListener, errorListener);
        this.sendRequest(postRequest);
    }
}
