package com.shareabike.shareabike;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        final SharedPreferences preferences = this.getSharedPreferences("pref", MODE_PRIVATE);
        final EditText emailText = (EditText) findViewById(R.id.input_email);
        final EditText passwordText = (EditText) findViewById(R.id.input_password);
        final Activity self = this;

        Button loginButton = (Button) findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!emailText.getText().toString().matches("[0-9]+")) return;
                int userId = Integer.parseInt(emailText.getText().toString());

                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("user_id", userId);
                editor.apply();

                Intent intent = new Intent();
                intent.putExtra("user_id", userId);

                self.setResult(RESULT_OK, intent);
                self.finish();
            }
        });

        /*
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("user_id", 1);
        editor.apply();
        */
    }
}
