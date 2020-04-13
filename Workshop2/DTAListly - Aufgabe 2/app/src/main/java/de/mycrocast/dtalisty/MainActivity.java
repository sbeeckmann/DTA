package de.mycrocast.dtalisty;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

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
                    case NORMAL: {
                        if (!o1.isActive() && !o2.isActive()) {
                            if (o2.getPriority() == Entry.Priority.NORMAL) {
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
}
