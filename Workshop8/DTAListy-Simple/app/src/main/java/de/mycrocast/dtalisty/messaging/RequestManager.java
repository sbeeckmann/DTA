package de.mycrocast.dtalisty.messaging;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import de.mycrocast.dtalisty.data.Entry;
import de.mycrocast.dtalisty.data.EntryHolder;

public class RequestManager {

    public interface SuccessResponse<T> {
        void onSuccess(T response);
    }

    public interface Error {
        void onError(String error);
    }

    public void createEntry(String name, Entry.Priority priority, long entryHolderId, SuccessResponse<Entry> successResponse, Error error) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {

                    URL getEndpoint = new URL("http://10.0.2.2:8080/rest/entry/createEntry");
                    HttpURLConnection connection = (HttpURLConnection) getEndpoint.openConnection();
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestMethod("POST");

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("name", name);
                    jsonObject.put("priority", priority.toString());
                    jsonObject.put("entryHolderId", entryHolderId);

                    String request = jsonObject.toString();
                    connection.setDoOutput(true);
                    connection.getOutputStream().write(request.getBytes());

                    int response = connection.getResponseCode();
                    if (response == 200) {

                    } else {
                        error.onError("Error " + response);
                    }

                    InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8);
                    BufferedReader reader = new BufferedReader(inputStreamReader);
                    StringBuffer sb = new StringBuffer();
                    String str = "";
                    while ((str = reader.readLine()) != null) {
                        sb.append(str);
                    }

                    JSONObject object = new JSONObject(sb.toString());
                    String errorValue = object.getString("error");
                    if (!errorValue.equals("null") && !errorValue.isEmpty()) {
                        error.onError(errorValue);
                        return;
                    }

                    JSONObject entryElement = object.getJSONObject("responseData");
                    String entryName = entryElement.getString("name");
                    long entryId = entryElement.getLong("id");
                    Entry.Priority priority = Entry.Priority.valueOf(entryElement.getString("priority"));
                    boolean active = entryElement.getBoolean("active");
                    long creationTime = entryElement.getLong("creationTime");

                    Entry entry = new Entry();
                    entry.setActive(active);
                    entry.setPriority(priority);
                    entry.setId(entryId);
                    entry.setName(entryName);
                    entry.setCreationTime(creationTime);

                    successResponse.onSuccess(entry);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void requestEntryHolders(SuccessResponse<List<EntryHolder>> successResponse, Error error) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL getEndpoint = new URL("http://10.0.2.2:8080/rest/entry/getEntries");
                    HttpURLConnection connection = (HttpURLConnection) getEndpoint.openConnection();
                    connection.setRequestProperty("Content-Type", "application/json");
                    int response = connection.getResponseCode();
                    if (response == 200) {

                    } else {
                        error.onError("Error " + response);
                    }

                    InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8);
                    BufferedReader reader = new BufferedReader(inputStreamReader);
                    StringBuffer sb = new StringBuffer();
                    String str = "";
                    while ((str = reader.readLine()) != null) {
                        sb.append(str);
                    }

                    JSONObject object = new JSONObject(sb.toString());
                    String errorValue = object.getString("error");
                    if (!errorValue.equals("null") && !errorValue.isEmpty()) {
                        error.onError(errorValue);
                        return;
                    }

                    JSONArray array = object.getJSONArray("responseData");
                    List<EntryHolder> holders = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject element = (JSONObject) array.get(i);

                        EntryHolder entryHolder = new EntryHolder();
                        String name = element.getString("name");
                        entryHolder.setName(name);

                        long id = element.getLong("id");
                        entryHolder.setId(id);

                        List<Entry> entries = new ArrayList<>();
                        JSONArray jsonEntries = element.getJSONArray("entries");
                        for (int j = 0; j < jsonEntries.length(); j++) {
                            JSONObject entryElement = (JSONObject) jsonEntries.get(j);
                            String entryName = entryElement.getString("name");
                            long entryId = entryElement.getLong("id");
                            Entry.Priority priority = Entry.Priority.valueOf(entryElement.getString("priority"));
                            boolean active = entryElement.getBoolean("active");
                            long creationTime = entryElement.getLong("creationTime");

                            Entry entry = new Entry();
                            entry.setActive(active);
                            entry.setPriority(priority);
                            entry.setId(entryId);
                            entry.setName(entryName);
                            entry.setCreationTime(creationTime);

                            entries.add(entry);
                        }
                        entryHolder.setEntries(entries);
                        holders.add(entryHolder);
                    }
                    successResponse.onSuccess(holders);
                } catch (IOException | JSONException e) {

                }
            }
        });
    }
}