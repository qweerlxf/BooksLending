package com.example.bookslending.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.bookslending.R;
import com.example.bookslending.activity.LoginActivity;
import com.example.bookslending.admin.AddBooksActivity;
import com.example.bookslending.admin.CheckBooksActivity;

public class AdminMainActivity extends AppCompatActivity {

    private Button addBooksBtn;
    private Button checkBooksBtn;
    private Button updataBooksBtn;
    private Button bt_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        addBooksBtn = findViewById(R.id.addBooks);
        checkBooksBtn = findViewById(R.id.checkBooks);
        updataBooksBtn = findViewById(R.id.updataBooks);
        bt_out = findViewById(R.id.bt_out);

        addBooksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainActivity.this, AddBooksActivity.class);
                startActivity(intent);
            }
        });

        checkBooksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainActivity.this, CheckBooksActivity.class);
                startActivity(intent);
            }
        });

        updataBooksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainActivity.this, CheckBooksActivity.class);
                startActivity(intent);
            }
        });

        bt_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }


}
