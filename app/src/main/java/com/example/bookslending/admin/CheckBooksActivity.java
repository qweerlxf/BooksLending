package com.example.bookslending.admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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

public class CheckBooksActivity extends Activity {
    private DatabaseHelper databaseHelper;
    private AlertDialog.Builder builder;

    private TextView bIdView;
    private TextView bNameView;
    private ImageView bPictureView;
    private TextView bWriterView;
    private TextView bTypeView;

    private long bId;
    private String bName;
    private Drawable bPicture;
    private String bWriter;
    private String bType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_books);

        builder = new AlertDialog.Builder(this);
        databaseHelper = new DatabaseHelper(this, "BookLending.db", null, 1);
        String SELECT_BOOKS = "select bId, bName, bPicture, bWriter, bType, bTime from books";
        Cursor cursor = databaseHelper.getReadableDatabase().rawQuery(SELECT_BOOKS, null);

        List<Map<String, Object>> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            Map<String, Object> map = new HashMap<>();

            map.put("bId", cursor.getLong(0));
            map.put("bName", cursor.getString(1));

            String bPicturePath = cursor.getString(2);
            Bitmap bPicture = ImageUtil.getImage(bPicturePath);
            map.put("bPicture", bPicture);

            map.put("bWriter", cursor.getString(3));
            map.put("bType", cursor.getString(4));
            map.put("bTime", cursor.getString(5));

            list.add(map);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, list, R.layout.check_books_item,
                new String[]{"bId", "bName", "bPicture", "bWriter", "bType", "bTime"},
                new int[]{R.id.bId, R.id.bName, R.id.bPicture, R.id.bWriter, R.id.bType, R.id.bTime});

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

        ListView listView = findViewById(R.id.booksList);
        listView.setAdapter(simpleAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bIdView = view.findViewById(R.id.bId);
                bId = Long.parseLong(bIdView.getText().toString());

                bNameView = view.findViewById(R.id.bName);
                bName = bNameView.getText().toString();

                bPictureView = view.findViewById(R.id.bPicture);
                bPicture = bPictureView.getDrawable();

                bWriterView = view.findViewById(R.id.bWriter);
                bWriter = bWriterView.getText().toString();

                bTypeView = view.findViewById(R.id.bType);
                bType = bTypeView.getText().toString();

                builder.setMessage("请选择相应操作：");
                setPositiveButton(builder);
                setNegativeButton(builder);
                builder.create();
                builder.show();
            }
        });
    }

    private AlertDialog.Builder setPositiveButton(AlertDialog.Builder builder) {
        return builder.setPositiveButton("修改", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(CheckBooksActivity.this, ChangeBooksActivity.class);

                intent.putExtra("bId", bId);
                intent.putExtra("bName", bName);
                //intent.putExtra("bPicture", bPicture);
                intent.putExtra("bWriter", bWriter);
                intent.putExtra("bType", bType);

                startActivity(intent);
            }
        });
    }

    private AlertDialog.Builder setNegativeButton(AlertDialog.Builder builder) {
        return builder.setNegativeButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (deleteBook(bId) == 1) {
                    Toast.makeText(CheckBooksActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CheckBooksActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public long deleteBook(long bId) {
        return databaseHelper.getWritableDatabase().delete("books", "bId = ?", new String[]{String.valueOf(bId)});
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
}
