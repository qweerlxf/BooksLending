package com.example.bookslending.users;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookslending.R;
import com.example.bookslending.database.DatabaseHelper;
import com.example.bookslending.util.ImageUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckBorrowedActivity extends Activity {
    private DatabaseHelper databaseHelper;

    private AlertDialog.Builder builder;
    private TextView bIdView;

    private int uId;
    private long bId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_borrowed);

        SharedPreferences sp = getSharedPreferences("UsersInfo", MODE_PRIVATE);
        uId = sp.getInt("uId", 1);

        builder = new AlertDialog.Builder(this);
        databaseHelper = new DatabaseHelper(this, "BookLending.db", null, 1);
        String SELECT_BORROWED =
                "select b.bId, b.bName, b.bPicture, b.bWriter, b.bType, b.bTime, br.bTime, br.rTime from books b, borrows br " +
                        "where b.bId = (select br.bId where br.uId = ?)";
        Cursor cursor = databaseHelper.getReadableDatabase().rawQuery(SELECT_BORROWED, new String[]{String.valueOf(uId)});

        List<Map<String, Object>> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            Map<String, Object> map = new HashMap<>();

            map.put("bId", cursor.getLong(0));
            map.put("bName", cursor.getString(1));

            String bPicturePath = cursor.getString(2);
            Bitmap bPicture = ImageUtil.getImage(bPicturePath);
            map.put("bPicture", bPicture);

            map.put("bWriter", "作者：" + cursor.getString(3));
            map.put("bType", "类型：" + cursor.getString(4));
            map.put("bTime", "上架时间：" + cursor.getString(5));

            map.put("br_bTime", "借阅时间：" + cursor.getString(6));

            list.add(map);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, list, R.layout.check_borrowed_item,
                new String[]{"bId", "bName", "bPicture", "bWriter", "bType", "bTime", "br_bTime"},
                new int[]{R.id.bId, R.id.bName, R.id.bPicture, R.id.bWriter, R.id.bType, R.id.bTime, R.id.br_bTime});

        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                if (view instanceof ImageView && data instanceof Bitmap) {
                    ImageView imageView = (ImageView) view;
                    imageView.setImageBitmap((Bitmap) data);
                    return true;
                } else {
                    return false;
                }
            }
        });

        ListView listView = findViewById(R.id.borrowedList);
        listView.setAdapter(simpleAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long bId) {
                bIdView = view.findViewById(R.id.bId);
                bId = Long.parseLong(bIdView.getText().toString());

                builder.setMessage("是否要归还这本书？");
                setPositiveButton(builder);
                setNegativeButton(builder);
                builder.create();
                builder.show();
            }
        });
    }

    private AlertDialog.Builder setPositiveButton(AlertDialog.Builder builder) {
        return builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (deleteBorrow(uId, bId) == 1) {
                    Toast.makeText(CheckBorrowedActivity.this, "还书成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CheckBorrowedActivity.this, "还书失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private AlertDialog.Builder setNegativeButton(AlertDialog.Builder builder) {
        return builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(CheckBorrowedActivity.this, "取消还书", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public long deleteBorrow(long uId, long bId) {
        return databaseHelper.getWritableDatabase().delete("borrows", "uId = ? and bId = ?", new String[]{String.valueOf(uId), String.valueOf(bId)});
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
}
