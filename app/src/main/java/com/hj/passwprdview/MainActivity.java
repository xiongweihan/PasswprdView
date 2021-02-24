package com.hj.passwprdview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PasswordEditText psdEdit = findViewById(R.id.psd_edit);

        psdEdit.setOnTextChangedListener(text -> Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show());

    }
}