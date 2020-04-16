package de.mycrocast.dtalisty.messaging.request;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

public class GetRequest<T> extends Request<T> {
    private final Gson gson = new Gson();
    private final Type type;
    private final Response.Listener<T> listener;

    public GetRequest(String url, Type type,
                      Response.Listener<T> listener,
                      Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.type = type;
        this.listener = listener;
    }

    @Override
    protected void deliverResponse(T response) {
        this.listener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(
                    (T) (gson.fromJson(json, this.type)),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException | JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
}
