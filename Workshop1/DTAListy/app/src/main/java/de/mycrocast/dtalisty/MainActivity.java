package de.mycrocast.dtalisty;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.mycrocast.dtalisty.adapter.EntryAdapter;
import de.mycrocast.dtalisty.data.Entry;

public class MainActivity extends AppCompatActivity {

    private RecyclerView entryRecyclerView;
    private EntryAdapter entryAdapter;

    private List<Entry> entryData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.entryRecyclerView = this.findViewById(R.id.entries);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        this.entryRecyclerView.setLayoutManager(linearLayoutManager);

        this.entryData = this.createDummyData();
        this.entryAdapter = new EntryAdapter(this.entryData);
        this.entryRecyclerView.setAdapter(entryAdapter);
    }

    private List<Entry> createDummyData() {
        List<Entry> dummies = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Entry entry = new Entry("Dummy entry " + i);
            dummies.add(entry);
        }

        return dummies;
    }
}
