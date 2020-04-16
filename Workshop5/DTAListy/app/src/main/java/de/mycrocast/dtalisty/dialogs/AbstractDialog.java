package de.mycrocast.dtalisty.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public abstract class AbstractDialog extends DialogFragment {

    protected TextView nameView;
    protected Button saveButton;
    protected Button cancelButton;

    protected void configure(View view) {
        this.nameView = view.findViewById(this.getNameViewResource());
        this.saveButton = view.findViewById(this.getSaveButtonResource());
        this.cancelButton = view.findViewById(this.getCancelButtonResource());

        this.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        this.nameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (nameView.getText().toString().isEmpty()) {
                    saveButton.setEnabled(false);
                } else {
                    saveButton.setEnabled(true);
                }
            }
        });
    }

    protected abstract int getNameViewResource();

    protected abstract int getSaveButtonResource();

    protected abstract int getCancelButtonResource();

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

}
