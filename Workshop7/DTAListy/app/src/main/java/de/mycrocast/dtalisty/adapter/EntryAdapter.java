package de.mycrocast.dtalisty.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.mycrocast.dtalisty.R;
import de.mycrocast.dtalisty.data.Entry;

public class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.EntryViewHolder> {

    public interface ClickListener {
        void onEntryClick(View view, int entryIndex);

        void onDeleteClick(View view, int entryIndex);

        void onEditClick(View view, int entryIndex);
    }

    private List<Entry> entries;
    private Context context;
    private ClickListener clickListener;

    public EntryAdapter(List<Entry> entries) {
        this.entries = entries;
    }

    @NonNull
    @Override
    public EntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(this.context);

        View entryRowView = inflater.inflate(R.layout.item_entry, parent, false);

        return new EntryViewHolder(entryRowView);
    }

    @Override
    public void onBindViewHolder(@NonNull EntryViewHolder holder, int position) {
        Entry currentEntry = entries.get(position);
        holder.getEntryContent().setText(currentEntry.getName());

        // convert long timestamp to actual string representation
        Date date = new Date(currentEntry.getCreationTime());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy");
        holder.getCreationTime().setText(simpleDateFormat.format(date));

        if (currentEntry.isActive()) {
            holder.getBackground().setBackgroundColor(this.context.getResources().getColor(R.color.active));
        } else {
            holder.getBackground().setBackgroundColor(this.context.getResources().getColor(R.color.inactive));
        }

        switch (currentEntry.getPriority()) {
            case HIGH: {
                holder.getPriorityView().setBackgroundColor(this.context.getResources().getColor(R.color.priorityHigh));
                break;
            }
            case MEDIUM: {
                holder.getPriorityView().setBackgroundColor(this.context.getResources().getColor(R.color.priorityMedium));
                break;
            }
            case LOW: {
                holder.getPriorityView().setBackgroundColor(this.context.getResources().getColor(R.color.priorityLow));
                break;
            }
            default: {
                // could throw an error or a log entry to show the next developer that we have priority that is not expected
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return this.entries.size();
    }

    public void setClickListener(ClickListener entryClickListener) {
        this.clickListener = entryClickListener;
    }

    public class EntryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView entryContent;
        private TextView creationTime;
        private View priorityView;
        private RelativeLayout background;
        private ImageButton deleteButton;
        private ImageButton editButton;

        public EntryViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            this.creationTime = itemView.findViewById(R.id.creationTime);
            this.entryContent = itemView.findViewById(R.id.entryContent);
            this.priorityView = itemView.findViewById(R.id.priority);
            this.background = itemView.findViewById(R.id.entryWrapper);
            this.deleteButton = itemView.findViewById(R.id.delete);
            this.deleteButton.setOnClickListener(this);

            this.editButton = itemView.findViewById(R.id.edit);
            this.editButton.setOnClickListener(this);
        }

        public TextView getEntryContent() {
            return this.entryContent;
        }

        public TextView getCreationTime() {
            return this.creationTime;
        }

        public View getPriorityView() {
            return priorityView;
        }

        public RelativeLayout getBackground() {
            return background;
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                if (v == this.deleteButton) {
                    clickListener.onDeleteClick(v, this.getAdapterPosition());
                    return;
                }
                if (v == this.editButton) {
                    clickListener.onEditClick(v, this.getAdapterPosition());
                    return;
                }
                clickListener.onEntryClick(v, this.getAdapterPosition());
            }
        }
    }
}
