package com.example.bookslending.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookslending.R;
import com.example.bookslending.database.DatabaseHelper;
import com.example.bookslending.util.SharedPreferencesUtil;
import com.example.bookslending.view.UserMainActivity;

import pl.com.salsoft.sqlitestudioremote.SQLiteStudioService;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public int uId;
    public SharedPreferencesUtil sp;
    private EditText edit_uId, edit_uPass;
    private TextView text_admin;
    private Button btn_login, btn_register;
    private ImageButton openpwd;
    private boolean flag = false;
    private String uPass;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

        SQLiteStudioService.instance().start(this);

    }

    private void init() {
        edit_uId = findViewById(R.id.edit_uId);
        edit_uId.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    edit_uId.clearFocus();
                }
                return false;
            }
        });
        edit_uPass = findViewById(R.id.edit_uPass);
        edit_uPass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    edit_uPass.clearFocus();
                    InputMethodManager imm =
                            (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edit_uPass.getWindowToken(), 0);
                }
                return false;
            }
        });
        text_admin = (TextView) findViewById(R.id.text_admin);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (Button) findViewById(R.id.btn_register);
        openpwd = (ImageButton) findViewById(R.id.btn_openpwd);
        text_admin.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        openpwd.setOnClickListener(this);
        dbHelper = new DatabaseHelper(this, "BookLending.db", null, 1);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (edit_uId.getText().toString().trim().equals("") | edit_uPass.getText().
                        toString().trim().equals("")) {
                    Toast.makeText(this, "请输入账号或者注册账号！", Toast.LENGTH_SHORT).show();
                } else {
                    readUserInfo();
                }
                break;
            case R.id.btn_register:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_openpwd:
                if (flag) {//不可见
                    edit_uPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    flag = false;
                    openpwd.setBackgroundResource(R.drawable.eye);
                } else {
                    edit_uPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    flag = true;
                    openpwd.setBackgroundResource(R.drawable.invisible);
                }
                break;
            case R.id.text_admin:
                Intent i = new Intent(LoginActivity.this, AdminLoginActivity.class);
                startActivity(i);
                break;
        }
    }

    /**
     * 读取SharedPreferences存储的键值对
     * */
//    public void readUsersInfo(){
//        SharedPreferences sharedPreferences = getSharedPreferences("UsersInfo",MODE_PRIVATE);
//        uId = sharedPreferences.getInt("uId",0);
//        uPass = sharedPreferences.getString("uPass","");
//    }

    /**
     * 读取IRead.db中的用户信息
     */
    protected void readUserInfo() {
        if (login(edit_uId.getText().toString(), edit_uPass.getText().toString())) {
            Toast.makeText(this, "登录成功！", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, UserMainActivity.class);
            intent.putExtra("uId", edit_uId.getText().toString());
            startActivity(intent);

            SharedPreferences sharedPreferences = getSharedPreferences("UsersInfo", MODE_PRIVATE);
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putInt("uId", Integer.parseInt(edit_uId.getText().toString()));
            edit.putString("uPass", edit_uPass.getText().toString());
            edit.apply();

            SharedPreferences sp = getSharedPreferences("UsersInfo", MODE_PRIVATE);
            int UID = sp.getInt("uId", 0);


        } else {
            Toast.makeText(this, "账户或密码错误，请重新输入！！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 验证登录信息
     */
    public boolean login(String uId, String uPass) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "Select * from users where uId=? and uPass=?";
        Cursor cursor = db.rawQuery(sql, new String[]{uId, uPass});
        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        }
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onDestroy() {
        SQLiteStudioService.instance().stop();
        super.onDestroy();
    }
}