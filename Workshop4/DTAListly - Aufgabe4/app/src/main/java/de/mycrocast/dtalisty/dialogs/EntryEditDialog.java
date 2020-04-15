package de.mycrocast.dtalisty.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import de.mycrocast.dtalisty.R;
import de.mycrocast.dtalisty.data.Entry;

public class EntryEditDialog extends DialogFragment {
    public interface OnEntryEdited {
        void onEntryEdited(Entry updatedEntry, int index);
    }

    private EditText entryName;
    private RadioGroup radioGroup;
    private Button saveButton;
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

        this.entryName = view.findViewById(R.id.entryName);
        this.entryName.setText(this.entryToUpdate.getName());

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

        this.saveButton = view.findViewById(R.id.save);
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
                Entry updated = new Entry(entryName.getText().toString(), priority);
                entryEditCallback.onEntryEdited(updated, index);
                dismiss();
            }
        });

        Button cancelButton = view.findViewById(R.id.cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        this.entryName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (entryName.getText().toString().isEmpty()) {
                    saveButton.setEnabled(false);
                } else {
                    saveButton.setEnabled(true);
                }
            }
        });
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
}
