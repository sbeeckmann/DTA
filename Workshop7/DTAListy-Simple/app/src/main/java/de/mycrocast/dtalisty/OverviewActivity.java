package de.mycrocast.dtalisty;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

import de.mycrocast.dtalisty.adapter.EntryHolderAdapter;
import de.mycrocast.dtalisty.adapter.RecyclerClickListener;
import de.mycrocast.dtalisty.data.Entry;
import de.mycrocast.dtalisty.data.EntryHolder;
import de.mycrocast.dtalisty.dialogs.EntryHolderCreateDialog;
import de.mycrocast.dtalisty.dialogs.EntryHolderEditDialog;

public class OverviewActivity extends AppCompatActivity implements RecyclerClickListener, EntryHolderCreateDialog.OnEntryHolderCreated, EntryHolderEditDialog.OnEntryHolderEdit {

    private EntryHolderAdapter holderAdapter;
    private List<EntryHolder> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_overview);

        this.data = new ArrayList<>();

        boolean portrait = this.getResources().getBoolean(R.bool.port);
        int numColumns = 2;
        if (!portrait) {
            numColumns = 3;
        }
        RecyclerView recyclerView = this.findViewById(R.id.holder);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, numColumns);
        recyclerView.setLayoutManager(gridLayoutManager);

        this.holderAdapter = new EntryHolderAdapter(this.data);
        this.holderAdapter.setClickListener(this);
        recyclerView.setAdapter(this.holderAdapter);

        FloatingActionButton fab = this.findViewById(R.id.addButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = OverviewActivity.this.getSupportFragmentManager();
                EntryHolderCreateDialog createDialog = new EntryHolderCreateDialog(OverviewActivity.this);

                createDialog.setCancelable(false);
                createDialog.show(fragmentManager, "entryDialog");
            }
        });

        this.requestEntries();
    }

    private void requestEntries() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    data.clear();

                    URL getEndpoint = new URL("http://10.0.2.2:8080/rest/entry/getEntries");
                    HttpURLConnection connection = (HttpURLConnection) getEndpoint.openConnection();
                    connection.setRequestProperty("Content-Type", "application/json");
                    int response = connection.getResponseCode();
                    if (response == 200) {

                    } else {
                        OverviewActivity.this.runOnUiThread(() -> {
                            Toast.makeText(OverviewActivity.this, "Error " + response, Toast.LENGTH_SHORT).show();
                        });
                    }

                    InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8);
                    BufferedReader reader = new BufferedReader(inputStreamReader);
                    StringBuffer sb = new StringBuffer();
                    String str = "";
                    while ((str = reader.readLine()) != null) {
                        sb.append(str);
                    }

                    JSONObject object = new JSONObject(sb.toString());
                    String error = object.getString("error");
                    if (!error.isEmpty()) {
                        OverviewActivity.this.runOnUiThread(() -> {
                            Toast.makeText(OverviewActivity.this, "Error " + error, Toast.LENGTH_SHORT).show();
                        });
                        return;
                    }

                    JSONArray array = object.getJSONArray("responseData");

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
                        data.add(entryHolder);
                    }
                    holderAdapter.notifyDataSetChanged();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onEntryClick(View view, int entryIndex) {
        EntryHolder holderClicked = this.data.get(entryIndex);
        Intent intent = new Intent(OverviewActivity.this, MainActivity.class);
        intent.putExtra("list", holderClicked.getId());
        this.startActivity(intent);
    }

    @Override
    public void onDeleteClick(View view, final int entryIndex) {
        final EntryHolder entryHolder = this.data.get(entryIndex);
        new AlertDialog.Builder(OverviewActivity.this)
                .setMessage("Eintrag: " + entryHolder.getName() + " löschen?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO: Create a new deleteEntryHolder method in RequestManager, that sends the DeleteEntryHolderRequest to the backend server

                        OverviewActivity.this.data.remove(entryHolder);
                        OverviewActivity.this.holderAdapter.notifyItemRemoved(entryIndex);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    @Override
    public void onEditClick(View view, int entryIndex) {
        EntryHolder toUpdate = this.data.get(entryIndex);
        FragmentManager fragmentManager = OverviewActivity.this.getSupportFragmentManager();
        EntryHolderEditDialog entryHolderEditDialog = new EntryHolderEditDialog(OverviewActivity.this, toUpdate, entryIndex);
        entryHolderEditDialog.setCancelable(false);
        entryHolderEditDialog.show(fragmentManager, "entryHolderEdit");
    }

    @Override
    public void onEntryHolderCreated(String name) {
        // TODO: Create a new createEntryHolder method in RequestManager, that sends the CreateEntryHolderRequest to the backend server

        EntryHolder holder = new EntryHolder();
        holder.setName(name);

        this.data.add(holder);
        this.holderAdapter.notifyDataSetChanged();
    }

    @Override
    public void onEntryHolderEdit(EntryHolder name, int index) {
        // TODO: Create a new editEntryHolder method in RequestManager, that sends the EditEntryHolderRequest to the backend server

        this.data.set(index, name);

        this.holderAdapter.notifyDataSetChanged();
    }
}
