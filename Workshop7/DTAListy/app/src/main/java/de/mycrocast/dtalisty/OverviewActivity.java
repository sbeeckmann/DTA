package de.mycrocast.dtalisty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import de.mycrocast.dtalisty.adapter.EntryHolderAdapter;
import de.mycrocast.dtalisty.adapter.RecyclerClickListener;

public class OverviewActivity extends AppCompatActivity implements RecyclerClickListener {

    private static final int NUM_COLUMNS = 2;

    private EntryHolderAdapter holderAdapter;
    private List<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        data = new ArrayList<>();
        data.add("sglkm");
        data.add("eeeee");
        data.add("eeeee2");
        data.add("eeeee3");
        data.add("eeeee4");
        data.add("eeeee5");
        data.add("eeeee5");
        data.add("eeeee5");
        data.add("eeeee5");
        data.add("eeeee5");
        data.add("eeeee5");
        data.add("eeeee5");
        data.add("eeeee5");
        data.add("eeeee5");

        boolean portrait = this.getResources().getBoolean(R.bool.port);
        int numColumns = 2;
        if (!portrait) {
            numColumns = 3;
        }
        RecyclerView recyclerView = this.findViewById(R.id.holder);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, numColumns);
        recyclerView.setLayoutManager(gridLayoutManager);

        this.holderAdapter = new EntryHolderAdapter(this.data);
        recyclerView.setAdapter(this.holderAdapter);
    }

    @Override
    public void onEntryClick(View view, int entryIndex) {

    }

    @Override
    public void onDeleteClick(View view, int entryIndex) {

    }

    @Override
    public void onEditClick(View view, int entryIndex) {

    }
}
