package com.example.experiment2_diary;

import static com.example.experiment2_diary.MainActivity.TAG_INSERT;
import static com.example.experiment2_diary.MainActivity.TAG_UPDATE;
import static com.example.experiment2_diary.MainActivity.dbHelper;
import static com.example.experiment2_diary.MainActivity.getDbHelper;
import static com.example.experiment2_diary.MySQLiteOpenHelper.TABLE_NAME;
import static com.example.experiment2_diary.MySQLiteOpenHelper.TABLE_NAME0;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class EditUserActivity extends AppCompatActivity implements View.OnClickListener {

    private SQLiteDatabase db;
    TextView username_modify0;
    EditText password_modify0;
    EditText telephone_modify0;
    EditText mail_modify0;
    private Button b_modify;
    String author_using;

    public MySQLiteOpenHelper deHelper = getDbHelper();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        username_modify0 = findViewById(R.id.rusername_modify);
        password_modify0 = findViewById(R.id.rpassword_modify);
        telephone_modify0 = findViewById(R.id.rphone_modify);
        mail_modify0 = findViewById(R.id.remil_modify);
        b_modify = findViewById(R.id.mmodify);
        b_modify.setOnClickListener(this);
        password_modify0.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        Intent intent = getIntent();
        author_using = intent.getExtras().getString("author", "");
        db = dbHelper.getWritableDatabase();


        Cursor cursor = db.query(TABLE_NAME0, null, "username=?", new String[]{String.valueOf(author_using)}, null, null, null);
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String select_password = cursor.getString(cursor.getColumnIndex("password"));
            @SuppressLint("Range") String select_telephone = cursor.getString(cursor.getColumnIndex("phone"));
            @SuppressLint("Range") String select_mail = cursor.getString(cursor.getColumnIndex("emil"));
            username_modify0.setText(author_using);
            password_modify0.setText(select_password);
            telephone_modify0.setText(select_telephone);
            mail_modify0.setText(select_mail);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_userinfo, menu);
        return true;
    }

    public void modify() {
            String update_password = password_modify0.getText().toString();
            String update_phone = telephone_modify0.getText().toString();
            String update_mail = mail_modify0.getText().toString();
            MD5 jiami_md5 = new MD5(update_password,author_using);
            update_password = jiami_md5.getMD5();
            ContentValues values = new ContentValues();
            values.put("password", update_password);
            values.put("phone", update_phone);
            values.put("emil", update_mail);
            db.update(TABLE_NAME0, values, "username=?", new String[]{String.valueOf(author_using)});
            finish();
    }

    public void gobak_modify(View view){
        Intent gl = new Intent(EditUserActivity.this,MainActivity.class);
        startActivity(gl);
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.mmodify) {
            modify();
        }
    }
}