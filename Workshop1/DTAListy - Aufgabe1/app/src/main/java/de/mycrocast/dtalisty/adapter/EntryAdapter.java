package de.mycrocast.dtalisty.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.mycrocast.dtalisty.R;
import de.mycrocast.dtalisty.data.Entry;

public class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.EntryViewHolder> {

    private List<Entry> entries;

    public EntryAdapter(List<Entry> entries) {
        this.entries = entries;
    }

    @NonNull
    @Override
    public EntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View entryRowView = inflater.inflate(R.layout.item_entry, parent, false);
        EntryViewHolder viewHolder = new EntryViewHolder(entryRowView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EntryViewHolder holder, int position) {
        Entry currentEntry = entries.get(position);
        holder.getEntryContent().setText(currentEntry.getName());

        // convert long timestamp to actual string representation
        Date date = new Date(currentEntry.getCreationTime());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy");
        holder.getCreationTime().setText(simpleDateFormat.format(date));
    }

    @Override
    public int getItemCount() {
        return this.entries.size();
    }

    public class EntryViewHolder extends RecyclerView.ViewHolder {

        private TextView entryContent;
        private TextView creationTime;

        public EntryViewHolder(@NonNull View itemView) {
            super(itemView);

            this.creationTime = itemView.findViewById(R.id.creationTime);
            this.entryContent = itemView.findViewById(R.id.entryContent);
        }

        public TextView getEntryContent() {
            return this.entryContent;
        }

        public TextView getCreationTime() {
            return this.creationTime;
        }
    }
}
