package de.mycrocast.dtalisty;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import de.mycrocast.dtalisty.adapter.EntryHolderAdapter;
import de.mycrocast.dtalisty.adapter.RecyclerClickListener;
import de.mycrocast.dtalisty.application.DTAListApp;
import de.mycrocast.dtalisty.data.EntryHolder;
import de.mycrocast.dtalisty.data.EntryHolderManager;
import de.mycrocast.dtalisty.dialogs.EntryHolderCreateDialog;
import de.mycrocast.dtalisty.dialogs.EntryHolderEditDialog;
import de.mycrocast.dtalisty.messaging.RequestManager;

public class OverviewActivity extends AppCompatActivity implements RecyclerClickListener, EntryHolderCreateDialog.OnEntryHolderCreated, EntryHolderEditDialog.OnEntryHolderEdit, EntryHolderManager.EntryHolderChangeListener {

    private EntryHolderAdapter holderAdapter;

    private RequestManager requestManager;
    private EntryHolderManager entryHolderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_overview);

        this.entryHolderManager = ((DTAListApp) this.getApplication()).getEntryHolderManager();
        this.requestManager = ((DTAListApp) this.getApplication()).getRequestManager();

        boolean portrait = this.getResources().getBoolean(R.bool.port);
        int numColumns = 2;
        if (!portrait) {
            numColumns = 3;
        }
        RecyclerView recyclerView = this.findViewById(R.id.holder);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, numColumns);
        recyclerView.setLayoutManager(gridLayoutManager);

        this.holderAdapter = new EntryHolderAdapter(this.entryHolderManager.getEntryHolders());
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

    @Override
    protected void onResume() {
        super.onResume();
        this.entryHolderManager.addChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        this.entryHolderManager.removeChangeListener(this);
    }

    private void requestEntries() {
        this.requestManager.requestEntryHolders(response -> {
            this.entryHolderManager.setEntryHolders(response);
            this.holderAdapter.setEntryHolderList(response);
            this.runOnUiThread(() -> {
                this.holderAdapter.notifyDataSetChanged();
            });
        }, error -> {
            OverviewActivity.this.runOnUiThread(() -> {
                Toast.makeText(OverviewActivity.this, error, Toast.LENGTH_SHORT).show();
            });
        });
    }

    @Override
    public void onEntryClick(View view, int entryIndex) {
        EntryHolder holderClicked = this.entryHolderManager.findById(entryIndex);
        Intent intent = new Intent(OverviewActivity.this, MainActivity.class);
        intent.putExtra("list", holderClicked.getId());
        this.startActivity(intent);
    }

    @Override
    public void onDeleteClick(View view, final int entryIndex) {
        final EntryHolder entryHolder = this.entryHolderManager.findById(entryIndex);
        new AlertDialog.Builder(OverviewActivity.this)
                .setMessage("Eintrag: " + entryHolder.getName() + " löschen?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        entryHolderManager.remove(entryHolder);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    @Override
    public void onEditClick(View view, int entryIndex) {
        EntryHolder toUpdate = this.entryHolderManager.findById(entryIndex);
        FragmentManager fragmentManager = OverviewActivity.this.getSupportFragmentManager();
        EntryHolderEditDialog entryHolderEditDialog = new EntryHolderEditDialog(OverviewActivity.this, toUpdate, entryIndex);
        entryHolderEditDialog.setCancelable(false);
        entryHolderEditDialog.show(fragmentManager, "entryHolderEdit");
    }

    @Override
    public void onEntryHolderCreated(String name) {
        this.entryHolderManager.add(new EntryHolder(name));

    }

    @Override
    public void onEntryHolderEdit(EntryHolder name, int index) {
        this.entryHolderManager.update(name);
    }

    @Override
    public void onEntryHolderAdded(EntryHolder entryHolder) {
        OverviewActivity.this.runOnUiThread(() -> {
            holderAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onEntryHolderUpdated(EntryHolder entryHolder) {
        OverviewActivity.this.runOnUiThread(() -> {
            holderAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onEntryHolderDeleted(EntryHolder entryHolder) {
        OverviewActivity.this.runOnUiThread(() -> {
            holderAdapter.notifyDataSetChanged();
        });
    }
}
