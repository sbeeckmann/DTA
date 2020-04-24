package de.mycrocast.dtalisty.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import de.mycrocast.dtalisty.Constants;
import de.mycrocast.dtalisty.R;
import de.mycrocast.dtalisty.adapter.EntryAdapter;
import de.mycrocast.dtalisty.adapter.RecyclerClickListener;
import de.mycrocast.dtalisty.data.Entry;
import de.mycrocast.dtalisty.data.EntryHolder;
import de.mycrocast.dtalisty.dialogs.EntryCreateDialog;
import de.mycrocast.dtalisty.dialogs.EntryEditDialog;
import de.mycrocast.dtalisty.messaging.restcaller.EntryRestCaller;
import de.mycrocast.dtalisty.messaging.response.BasicResponse;

public class EntryListActivity extends AbstractActivity implements RecyclerClickListener, EntryCreateDialog.OnEntryCreated, EntryEditDialog.OnEntryEdited, EntryHolder.EntryChangeListener {

    public static Intent createStartIntent(Context context, EntryHolder entryHolder) {
        Intent intent = new Intent(context, EntryListActivity.class);
        intent.putExtra(Constants.ENTRY_HOLDER_ID, entryHolder.getId());
        return intent;
    }

    private RecyclerView entryRecyclerView;
    private EntryAdapter entryAdapter;
    private EntryRestCaller entryRestCaller;
    private EntryHolder entryHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        this.entryRestCaller = this.getEntryRestCaller();

        long entryHolderId = this.getIntent().getExtras().getLong(Constants.ENTRY_HOLDER_ID);
        this.entryHolder = this.getEntryHolderManager().findById(entryHolderId);

        this.entryRecyclerView = this.findViewById(R.id.entries);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        this.entryRecyclerView.setLayoutManager(linearLayoutManager);

        this.entryAdapter = new EntryAdapter(this.entryHolder.getEntries());
        this.entryAdapter.setClickListener(this);
        this.entryRecyclerView.setAdapter(this.entryAdapter);

        FloatingActionButton fab = this.findViewById(R.id.addButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = EntryListActivity.this.getSupportFragmentManager();
                EntryCreateDialog createDialog = new EntryCreateDialog(EntryListActivity.this);

                createDialog.setCancelable(false);
                createDialog.show(fragmentManager, "entryDialog");
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        this.entryHolder.removeChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.entryHolder.addChangeListener(this);
    }

    @Override
    public void onEntryClick(View view, final int entryIndex) {
        Entry entry = this.entryAdapter.getByPosition(entryIndex);

        this.entryRestCaller.changeEntryStatus(entry.getId(), !entry.isActive(), new Response.Listener<BasicResponse<Entry>>() {
            @Override
            public void onResponse(BasicResponse<Entry> response) {
                if (response.getError() == null || response.getError().isEmpty()) {
                    EntryListActivity.this.entryHolder.update(response.getResponseData());

                    Toast.makeText(EntryListActivity.this, "Erfolgreich Entry-Status geändert", Toast.LENGTH_SHORT).show();
                } else {
                    //TODO something more to show there was an error
                    Toast.makeText(EntryListActivity.this, response.getError(), Toast.LENGTH_SHORT).show();
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
    public void onDeleteClick(View view, final int entryIndex) {
        final Entry entry = this.entryAdapter.getByPosition(entryIndex);

        new AlertDialog.Builder(EntryListActivity.this)
                .setMessage("Eintrag: " + entry.getName() + " löschen?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EntryListActivity.this.entryRestCaller.deleteEntry(entry.getId(), EntryListActivity.this.entryHolder.getId(), new Response.Listener<BasicResponse<Entry>>() {
                            @Override
                            public void onResponse(BasicResponse<Entry> response) {
                                if (response.getError() == null || response.getError().isEmpty()) {
                                    EntryListActivity.this.entryHolder.remove(entry);

                                    Toast.makeText(EntryListActivity.this, "Erfolgreich gelöscht", Toast.LENGTH_SHORT).show();
                                } else {
                                    //TODO something more to show there was an error
                                    Toast.makeText(EntryListActivity.this, response.getError(), Toast.LENGTH_SHORT).show();
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
        Entry entryToUpdate = this.entryAdapter.getByPosition(entryIndex);

        FragmentManager fragmentManager = EntryListActivity.this.getSupportFragmentManager();
        EntryEditDialog entryEditDialog = new EntryEditDialog(EntryListActivity.this, entryToUpdate, entryIndex);
        entryEditDialog.setCancelable(false);
        entryEditDialog.show(fragmentManager, "entryEdit");
    }

    @Override
    public void onEntryCreated(final String name, final Entry.Priority priority) {
        this.entryRestCaller.createEntry(this.entryHolder.getId(), name, priority, new Response.Listener<BasicResponse<Entry>>() {
            @Override
            public void onResponse(BasicResponse<Entry> response) {
                if (response.getError() == null || response.getError().isEmpty()) {
                    EntryListActivity.this.entryHolder.add(response.getResponseData());

                    Toast.makeText(EntryListActivity.this, "Erfolgreich erstellt", Toast.LENGTH_SHORT).show();
                } else {
                    //TODO something more to show there was an error
                    Toast.makeText(EntryListActivity.this, response.getError(), Toast.LENGTH_SHORT).show();
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
    public void onEntryEdited(Entry updatedEntry, final int index) {
        this.entryRestCaller.updateEntry(updatedEntry.getId(), updatedEntry.getName(), updatedEntry.getPriority(), new Response.Listener<BasicResponse<Entry>>() {
            @Override
            public void onResponse(BasicResponse<Entry> response) {
                if (response.getError() == null || response.getError().isEmpty()) {
                    EntryListActivity.this.entryHolder.update(response.getResponseData());

                    Toast.makeText(EntryListActivity.this, "Erfolgreich editiert", Toast.LENGTH_SHORT).show();
                } else {
                    //TODO something more to show there was an error
                    Toast.makeText(EntryListActivity.this, response.getError(), Toast.LENGTH_SHORT).show();
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
    public void onEntryAdded(Entry entry) {
        this.entryAdapter.sortEntries();
        this.entryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onEntryUpdated(Entry entry) {
        this.entryAdapter.sortEntries();
        this.entryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onEntryDeleted(Entry entry) {
        this.entryAdapter.sortEntries();
        this.entryAdapter.notifyDataSetChanged();
    }
}
