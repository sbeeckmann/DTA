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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import de.mycrocast.dtalisty.adapter.EntryHolderAdapter;
import de.mycrocast.dtalisty.adapter.RecyclerClickListener;
import de.mycrocast.dtalisty.data.EntryHolder;
import de.mycrocast.dtalisty.dialogs.EntryHolderCreateDialog;
import de.mycrocast.dtalisty.dialogs.EntryHolderEditDialog;
import de.mycrocast.dtalisty.messaging.RequestManager;
import de.mycrocast.dtalisty.messaging.response.BasicResponse;

public class OverviewActivity extends AppCompatActivity implements RecyclerClickListener, EntryHolderCreateDialog.OnEntryHolderCreated, EntryHolderEditDialog.OnEntryHolderEdit {

    private EntryHolderAdapter holderAdapter;
    private List<EntryHolder> data;
    private RequestManager requestManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_overview);

        this.data = new ArrayList<>();
        this.requestManager = new RequestManager(this);
        this.requestManager.getEntries(new Response.Listener<BasicResponse<List<EntryHolder>>>() {
            @Override
            public void onResponse(BasicResponse<List<EntryHolder>> response) {
                if (response.getError() == null || response.getError().isEmpty()) {
                    OverviewActivity.this.data.clear();
                    OverviewActivity.this.data.addAll(response.getResponseData());
                    OverviewActivity.this.holderAdapter.notifyDataSetChanged();
                } else {
                    //TODO something more to show there was an error
                    Toast.makeText(OverviewActivity.this, response.getError(), Toast.LENGTH_SHORT).show();
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
