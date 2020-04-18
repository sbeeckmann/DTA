package de.mycrocast.dtalisty.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import de.mycrocast.dtalisty.R;
import de.mycrocast.dtalisty.messaging.UserRestCaller;

public class LoginActivity extends AbstractActivity implements View.OnClickListener {

    private UserRestCaller userRestCaller;

    // are we currently showing an error message in the username input field?
    private boolean usernameErrorEnabled;
    // are we currently showing an error message in the password input field?
    private boolean passwordErrorEnabled;

    private TextInputLayout usernameInputLayout;
    private TextInputLayout passwordInputLayout;
    private TextInputEditText usernameInput;
    private TextInputEditText passwordInput;
    private AppCompatButton loginButton;
    private AppCompatButton registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_login);

        this.userRestCaller = this.getUserRestCaller();

        this.usernameInputLayout = this.findViewById(R.id.input_username);
        this.passwordInputLayout = this.findViewById(R.id.input_password);

        this.usernameInput = this.findViewById(R.id.text_username);
        this.usernameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // we want to clear the error message (if we are currently showing one), when we have some input in our input field
                if (!s.toString().isEmpty()) {
                    if (LoginActivity.this.usernameErrorEnabled) {
                        LoginActivity.this.usernameErrorEnabled = false;

                        LoginActivity.this.usernameInputLayout.setErrorEnabled(false);
                        LoginActivity.this.usernameInputLayout.setError(null);
                    }
                }
            }
        });

        this.passwordInput = this.findViewById(R.id.text_password);
        this.passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // we want to clear the error message (if we are currently showing one), when we have some input in our input field
                if (!s.toString().isEmpty()) {
                    if (LoginActivity.this.passwordErrorEnabled) {
                        LoginActivity.this.passwordErrorEnabled = false;

                        LoginActivity.this.passwordInputLayout.setErrorEnabled(false);
                        LoginActivity.this.passwordInputLayout.setError(null);
                    }
                }
            }
        });

        this.loginButton = this.findViewById(R.id.button_login);
        this.loginButton.setOnClickListener(this);

        this.registerButton = this.findViewById(R.id.button_register);
        this.registerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View clickedView) {
        // did we click on the login button?
        if (clickedView.getId() == this.loginButton.getId()) {
            this.onLoginClicked();
        }

        // did we click on the register button?
        if (clickedView.getId() == this.registerButton.getId()) {
            this.onRegisterClicked();
        }
    }

    private void onLoginClicked() {
        String username = this.usernameInput.getEditableText().toString();
        String password = this.passwordInput.getEditableText().toString();

        // do we need to display the error message? (we want to display the error message if we have no text in the input field)
        if (username.isEmpty()) {
            // do we already show the error message? -> yes: nothing needs to be done
            if (!this.usernameErrorEnabled) {
                // no -> show the error message
                this.usernameErrorEnabled = true;

                String errorText = this.getResources().getString(R.string.error_username_empty);
                this.usernameInputLayout.setError(errorText);
                this.usernameInputLayout.setErrorEnabled(true);
            }
        } else {
            // do we show the error message?
            if (this.usernameErrorEnabled) {
                // yes -> dismiss the previous shown error message
                this.usernameErrorEnabled = false;

                this.usernameInputLayout.setErrorEnabled(false);
                this.usernameInputLayout.setError(null);
            }
        }

        // do we need to display the error message? (we want to display the error message if we have no text in the input field)
        if (password.isEmpty()) {
            // do we already show the error message? -> yes: nothing needs to be done
            if (!this.passwordErrorEnabled) {
                // no -> show the error message
                this.passwordErrorEnabled = true;

                String errorText = this.getResources().getString(R.string.error_password_empty);
                this.passwordInputLayout.setError(errorText);
                this.passwordInputLayout.setErrorEnabled(true);
            }
        } else {
            // do we show the error message?
            if (this.passwordErrorEnabled) {
                // yes -> dismiss the previous shown error message
                this.passwordInputLayout.setErrorEnabled(false);
                this.passwordInputLayout.setError(null);
            }
        }

        // if both input fields have valid input, we want to send the login request to our backend server
        if (!username.isEmpty() && !password.isEmpty()) {
            // TODO: send login request
        }
    }

    private void onRegisterClicked() {
    }
}
