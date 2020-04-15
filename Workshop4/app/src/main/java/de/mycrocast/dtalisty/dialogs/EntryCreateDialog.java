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

public class EntryCreateDialog extends DialogFragment {

    public interface OnEntryCreated {
        void onEntryCreated(String name, Entry.Priority priority);
    }

    private EditText entryName;
    private RadioGroup radioGroup;
    private Button saveButton;
    private OnEntryCreated onEntryCreatedCallback;

    public EntryCreateDialog(OnEntryCreated entryCreatedCallback) {
        this.onEntryCreatedCallback = entryCreatedCallback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.entry_creation_dialog, container, false);

        this.entryName = view.findViewById(R.id.entryName);
        this.radioGroup = view.findViewById(R.id.priorityGroup);
        this.saveButton = view.findViewById(R.id.save);
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
                        priority = Entry.Priority.NORMAL;
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
                onEntryCreatedCallback.onEntryCreated(entryName.getText().toString(), priority);
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
