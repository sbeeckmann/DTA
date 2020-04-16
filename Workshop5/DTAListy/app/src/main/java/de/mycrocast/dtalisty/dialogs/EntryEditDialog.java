package de.mycrocast.dtalisty.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import de.mycrocast.dtalisty.R;
import de.mycrocast.dtalisty.data.Entry;

public class EntryEditDialog extends AbstractDialog {

    public interface OnEntryEdited {
        void onEntryEdited(Entry updatedEntry, int index);
    }

    private RadioGroup radioGroup;
    private OnEntryEdited entryEditCallback;
    private Entry entryToUpdate;
    private int index;

    public EntryEditDialog(OnEntryEdited entryEditCallback, Entry entryToUpdate, int entryIndex) {
        this.entryEditCallback = entryEditCallback;
        this.entryToUpdate = entryToUpdate;
        this.index = entryIndex;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.entry_creation_dialog, container, false);
        this.configure(view);
        TextView header = view.findViewById(R.id.header);
        header.setText("Eintrag editieren");

        this.nameView = view.findViewById(R.id.entryName);
        this.nameView.setText(this.entryToUpdate.getName());

        this.radioGroup = view.findViewById(R.id.priorityGroup);
        switch (this.entryToUpdate.getPriority()) {
            case HIGH: {
                this.radioGroup.check(R.id.highPriority);
                break;
            }
            case MEDIUM: {
                this.radioGroup.check(R.id.mediumPriority);
                break;
            }
            case LOW: {
                this.radioGroup.check(R.id.lowPriority);
                break;
            }
            default: {
                // could throw an error to show the next developer that we got a new status
            }
        }

        this.saveButton.setEnabled(true);
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
                Entry updated = new Entry(nameView.getText().toString(), priority);
                entryEditCallback.onEntryEdited(updated, index);
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
