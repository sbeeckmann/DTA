package de.mycrocast.dtalisty;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import de.mycrocast.dtalisty.adapter.EntryAdapter;
import de.mycrocast.dtalisty.adapter.RecyclerClickListener;
import de.mycrocast.dtalisty.data.Entry;
import de.mycrocast.dtalisty.dialogs.EntryCreateDialog;
import de.mycrocast.dtalisty.dialogs.EntryEditDialog;

public class MainActivity extends AppCompatActivity implements RecyclerClickListener, EntryCreateDialog.OnEntryCreated, EntryEditDialog.OnEntryEdited {

    private RecyclerView entryRecyclerView;
    private EntryAdapter entryAdapter;

    private List<Entry> entryData;
    private long entryHolderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        FloatingActionButton fab = this.findViewById(R.id.addButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
                EntryCreateDialog createDialog = new EntryCreateDialog(MainActivity.this);

                createDialog.setCancelable(false);
                createDialog.show(fragmentManager, "entryDialog");
            }
        });

        this.entryRecyclerView = this.findViewById(R.id.entries);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        this.entryRecyclerView.setLayoutManager(linearLayoutManager);

        this.entryData = this.createDummyData();
        this.entryAdapter = new EntryAdapter(this.entryData);
        this.entryAdapter.setClickListener(this);

        this.entryRecyclerView.setAdapter(this.entryAdapter);

        this.sortEntries();

        this.entryHolderId = this.getIntent().getExtras().getLong("list");
        System.out.println(this.entryHolderId);
    }

    private void sortEntries() {
        Collections.sort(this.entryData, new Comparator<Entry>() {
            @Override
            public int compare(Entry o1, Entry o2) {
                switch (o1.getPriority()) {
                    case HIGH: {
                        if (!o1.isActive() && !o2.isActive()) {
                            if (o2.getPriority() != Entry.Priority.HIGH) {
                                return -1;
                            }
                            return 0;
                        }
                        if (o1.isActive() && !o2.isActive()) {
                            return -1;
                        }

                        if (!o1.isActive() && o2.isActive()) {
                            return 1;
                        }
                        if (o1.isActive() && o2.isActive()) {
                            if (o2.getPriority() == Entry.Priority.HIGH) {
                                return 0;
                            }
                            return -1;
                        }
                        break;
                    }
                    case MEDIUM: {
                        if (!o1.isActive() && !o2.isActive()) {
                            if (o2.getPriority() == Entry.Priority.MEDIUM) {
                                return 0;
                            }
                            if (o2.getPriority() == Entry.Priority.HIGH) {
                                return 1;
                            }
                            return -1;
                        }
                        if (o1.isActive() && !o2.isActive()) {
                            return -1;
                        }
                        if (!o1.isActive() && o2.isActive()) {
                            return 1;
                        }
                        if (o1.isActive() && o2.isActive()) {
                            if (o2.getPriority() == Entry.Priority.HIGH) {
                                return 1;
                            }
                            if (o2.getPriority() == Entry.Priority.LOW) {
                                return -1;
                            }
                            return 0;
                        }
                        break;
                    }
                    case LOW: {
                        if (!o1.isActive() && !o2.isActive()) {
                            if (o2.getPriority() == Entry.Priority.LOW) {
                                return 0;
                            }
                            return 1;
                        }
                        if (o1.isActive() && !o2.isActive()) {
                            return -1;
                        }
                        if (!o1.isActive() && o2.isActive()) {
                            return 1;
                        }
                        if (o1.isActive() && o2.isActive()) {
                            if (o2.getPriority() == Entry.Priority.LOW) {
                                return 0;
                            }
                            return 1;
                        }
                        break;
                    }
                    default: {
                        return 0;
                    }
                }
                return 0;
            }
        });
    }


    private List<Entry> createDummyData() {
        List<Entry> dummies = new ArrayList<>();

        // build in some randomness
        Random random = new Random();
        Entry.Priority[] priorities = Entry.Priority.values();
        String[] todoTasks = new String[]{"Android lernen", "DTA Workshop toll finden", "Konkurrenzanalyse", "IOS lernen"};

        for (int i = 0; i < 10; i++) {
            boolean entryActive = random.nextInt(2) == 0;
            String todoTask = todoTasks[random.nextInt(todoTasks.length)];
            Entry.Priority priority = priorities[random.nextInt(priorities.length)];

            Entry entry = new Entry(todoTask);
            entry.setCreationTime(System.currentTimeMillis() - (1000 * 60 * 60 * 24 * i)); // we subtract i days of the current time in milliseconds
            entry.setActive(entryActive);
            entry.setPriority(priority);

            dummies.add(entry);
        }
        return dummies;
    }

    @Override
    public void onEntryClick(View view, int entryIndex) {
        final Entry entry = this.entryData.get(entryIndex);

        // TODO: change active-state of entry in the backend server via changeEntryStatus in RequestManager

        entry.setActive(!entry.isActive());
        this.sortEntries();

        this.entryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDeleteClick(View view, final int entryIndex) {
        final Entry entry = this.entryData.get(entryIndex);

        // ATTENTION!! Deleting any of the dummy data will always result in an error, as this entry does not exist in the database
        new AlertDialog.Builder(MainActivity.this)
                .setMessage("Eintrag: " + entry.getName() + " löschen?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    @Override
    public void onEditClick(View view, int entryIndex) {
        Entry entryToUpdate = this.entryData.get(entryIndex);
        FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
        EntryEditDialog entryEditDialog = new EntryEditDialog(MainActivity.this, entryToUpdate, entryIndex);

        entryEditDialog.setCancelable(false);
        entryEditDialog.show(fragmentManager, "entryEdit");
    }

    @Override
    public void onEntryCreated(final String name, final Entry.Priority priority) {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {

                    URL getEndpoint = new URL("http://10.0.2.2:8080/rest/entry/createEntry");
                    HttpURLConnection connection = (HttpURLConnection) getEndpoint.openConnection();
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

                    JSONObject object = new JSONObject(sb.toString());
                    String error = object.getString("error");
                    if (!error.isEmpty()) {
                        MainActivity.this.runOnUiThread(() -> {
                            Toast.makeText(MainActivity.this, "Error " + error, Toast.LENGTH_SHORT).show();
                        });
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

                    MainActivity.this.entryData.add(entry);
                    MainActivity.this.sortEntries();
                    MainActivity.this.entryAdapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "Erfolgreich erstellt", Toast.LENGTH_SHORT).show();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onEntryEdited(Entry updatedEntry, int index) {
        // TODO: update edited entry in the backend server via updateEntry in RequestManager

        this.entryData.set(index, updatedEntry);
        this.sortEntries();

        this.entryAdapter.notifyDataSetChanged();
    }
}
