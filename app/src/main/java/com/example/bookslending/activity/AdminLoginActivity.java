package com.example.bookslending.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bookslending.R;
import com.example.bookslending.util.Constants;
import com.example.bookslending.util.SharedPreferencesUtil;
import com.example.bookslending.view.AdminMainActivity;


public class AdminLoginActivity extends Activity implements View.OnClickListener {

    private EditText et_username;
    private EditText et_password;
    private SharedPreferencesUtil sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        Button bt_login = findViewById(R.id.bt_login);

        bt_login.setOnClickListener(this);

        sp = SharedPreferencesUtil.getInstance(getApplicationContext());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login:
                login();
                break;
        }
    }

    private void login() {
        //获取用户输入的帐号，密码，做校验
        String username = et_username.getText().toString().trim();
        //判断是否输入了账号
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, R.string.uId_hint, Toast.LENGTH_SHORT).show();
            return;
        }

        String password = et_password.getText().toString().trim();

        //是否输入了密码
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, R.string.password_hint, Toast.LENGTH_SHORT).show();
            return;
        }

        //调用调用服务端的登录接口
        if ((Constants.USERNAME.equals(username) && Constants.PASSWORD.equals(password))) {

            //登录成功，进入首页
            Intent intent = new Intent(this, AdminMainActivity.class);
            startActivity(intent);

            //关闭当前页面
            finish();
        } else {
            //登录失败，进行提示
            Toast.makeText(this, R.string.username_or_password_error, Toast.LENGTH_SHORT).show();
        }
    }

}
