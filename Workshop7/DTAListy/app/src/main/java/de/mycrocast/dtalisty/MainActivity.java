package de.mycrocast.dtalisty;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
import de.mycrocast.dtalisty.messaging.RequestManager;
import de.mycrocast.dtalisty.messaging.response.BasicResponse;

public class MainActivity extends AppCompatActivity implements RecyclerClickListener, EntryCreateDialog.OnEntryCreated, EntryEditDialog.OnEntryEdited {

    private RecyclerView entryRecyclerView;
    private EntryAdapter entryAdapter;

    private RequestManager requestManager;
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

        this.requestManager = new RequestManager(this);
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
                        MainActivity.this.requestManager.deleteEntry(entry.getId(), MainActivity.this.entryHolderId, new Response.Listener<BasicResponse<Entry>>() {
                            @Override
                            public void onResponse(BasicResponse<Entry> response) {
                                if (response.getError() == null || response.getError().isEmpty()) {
                                    MainActivity.this.entryData.remove(entryIndex);
                                    MainActivity.this.entryAdapter.notifyItemRemoved(entryIndex);
                                    Toast.makeText(MainActivity.this, "Erfolgreich gelöscht", Toast.LENGTH_SHORT).show();
                                } else {
                                    //TODO something more to show there was an error
                                    Toast.makeText(MainActivity.this, response.getError(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO inform the user about the error
                                System.out.println(error.getMessage());
                            }
                        });
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
        this.requestManager.createEntry(this.entryHolderId, name, priority, new Response.Listener<BasicResponse<Entry>>() {
            @Override
            public void onResponse(BasicResponse<Entry> response) {
                if (response.getError() == null || response.getError().isEmpty()) {
                    MainActivity.this.entryData.add(new Entry(name, priority));
                    MainActivity.this.sortEntries();
                    MainActivity.this.entryAdapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "Erfolgreich erstellt", Toast.LENGTH_SHORT).show();
                } else {
                    //TODO something more to show there was an error
                    Toast.makeText(MainActivity.this, response.getError(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO inform the user about the error
                System.out.println(error.getMessage());
            }
        });
    }

    @Override
    public void onEntryEdited(Entry updatedEntry, int index) {
        this.entryData.set(index, updatedEntry);
        this.sortEntries();

        this.entryAdapter.notifyDataSetChanged();
    }
}
