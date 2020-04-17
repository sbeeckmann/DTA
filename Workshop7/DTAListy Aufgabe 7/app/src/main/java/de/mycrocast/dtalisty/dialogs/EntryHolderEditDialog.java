package de.mycrocast.dtalisty.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import de.mycrocast.dtalisty.R;
import de.mycrocast.dtalisty.data.EntryHolder;

public class EntryHolderEditDialog extends AbstractDialog {

    public interface OnEntryHolderEdit {
        void onEntryHolderEdit(EntryHolder name, int index);
    }

    private OnEntryHolderEdit onEntryHolderEditCallback;
    private EntryHolder toUpdate;
    private int index;

    public EntryHolderEditDialog(OnEntryHolderEdit onEntryHolderEditCallback, EntryHolder toUpdate, int index) {
        this.onEntryHolderEditCallback = onEntryHolderEditCallback;
        this.toUpdate = toUpdate;
        this.index = index;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.entry_holder_creation_dialog, container, false);
        this.configure(view);

        TextView header = view.findViewById(R.id.header);
        header.setText("Liste editieren");
        this.nameView.setText(this.toUpdate.getName());


        this.saveButton.setEnabled(false);
        this.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEntryHolderEditCallback.onEntryHolderEdit(new EntryHolder(nameView.getText().toString()), index);
                dismiss();
            }
        });

        return view;
    }

    @Override
    protected int getNameViewResource() {
        return R.id.name;
    }

    @Override
    protected int getSaveButtonResource() {
        return R.id.save;
    }

    @Override
    protected int getCancelButtonResource() {
        return R.id.cancel;
    }
}
