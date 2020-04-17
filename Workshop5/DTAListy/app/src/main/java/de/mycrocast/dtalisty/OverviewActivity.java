package de.mycrocast.dtalisty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import de.mycrocast.dtalisty.adapter.EntryHolderAdapter;
import de.mycrocast.dtalisty.adapter.RecyclerClickListener;
import de.mycrocast.dtalisty.data.EntryHolder;
import de.mycrocast.dtalisty.dialogs.EntryHolderCreateDialog;

public class OverviewActivity extends AppCompatActivity implements RecyclerClickListener, EntryHolderCreateDialog.OnEntryHolderCreated {

    private static final int NUM_COLUMNS = 2;

    private EntryHolderAdapter holderAdapter;
    private List<EntryHolder> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        data = new ArrayList<>();
        data.add(new EntryHolder("sglkm"));
        data.add(new EntryHolder("sglkm"));
        data.add(new EntryHolder("sglkm"));
        data.add(new EntryHolder("sglkm"));
        data.add(new EntryHolder("sglkm"));
        data.add(new EntryHolder("sglkm"));
        data.add(new EntryHolder("sglkm"));
        data.add(new EntryHolder("sglkm"));
        data.add(new EntryHolder("sglkm"));
        data.add(new EntryHolder("sglkm"));
        data.add(new EntryHolder("sglkm"));
        data.add(new EntryHolder("sglkm"));


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
        Intent intent = new Intent(OverviewActivity.this, MainActivity.class);
        this.startActivity(intent);
    }

    @Override
    public void onDeleteClick(View view, int entryIndex) {

    }

    @Override
    public void onEditClick(View view, int entryIndex) {

    }

    @Override
    public void onEntryHolderCreated(String name) {
        EntryHolder holder = new EntryHolder(name);

        data.add(holder);
        holderAdapter.notifyDataSetChanged();
    }
}
