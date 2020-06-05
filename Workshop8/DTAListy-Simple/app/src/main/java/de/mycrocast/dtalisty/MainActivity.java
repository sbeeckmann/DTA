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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import de.mycrocast.dtalisty.adapter.EntryAdapter;
import de.mycrocast.dtalisty.adapter.RecyclerClickListener;
import de.mycrocast.dtalisty.application.DTAListApp;
import de.mycrocast.dtalisty.data.Entry;
import de.mycrocast.dtalisty.data.EntryHolder;
import de.mycrocast.dtalisty.data.EntryHolderManager;
import de.mycrocast.dtalisty.dialogs.EntryCreateDialog;
import de.mycrocast.dtalisty.dialogs.EntryEditDialog;
import de.mycrocast.dtalisty.messaging.RequestManager;

public class MainActivity extends AppCompatActivity implements RecyclerClickListener, EntryCreateDialog.OnEntryCreated, EntryEditDialog.OnEntryEdited {

    private RecyclerView entryRecyclerView;
    private EntryAdapter entryAdapter;

    private RequestManager requestManager;
    private EntryHolderManager entryHolderManager;

    private long entryHolderId;
    private EntryHolder holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        this.entryHolderManager = ((DTAListApp) this.getApplication()).getEntryHolderManager();
        this.requestManager = ((DTAListApp) this.getApplication()).getRequestManager();

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

        this.entryHolderId = this.getIntent().getExtras().getLong("list");

        this.holder = this.entryHolderManager.findById(this.entryHolderId);
        this.entryAdapter = new EntryAdapter(this.holder.getEntries());
        this.entryAdapter.setClickListener(this);

        this.entryRecyclerView.setAdapter(this.entryAdapter);

        this.sortEntries();
    }

    private void sortEntries() {
        Collections.sort(this.holder.getEntries(), new Comparator<Entry>() {
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
        final Entry entry = this.holder.getEntries().get(entryIndex);

        entry.setActive(!entry.isActive());
        this.sortEntries();

        this.entryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDeleteClick(View view, final int entryIndex) {
        final Entry entry = this.holder.getEntries().get(entryIndex);

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
        Entry entryToUpdate = this.holder.getEntries().get(entryIndex);
        FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
        EntryEditDialog entryEditDialog = new EntryEditDialog(MainActivity.this, entryToUpdate, entryIndex);

        entryEditDialog.setCancelable(false);
        entryEditDialog.show(fragmentManager, "entryEdit");
    }

    @Override
    public void onEntryCreated(final String name, final Entry.Priority priority) {
        this.requestManager.createEntry(name, priority, this.entryHolderId, response -> {
            this.runOnUiThread(() -> {
                EntryHolder holder = this.entryHolderManager.findById(this.entryHolderId);
                holder.getEntries().add(response);
                this.entryAdapter.notifyDataSetChanged();

                this.sortEntries();
            });
        }, error -> {
            MainActivity.this.runOnUiThread(() -> {
                Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
            });
        });
    }

    @Override
    public void onEntryEdited(Entry updatedEntry, int index) {
        this.holder.getEntries().set(index, updatedEntry);
        this.sortEntries();

        this.entryAdapter.notifyDataSetChanged();
    }
}
