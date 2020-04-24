package de.mycrocast.dtalisty.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.mycrocast.dtalisty.R;
import de.mycrocast.dtalisty.data.Entry;
import de.mycrocast.dtalisty.data.EntryHolder;

public class EntryHolderAdapter extends RecyclerView.Adapter<EntryHolderAdapter.EntryHolderView> {

    private List<EntryHolder> entryHolderList;
    private RecyclerClickListener clickListener;

    public EntryHolderAdapter() {
        this.entryHolderList = new ArrayList<>();
    }

    public void setClickListener(RecyclerClickListener entryClickListener) {
        this.clickListener = entryClickListener;
    }

    @NonNull
    @Override
    public EntryHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View entryRowView = inflater.inflate(R.layout.item_entry_holder, parent, false);
        return new EntryHolderView(entryRowView);
    }

    @Override
    public void onBindViewHolder(@NonNull EntryHolderView holder, int position) {
        EntryHolder entryHolder = this.entryHolderList.get(position);
        holder.getName().setText(entryHolder.getName());

        int activeCount = 0;
        if (entryHolder.getEntries() != null) {
            for (Entry entry : entryHolder.getEntries()) {
                if (entry.isActive()) {
                    activeCount++;
                }
            }
        }

        holder.getCount().setText(String.valueOf(activeCount));
    }

    @Override
    public int getItemCount() {
        return this.entryHolderList.size();
    }

    public EntryHolder getByPosition(int position) {
        if (position < 0 || position >= this.entryHolderList.size()) {
            return null;
        }

        return this.entryHolderList.get(position);
    }

    public void setEntryHolderList(List<EntryHolder> entryHolders) {
        this.entryHolderList.clear();

        for (EntryHolder entryHolder : entryHolders) {
            if (entryHolder != null) {
                this.entryHolderList.add(entryHolder);
            }
        }
    }

    public void add(EntryHolder entryHolder) {
        this.entryHolderList.add(entryHolder);
    }

    public void update(EntryHolder entryHolder) {
        int position = this.entryHolderList.indexOf(entryHolder);
        this.entryHolderList.set(position, entryHolder);
    }

    public void remove(EntryHolder entryHolder) {
        this.entryHolderList.remove(entryHolder);
    }

    public class EntryHolderView extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView name;
        private TextView count;

        public EntryHolderView(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
            this.count = itemView.findViewById(R.id.count);

            ImageButton edit = itemView.findViewById(R.id.edit);
            edit.setOnClickListener(this);

            ImageButton delete = itemView.findViewById(R.id.delete);
            delete.setOnClickListener(this);

            itemView.setOnClickListener(this);
        }

        public TextView getName() {
            return this.name;
        }

        public TextView getCount() {
            return this.count;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.edit: {
                    EntryHolderAdapter.this.clickListener.onEditClick(v, this.getAdapterPosition());
                    return;
                }
                case R.id.delete: {
                    EntryHolderAdapter.this.clickListener.onDeleteClick(v, this.getAdapterPosition());
                    return;
                }
            }
            EntryHolderAdapter.this.clickListener.onEntryClick(v, this.getAdapterPosition());
        }
    }
}
