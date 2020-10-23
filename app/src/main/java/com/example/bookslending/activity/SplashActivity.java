package com.example.bookslending.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.example.bookslending.R;
import com.example.bookslending.util.SharedPreferencesUtil;
import com.example.bookslending.view.UserMainActivity;

public class SplashActivity extends AppCompatActivity {
    private SharedPreferencesUtil sp;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            next();
        }
    };

    private void next() {

        Intent intent = null;
        if (sp.isLogin()) {
            //已经登录，跳转到首页
            intent = new Intent(this, UserMainActivity.class);
        } else {
            //否则跳转到登录界面
            intent = new Intent(this, LoginActivity.class);
            //引导界面
        }
        startActivity(intent);

        //关闭当前页面
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sp = SharedPreferencesUtil.getInstance(getApplicationContext());

        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //3秒后调用

                //这里的调用是在子线程，不能直接操作UI，需要用handler切换到主线程
                //用多个子线程的目的解决，如果有耗时任务，那么就会卡界面
                //而用了多线程后，将耗时任务放到子线程，这样主线程（UI线程）就不会卡住
                handler.sendEmptyMessage(0);
            }
        }, 1500);
    }
}