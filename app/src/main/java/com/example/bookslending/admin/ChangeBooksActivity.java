package com.example.bookslending.admin;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.bookslending.R;
import com.example.bookslending.database.DatabaseHelper;
import com.example.bookslending.util.ImageUtil;

public class ChangeBooksActivity extends Activity {
    private static final int CHOOSE_PHOTO = 0;
    private DatabaseHelper databaseHelper;

    private EditText bNameText;
    private EditText bWriterText;

    private ImageView bPictureView;
    private Button bPictureBtn;

    private RadioGroup bTypeGroup;
    private RadioButton literatureBtn;
    private RadioButton philosophyBtn;
    private RadioButton poetryBtn;
    private RadioButton fictionBtn;

    private Button resetBtn;
    private Button changeBtn;

    private Bitmap bitmap;
    private long bId;
    private String bName;
    private String bPicture;
    private String bWriter;
    private String bType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_books);

        databaseHelper = new DatabaseHelper(this, "BookLending.db", null, 1);

        bNameText = findViewById(R.id.bName);
        bWriterText = findViewById(R.id.bWriter);

        bPictureView = findViewById(R.id.bPicture);
        bPictureBtn = findViewById(R.id.bPictureSelect);

        bTypeGroup = findViewById(R.id.bType);
        literatureBtn = findViewById(R.id.bType_literature);
        philosophyBtn = findViewById(R.id.bType_philosophy);
        poetryBtn = findViewById(R.id.bType_poetry);
        fictionBtn = findViewById(R.id.bType_fiction);

        resetBtn = findViewById(R.id.reset);
        changeBtn = findViewById(R.id.change);

        Intent intent = getIntent();

        bId = intent.getLongExtra("bId", 0);
        bName = intent.getStringExtra("bName");
        bNameText.setText(bName);
        //bPicture = intent.getStringExtra("bPicture");
        bWriter = intent.getStringExtra("bWriter");
        bWriterText.setText(bWriter);
        bType = intent.getStringExtra("bType");
        switch (bType) {
            case "文学":
                literatureBtn.setChecked(true);
                break;
            case "哲学":
                philosophyBtn.setChecked(true);
                break;
            case "诗歌":
                poetryBtn.setChecked(true);
                break;
            case "小说":
                fictionBtn.setChecked(true);
                break;
        }

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

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bNameText.setText("");
                bWriterText.setText("");
                bPictureView.setImageBitmap(null);
                bTypeGroup.clearCheck();
            }
        });

        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bName = bNameText.getText().toString();
                bWriter = bWriterText.getText().toString();

                if (updateBook(bId, bName, bPicture, bWriter, bType) == 1) {
                    Toast.makeText(ChangeBooksActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ChangeBooksActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                }
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

    public int updateBook(long bId, String bName, String bPicture, String bWriter, String bType) {
        ContentValues contentValues = new ContentValues();

        contentValues.put("bName", bName);
        contentValues.put("bPicture", bPicture);
        contentValues.put("bWriter", bWriter);
        contentValues.put("bType", bType);

        return databaseHelper.getWritableDatabase().update("books", contentValues, "bId = ?", new String[]{String.valueOf(bId)});
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
}
