package de.mycrocast.dtalisty.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import de.mycrocast.dtalisty.R;
import de.mycrocast.dtalisty.data.Entry;

public class EntryCreateDialog extends AbstractDialog {

    public interface OnEntryCreated {
        void onEntryCreated(String name, Entry.Priority priority);
    }

    private RadioGroup radioGroup;
    private OnEntryCreated onEntryCreatedCallback;

    public EntryCreateDialog(OnEntryCreated entryCreatedCallback) {
        this.onEntryCreatedCallback = entryCreatedCallback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.entry_creation_dialog, container, false);
        this.configure(view);

        TextView header = view.findViewById(R.id.header);
        header.setText("Eintrag erstellen");

        this.radioGroup = view.findViewById(R.id.priorityGroup);

        this.saveButton.setEnabled(false);
        this.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Entry.Priority priority = null;
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.lowPriority: {
                        priority = Entry.Priority.LOW;
                        break;
                    }
                    case R.id.mediumPriority: {
                        priority = Entry.Priority.MEDIUM;
                        break;
                    }
                    case R.id.highPriority: {
                        priority = Entry.Priority.HIGH;
                        break;
                    }
                    default: {
                        break;
                    }
                }
                onEntryCreatedCallback.onEntryCreated(nameView.getText().toString(), priority);
                dismiss();
            }
        });

        return view;
    }

    @Override
    protected int getNameViewResource() {
        return R.id.entryName;
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
