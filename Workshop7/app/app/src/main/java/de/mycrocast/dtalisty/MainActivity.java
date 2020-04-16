package de.mycrocast.dtalisty;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

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

import de.mycrocast.dtalisty.adapter.EntryAdapter;
import de.mycrocast.dtalisty.data.Entry;
import de.mycrocast.dtalisty.data.EntryHolder;
import de.mycrocast.dtalisty.dialogs.EntryCreateDialog;
import de.mycrocast.dtalisty.dialogs.EntryEditDialog;
import de.mycrocast.dtalisty.messaging.RequestManager;
import de.mycrocast.dtalisty.messaging.response.BasicResponse;

public class MainActivity extends AppCompatActivity implements EntryAdapter.ClickListener, EntryCreateDialog.OnEntryCreated, EntryEditDialog.OnEntryEdited {

    private RecyclerView entryRecyclerView;
    private EntryAdapter entryAdapter;

    private RequestManager requestManager;
    private EntryHolder entryHolder;
    private List<Entry> entries = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        this.requestManager = new RequestManager(this);
        this.requestManager.getEntries(new Response.Listener<BasicResponse<List<EntryHolder>>>() {
            @Override
            public void onResponse(BasicResponse<List<EntryHolder>> response) {
                // did we get an error message from the backend server?
                if (response.getError() != null) {
                    // todo: show meaningful message to the end user for feedback
                    System.out.println(response.getError());
                    return;
                }

                MainActivity.this.entryHolder = response.getResponseData().get(0);

                MainActivity.this.entries = MainActivity.this.entryHolder.getEntries();
                MainActivity.this.sortEntries();

                MainActivity.this.entryAdapter.setEntries(MainActivity.this.entries);
                MainActivity.this.entryAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // todo: show meaningful message to the end user for feedback
                System.out.println(error.getMessage());
            }
        });

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

        this.entryAdapter = new EntryAdapter();
        this.entryAdapter.setClickListener(this);

        this.entryRecyclerView.setAdapter(this.entryAdapter);

        this.sortEntries();
    }

    private void sortEntries() {
        Collections.sort(this.entries, new Comparator<Entry>() {
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

    @Override
    public void onEntryClick(View view, int entryIndex) {
        final Entry entry = this.entries.get(entryIndex);

        entry.setActive(!entry.isActive());
        this.sortEntries();

        this.entryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDeleteClick(View view, final int entryIndex) {
        final Entry entry = this.entries.get(entryIndex);

        new AlertDialog.Builder(MainActivity.this)
                .setMessage("Eintrag: " + entry.getName() + " löschen?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.this.requestManager.deleteEntry(entry.getId(), new Response.Listener<BasicResponse<Entry>>() {
                                    @Override
                                    public void onResponse(BasicResponse<Entry> response) {
                                        // did we get an error message from the backend server?
                                        if (response.getError() != null) {
                                            // todo: show meaningful message to the end user for feedback
                                            System.out.println(response.getError());
                                            return;
                                        }

                                        MainActivity.this.entryHolder.getEntries().remove(entry);
                                        MainActivity.this.entries = MainActivity.this.entryHolder.getEntries();
                                        MainActivity.this.sortEntries();

                                        MainActivity.this.entryAdapter.setEntries(MainActivity.this.entries);
                                        MainActivity.this.entryAdapter.notifyDataSetChanged();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // todo: show meaningful message to the end user for feedback
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
        Entry entryToUpdate = this.entries.get(entryIndex);
        FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
        EntryEditDialog entryEditDialog = new EntryEditDialog(MainActivity.this, entryToUpdate, entryIndex);

        entryEditDialog.setCancelable(false);
        entryEditDialog.show(fragmentManager, "entryEdit");
    }

    @Override
    public void onEntryCreated(String name, Entry.Priority priority) {
        this.requestManager.createEntry(this.entryHolder.getId(), name, priority, new Response.Listener<BasicResponse<Entry>>() {
            @Override
            public void onResponse(BasicResponse<Entry> response) {
                // did we get an error message from the backend server?
                if (response.getError() != null) {
                    // todo: show meaningful message to the end user for feedback
                    System.out.println(response.getError());
                    return;
                }

                Entry addedEntry = response.getResponseData();
                MainActivity.this.entryHolder.getEntries().add(addedEntry);

                MainActivity.this.entries = MainActivity.this.entryHolder.getEntries();
                MainActivity.this.sortEntries();

                MainActivity.this.entryAdapter.setEntries(MainActivity.this.entries);
                MainActivity.this.entryAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // todo: show meaningful message to the end user for feedback
                System.out.println(error.getMessage());
            }
        });
    }

    @Override
    public void onEntryEdited(Entry updatedEntry, int index) {
        this.entries.set(index, updatedEntry);
        this.sortEntries();

        this.entryAdapter.notifyDataSetChanged();
    }
}
