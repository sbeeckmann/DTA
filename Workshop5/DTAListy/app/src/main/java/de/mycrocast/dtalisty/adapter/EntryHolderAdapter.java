package de.mycrocast.dtalisty.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.mycrocast.dtalisty.R;
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
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public class EntryHolderView extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView name;

        public EntryHolderView(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
            itemView.setOnClickListener(this);
        }

        public TextView getName() {
            return name;
        }

        @Override
        public void onClick(View v) {
            clickListener.onEntryClick(v, getAdapterPosition());
        }
    }
}
