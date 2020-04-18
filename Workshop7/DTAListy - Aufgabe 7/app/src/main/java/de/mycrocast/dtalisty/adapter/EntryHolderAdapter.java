package de.mycrocast.dtalisty.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.mycrocast.dtalisty.R;
import de.mycrocast.dtalisty.data.Entry;
import de.mycrocast.dtalisty.data.EntryHolder;

public class EntryHolderAdapter extends RecyclerView.Adapter<EntryHolderAdapter.EntryHolderView> {

    private List<EntryHolder> data;
    private RecyclerClickListener clickListener;

    public EntryHolderAdapter(List<EntryHolder> data) {
        this.data = data;
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
        EntryHolder entryHolder = this.data.get(position);
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
        return this.data.size();
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
            return name;
        }

        public TextView getCount() {
            return count;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.edit: {
                    clickListener.onEditClick(v, getAdapterPosition());
                    return;
                }
                case R.id.delete: {
                    clickListener.onDeleteClick(v, getAdapterPosition());
                    return;
                }
            }
            clickListener.onEntryClick(v, getAdapterPosition());
        }
    }
}
