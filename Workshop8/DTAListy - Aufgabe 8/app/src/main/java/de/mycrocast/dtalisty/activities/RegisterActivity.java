package de.mycrocast.dtalisty.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import de.mycrocast.dtalisty.R;
import de.mycrocast.dtalisty.data.User;
import de.mycrocast.dtalisty.messaging.response.BasicResponse;
import de.mycrocast.dtalisty.messaging.restcaller.UserRestCaller;

public class RegisterActivity extends AbstractActivity implements View.OnClickListener {

    public static Intent createStartIntent(Context context) {
        return new Intent(context, RegisterActivity.class);
    }

    private UserRestCaller userRestCaller;

    // are we currently showing an error message in the username input field?
    private boolean usernameErrorEnabled;
    // are we currently showing an error message in the password input field?
    private boolean passwordErrorEnabled;
    // are we currently showing an error message in the confirm password input field?
    private boolean confirmPasswordErrorEnabled;

    private TextInputLayout usernameInputLayout;
    private TextInputLayout passwordInputLayout;
    private TextInputLayout confirmPasswordInputLayout;

    private TextInputEditText usernameInput;
    private TextInputEditText passwordInput;
    private TextInputEditText confirmPasswordInput;

    private AppCompatButton registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_register);

        this.userRestCaller = this.getUserRestCaller();

        this.usernameInputLayout = this.findViewById(R.id.input_register_username);
        this.passwordInputLayout = this.findViewById(R.id.input_register_password);
        this.confirmPasswordInputLayout = this.findViewById(R.id.input_register_confirm_password);

        this.usernameInput = this.findViewById(R.id.text_register_username);
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
                    if (RegisterActivity.this.usernameErrorEnabled) {
                        RegisterActivity.this.usernameErrorEnabled = false;

                        RegisterActivity.this.usernameInputLayout.setErrorEnabled(false);
                        RegisterActivity.this.usernameInputLayout.setError(null);
                    }
                }
            }
        });

        this.passwordInput = this.findViewById(R.id.text_register_password);
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
                    if (RegisterActivity.this.passwordErrorEnabled) {
                        RegisterActivity.this.passwordErrorEnabled = false;

                        RegisterActivity.this.passwordInputLayout.setErrorEnabled(false);
                        RegisterActivity.this.passwordInputLayout.setError(null);
                    }
                }
            }
        });

        this.confirmPasswordInput = this.findViewById(R.id.text_register_confirm_password);
        this.confirmPasswordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // we want to clear the error message (if we are currently showing one), when we the confirmed password matches with the entered password
                if (s.toString().equals(RegisterActivity.this.passwordInput.getEditableText().toString())) {
                    if (RegisterActivity.this.confirmPasswordErrorEnabled) {
                        RegisterActivity.this.confirmPasswordErrorEnabled = false;

                        RegisterActivity.this.confirmPasswordInputLayout.setErrorEnabled(false);
                        RegisterActivity.this.confirmPasswordInputLayout.setError(null);
                    }
                }
            }
        });

        this.registerButton = this.findViewById(R.id.button_register_user);
        this.registerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View clickedView) {
        // did we click on the register button?
        if (clickedView.getId() == this.registerButton.getId()) {
            this.onRegisterClicked();
        }
    }

    private void onRegisterClicked() {
        String username = this.usernameInput.getEditableText().toString();
        String password = this.passwordInput.getEditableText().toString();
        String confirmedPassword = this.confirmPasswordInput.getEditableText().toString();

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
                this.passwordErrorEnabled = false;

                this.passwordInputLayout.setErrorEnabled(false);
                this.passwordInputLayout.setError(null);
            }
        }

        // do we need to display the error message? (we want to display the error message if the entered confirmed password does not match with the entered password)
        if (!confirmedPassword.equals(password)) {
            // do we already show the error message? -> yes: nothing needs to be done
            if (!this.confirmPasswordErrorEnabled) {
                // no -> show the error message
                this.confirmPasswordErrorEnabled = true;

                String errorText = this.getResources().getString(R.string.error_password_mismatch);
                this.confirmPasswordInputLayout.setError(errorText);
                this.confirmPasswordInputLayout.setErrorEnabled(true);
            }
        } else {
            // do we show the error message?
            if (this.confirmPasswordErrorEnabled) {
                // yes -> dismiss the previous shown error message
                this.confirmPasswordErrorEnabled = false;

                this.confirmPasswordInputLayout.setErrorEnabled(false);
                this.confirmPasswordInputLayout.setError(null);
            }
        }

        // if both input fields have valid input, we want to send the login request to our backend server
        if (!this.usernameErrorEnabled && !this.passwordErrorEnabled && !this.confirmPasswordErrorEnabled) {
            this.userRestCaller.register(username, password, new Response.Listener<BasicResponse<User>>() {
                @Override
                public void onResponse(BasicResponse<User> response) {
                    if (response.getError() == null || response.getError().isEmpty()) {
                        Intent intent = MainActivity.createStartIntent(RegisterActivity.this);
                        RegisterActivity.this.startActivity(intent);
                        RegisterActivity.this.finish();
                    } else {
                        //TODO something more to show there was an error
                        Toast.makeText(RegisterActivity.this, response.getError(), Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // todo: show meaningful message to the end user for feedback
                    System.out.println(error.getMessage());
                }
            });
        }
    }
}
