package com.example.bookslending.admin;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookslending.R;
import com.example.bookslending.database.DatabaseHelper;
import com.example.bookslending.util.ImageUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddBooksActivity extends Activity {
    private static final int CHOOSE_PHOTO = 0;
    private DatabaseHelper databaseHelper;

    private TextView bIdText;
    private EditText bNameText;
    private EditText bWriterText;

    private ImageView bPictureView;
    private Button bPictureBtn;

    private RadioGroup bTypeGroup;
    private RadioButton literatureBtn;
    private RadioButton philosophyBtn;
    private RadioButton poetryBtn;
    private RadioButton fictionBtn;

    private EditText bTimeText;
    private Button bTimeBtn;

    private Button resetBtn;
    private Button addBtn;

    private Bitmap bitmap;
    private long bId;
    private String bName;
    private String bPicture;
    private String bWriter;
    private String bType;
    private String bTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_books);

        databaseHelper = new DatabaseHelper(this, "BookLending.db", null, 1);

        bIdText = findViewById(R.id.bId);
        bNameText = findViewById(R.id.bName);
        bWriterText = findViewById(R.id.bWriter);

        bPictureView = findViewById(R.id.bPicture);
        bPictureBtn = findViewById(R.id.bPictureSelect);

        bTypeGroup = findViewById(R.id.bType);
        literatureBtn = findViewById(R.id.bType_literature);
        philosophyBtn = findViewById(R.id.bType_philosophy);
        poetryBtn = findViewById(R.id.bType_poetry);
        fictionBtn = findViewById(R.id.bType_fiction);

        bTimeText = findViewById(R.id.bTime);
        bTimeBtn = findViewById(R.id.bTimeSelect);

        resetBtn = findViewById(R.id.reset);
        addBtn = findViewById(R.id.add);

        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        bIdText.setText(simpleDateFormat.format(date));

        bPictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setType("image/*");
                startActivityForResult(intent, CHOOSE_PHOTO);
            }
        });

        bTypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.bType_literature) {
                    literatureBtn.setChecked(true);
                    bType = "文学";
                } else if (checkedId == R.id.bType_philosophy) {
                    philosophyBtn.setChecked(true);
                    bType = "哲学";
                } else if (checkedId == R.id.bType_poetry) {
                    poetryBtn.setChecked(true);
                    bType = "诗歌";
                } else if (checkedId == R.id.bType_fiction) {
                    fictionBtn.setChecked(true);
                    bType = "小说";
                }
            }
        });

        bTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                new DatePickerDialog(AddBooksActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        bTimeText.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bNameText.setText("");
                bWriterText.setText("");
                bPictureView.setImageBitmap(null);
                bTypeGroup.clearCheck();
                bTimeText.setText("");
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bId = Long.parseLong(bIdText.getText().toString());
                bName = bNameText.getText().toString();
                bWriter = bWriterText.getText().toString();
                bTime = bTimeText.getText().toString();

                insertBook(bId, bName, bPicture, bWriter, bType, bTime);
                Toast.makeText(AddBooksActivity.this, "添加成功", Toast.LENGTH_SHORT).show();

                /*if (insertBook(bId, bName, bPicture, bWriter, bType, bTime) == 1) {
                    Toast.makeText(AddBooksActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddBooksActivity.this, "添加失败", Toast.LENGTH_SHORT).show();
                }*/
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHOOSE_PHOTO) {
            if (resultCode == RESULT_OK) {
                bitmap = ImageUtil.handleImageOnKitKat(this, data);
                bPictureView.setImageBitmap(bitmap);
                bPicture = ImageUtil.imagePath;
            }
        }
    }

    public long insertBook(long bId, String bName, String bPicture, String bWriter, String bType, String bTime) {
        ContentValues contentValues = new ContentValues();

        contentValues.put("bId", bId);
        contentValues.put("bName", bName);
        contentValues.put("bPicture", bPicture);
        contentValues.put("bWriter", bWriter);
        contentValues.put("bType", bType);
        contentValues.put("bTime", bTime);

        return databaseHelper.getWritableDatabase().insert("books", null, contentValues);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
}
