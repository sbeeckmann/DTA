package de.mycrocast.dtalisty.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import de.mycrocast.dtalisty.R;

public class EntryHolderCreateDialog extends AbstractDialog {

    public interface OnEntryHolderCreated {
        void onEntryHolderCreated(String name);
    }

    private OnEntryHolderCreated onEntryHolderCreatedCallback;

    public EntryHolderCreateDialog(OnEntryHolderCreated entryCreatedCallback) {
        this.onEntryHolderCreatedCallback = entryCreatedCallback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.entry_holder_creation_dialog, container, false);
        this.configure(view);

        TextView header = view.findViewById(R.id.header);
        header.setText("Liste anlegen");

        this.saveButton.setEnabled(false);
        this.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EntryHolderCreateDialog.this.onEntryHolderCreatedCallback.onEntryHolderCreated(EntryHolderCreateDialog.this.nameView.getText().toString());
                EntryHolderCreateDialog.this.dismiss();
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
