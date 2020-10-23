package com.example.bookslending.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.bookslending.R;
import com.example.bookslending.activity.LoginActivity;
import com.example.bookslending.users.CheckBorrowedActivity;
import com.example.bookslending.users.CheckNotBorrowActivity;


public class UserMainActivity extends AppCompatActivity {

    private Button checkNotBorrowBtn;
    private Button checkBorrowed;
    private Button myBorrowed;
    private Button bt_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        checkNotBorrowBtn = findViewById(R.id.checkNotBorrow);
        checkBorrowed = findViewById(R.id.checkBorrowed);
        myBorrowed = findViewById(R.id.myBorrowed);
        bt_out = findViewById(R.id.bt_out);

        checkNotBorrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserMainActivity.this, CheckNotBorrowActivity.class);
                startActivity(intent);
            }
        });

        checkBorrowed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserMainActivity.this, CheckBorrowedActivity.class);
                startActivity(intent);
            }
        });

        myBorrowed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserMainActivity.this, CheckBorrowedActivity.class);
                startActivity(intent);
            }
        });

        bt_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserMainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

}
