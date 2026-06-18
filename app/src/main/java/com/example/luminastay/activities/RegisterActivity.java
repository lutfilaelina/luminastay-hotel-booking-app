package com.example.luminastay.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.luminastay.R;
import com.example.luminastay.database.DBHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText etName, etEmail, etPassword, etConfirmPassword;
    private MaterialButton btnRegister;
    private TextView tvLogin;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize views
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);

        dbHelper = new DBHelper(this);

        // Register button click
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();

                // Validation
                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Snackbar.make(v, getString(R.string.fill_all_fields), Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Snackbar.make(v, getString(R.string.invalid_email), Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Snackbar.make(v, "Password minimal 6 karakter", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    Snackbar.make(v, getString(R.string.password_not_match), Snackbar.LENGTH_SHORT).show();
                    return;
                }

                // Register user
                boolean isRegistered = dbHelper.registerUser(name, email, password);

                if (isRegistered) {
                    Snackbar.make(v, getString(R.string.register_success), Snackbar.LENGTH_SHORT).show();

                    // Go back to login
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Snackbar.make(v, "Email sudah terdaftar", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        // Login link click
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}