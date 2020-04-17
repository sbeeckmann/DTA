package de.mycrocast.dtalisty.messaging;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import de.mycrocast.dtalisty.data.Entry;
import de.mycrocast.dtalisty.data.EntryHolder;
import de.mycrocast.dtalisty.messaging.request.CreateEntryRequestParameter;
import de.mycrocast.dtalisty.messaging.request.DeleteEntryRequestParameter;
import de.mycrocast.dtalisty.messaging.request.GetRequest;
import de.mycrocast.dtalisty.messaging.request.PostRequest;
import de.mycrocast.dtalisty.messaging.response.BasicResponse;

public class RequestManager {
    private final RequestQueue requestQueue;

    public RequestManager(Context context) {
        this.requestQueue = Volley.newRequestQueue(context);
    }

    public void createEntry(long entryHolderId, String name, Entry.Priority priority,
                            Response.Listener<BasicResponse<Entry>> successListener, Response.ErrorListener errorListener) {
        String url = "http://192.168.0.18:8080/rest/entry/createEntry";

        CreateEntryRequestParameter requestParameter = new CreateEntryRequestParameter(entryHolderId, name, priority);
        String body = new Gson().toJson(requestParameter);

        Type type = new TypeToken<BasicResponse<Entry>>(){}.getType();
        PostRequest<BasicResponse<Entry>> postRequest = new PostRequest<>(url, body, type, successListener, errorListener);
        this.requestQueue.add(postRequest);
    }

    public void updateEntry(long entryHolderId, String name, Entry.Priority priority,
                            Response.Listener<BasicResponse<Entry>> successListener, Response.ErrorListener errorListener) {
    }

    public void deleteEntry(long entryId, Response.Listener<BasicResponse<Entry>> successListener, Response.ErrorListener errorListener) {
        String url = "http://192.168.0.18:8080/rest/entry/deleteEntry";

        DeleteEntryRequestParameter requestParameter = new DeleteEntryRequestParameter(entryId);
        String body = new Gson().toJson(requestParameter);

        Type type = new TypeToken<BasicResponse<Entry>>(){}.getType();
        PostRequest<BasicResponse<Entry>> postRequest = new PostRequest<>(url, body, type, successListener, errorListener);
        this.requestQueue.add(postRequest);
    }

    public void getEntries(Response.Listener<BasicResponse<List<EntryHolder>>> successListener, Response.ErrorListener errorListener) {
        String url = "http://192.168.0.18:8080/rest/entry/getEntries";

        Type type = new TypeToken<BasicResponse<List<EntryHolder>>>(){}.getType();
        GetRequest<BasicResponse<List<EntryHolder>>> getRequest = new GetRequest<>(url, type, successListener, errorListener);
        this.requestQueue.add(getRequest);
    }
}
