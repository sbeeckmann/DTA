package de.mycrocast.rest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button restGet = this.findViewById(R.id.get);
        restGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                MainActivity.this.runOnUiThread(() -> {
                                    Toast.makeText(MainActivity.this, "Error " + response, Toast.LENGTH_SHORT).show();
                                });
                            }

                            InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8);
                            BufferedReader reader = new BufferedReader(inputStreamReader);
                            StringBuffer sb = new StringBuffer();
                            String str = "";
                            while ((str = reader.readLine()) != null) {
                                sb.append(str);
                            }

                            JSONArray array = new JSONArray(sb.toString());

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject element = (JSONObject) array.get(0);
                                String name = element.getString("name");
                                System.out.println(name);
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        Button restPost = this.findViewById(R.id.post);
        restPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL getEndpoint = new URL("http://10.0.2.2:8080/rest/entry/createEntryHolder");
                            HttpURLConnection connection = (HttpURLConnection) getEndpoint.openConnection();
                            connection.setRequestProperty("Content-Type", "application/json");
                            connection.setRequestMethod("POST");

                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("name", "testerName");

                            String request = jsonObject.toString();
                            connection.setDoOutput(true);
                            connection.getOutputStream().write(request.getBytes());

                            int response = connection.getResponseCode();
                            if (response == 200) {
                                MainActivity.this.runOnUiThread(() -> {
                                    Toast.makeText(MainActivity.this, "Success!! " + response, Toast.LENGTH_SHORT).show();
                                });
                            } else {
                                MainActivity.this.runOnUiThread(() -> {
                                    Toast.makeText(MainActivity.this, "Error " + response, Toast.LENGTH_SHORT).show();
                                });
                            }

                            InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8);
                            BufferedReader reader = new BufferedReader(inputStreamReader);
                            StringBuffer sb = new StringBuffer();
                            String str = "";
                            while ((str = reader.readLine()) != null) {
                                sb.append(str);
                            }

                            JSONObject responseJson = new JSONObject(sb.toString());

                            long id = responseJson.getLong("id");
                            System.out.println(id);

                            MainActivity.this.runOnUiThread(() -> {
                                Toast.makeText(MainActivity.this, "EntryHolder: " + id + " created", Toast.LENGTH_LONG).show();
                            });

                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}
