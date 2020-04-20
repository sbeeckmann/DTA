package de.mycrocast.dtalisty.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import de.mycrocast.dtalisty.R;
import de.mycrocast.dtalisty.adapter.EntryHolderAdapter;
import de.mycrocast.dtalisty.adapter.RecyclerClickListener;
import de.mycrocast.dtalisty.data.EntryHolder;
import de.mycrocast.dtalisty.data.EntryHolderManager;
import de.mycrocast.dtalisty.dialogs.EntryHolderCreateDialog;
import de.mycrocast.dtalisty.dialogs.EntryHolderEditDialog;
import de.mycrocast.dtalisty.messaging.RequestManager;
import de.mycrocast.dtalisty.messaging.response.BasicResponse;

public class MainActivity extends AbstractActivity implements RecyclerClickListener, EntryHolderCreateDialog.OnEntryHolderCreated, EntryHolderEditDialog.OnEntryHolderEdit, EntryHolderManager.EntryHolderChangeListener {

    private RequestManager requestManager;
    private EntryHolderManager entryHolderManager;
    private EntryHolderAdapter holderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_overview);

        this.requestManager = this.getRequestManager();
        this.entryHolderManager = this.getEntryHolderManager();

        this.requestManager.getEntries(new Response.Listener<BasicResponse<List<EntryHolder>>>() {
            @Override
            public void onResponse(BasicResponse<List<EntryHolder>> response) {
                if (response.getError() == null || response.getError().isEmpty()) {
                    List<EntryHolder> entryHolderList = response.getResponseData();
                    MainActivity.this.entryHolderManager.setEntryHolders(entryHolderList);

                    MainActivity.this.holderAdapter.setEntryHolderList(entryHolderList);
                    MainActivity.this.holderAdapter.notifyDataSetChanged();
                } else {
                    //TODO something more to show there was an error
                    Toast.makeText(MainActivity.this, response.getError(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // todo: show meaningful message to the end user for feedback
                System.out.println(error.getMessage());
            }
        });

        boolean portrait = this.getResources().getBoolean(R.bool.port);
        int numColumns = 2;
        if (!portrait) {
            numColumns = 3;
        }
        RecyclerView recyclerView = this.findViewById(R.id.holder);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, numColumns);
        recyclerView.setLayoutManager(gridLayoutManager);

        this.holderAdapter = new EntryHolderAdapter();
        this.holderAdapter.setClickListener(this);
        recyclerView.setAdapter(this.holderAdapter);

        FloatingActionButton fab = this.findViewById(R.id.addButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
                EntryHolderCreateDialog createDialog = new EntryHolderCreateDialog(MainActivity.this);

                createDialog.setCancelable(false);
                createDialog.show(fragmentManager, "entryDialog");
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        this.entryHolderManager.removeChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.entryHolderManager.addChangeListener(this);
    }

    @Override
    public void onEntryClick(View view, int entryIndex) {
        EntryHolder holderClicked = this.holderAdapter.getByPosition(entryIndex);
        Intent intent = EntryListActivity.createStartIntent(this, holderClicked);
        this.startActivity(intent);
    }

    @Override
    public void onDeleteClick(View view, final int entryIndex) {
        final EntryHolder entryHolder = this.holderAdapter.getByPosition(entryIndex);

        new AlertDialog.Builder(MainActivity.this)
                .setMessage("Eintrag: " + entryHolder.getName() + " löschen?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.this.requestManager.deleteEntryHolder(entryHolder.getId(), new Response.Listener<BasicResponse<EntryHolder>>() {
                            @Override
                            public void onResponse(BasicResponse<EntryHolder> response) {
                                if (response.getError() == null || response.getError().isEmpty()) {
                                    MainActivity.this.entryHolderManager.remove(entryHolder);
                                    Toast.makeText(MainActivity.this, "EntryHolder erfolgreich entfernt", Toast.LENGTH_SHORT).show();
                                } else {
                                    //TODO: something more to show there was an error
                                    Toast.makeText(MainActivity.this, response.getError(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO: show meaningful message to the end user for feedback
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
        EntryHolder toUpdate = this.holderAdapter.getByPosition(entryIndex);

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        EntryHolderEditDialog entryHolderEditDialog = new EntryHolderEditDialog(this, toUpdate, entryIndex);
        entryHolderEditDialog.setCancelable(false);
        entryHolderEditDialog.show(fragmentManager, "entryHolderEdit");
    }

    @Override
    public void onEntryHolderCreated(String name) {
        this.requestManager.createEntryHolder(name, new Response.Listener<BasicResponse<EntryHolder>>() {
            @Override
            public void onResponse(BasicResponse<EntryHolder> response) {
                if (response.getError() == null || response.getError().isEmpty()) {
                    MainActivity.this.entryHolderManager.add(response.getResponseData());
                    Toast.makeText(MainActivity.this, "EntryHolder erfolgreich erstellt", Toast.LENGTH_SHORT).show();
                } else {
                    //TODO: something more to show there was an error
                    Toast.makeText(MainActivity.this, response.getError(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: show meaningful message to the end user for feedback
                System.out.println(error.getMessage());
            }
        });
    }

    @Override
    public void onEntryHolderEdit(EntryHolder entryHolder, final int index) {
        this.requestManager.editEntryHolder(entryHolder.getId(), entryHolder.getName(), new Response.Listener<BasicResponse<EntryHolder>>() {
            @Override
            public void onResponse(BasicResponse<EntryHolder> response) {
                if (response.getError() == null || response.getError().isEmpty()) {
                    MainActivity.this.entryHolderManager.update(response.getResponseData());
                    Toast.makeText(MainActivity.this, "EntryHolder erfolgreich editiert", Toast.LENGTH_SHORT).show();
                } else {
                    //TODO: something more to show there was an error
                    Toast.makeText(MainActivity.this, response.getError(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: show meaningful message to the end user for feedback
                System.out.println(error.getMessage());
            }
        });
    }

    @Override
    public void onEntryHolderAdded(EntryHolder entryHolder) {
        this.holderAdapter.add(entryHolder);
        this.holderAdapter.notifyDataSetChanged();
    }

    @Override
    public void onEntryHolderUpdated(EntryHolder entryHolder) {
        this.holderAdapter.update(entryHolder);
        this.holderAdapter.notifyDataSetChanged();
    }

    @Override
    public void onEntryHolderDeleted(EntryHolder entryHolder) {
        this.holderAdapter.remove(entryHolder);
        this.holderAdapter.notifyDataSetChanged();
    }
}
