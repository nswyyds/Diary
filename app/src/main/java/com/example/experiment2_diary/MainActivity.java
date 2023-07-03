package com.example.experiment2_diary;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.example.experiment2_diary.MySQLiteOpenHelper.TABLE_NAME;
import static com.example.experiment2_diary.MySQLiteOpenHelper.DB_NAME;
import static com.example.experiment2_diary.MySQLiteOpenHelper.VERSION;

public class MainActivity extends AppCompatActivity {

    public static MySQLiteOpenHelper dbHelper;
    private SQLiteDatabase db;
    private List<String> diary=new ArrayList<>();
    public static final int TAG_INSERT=1;
    public static final int TAG_UPDATE=0;
    private String select_item;
    public String author_using;
    private int Id;
    ListView listView;
    ArrayAdapter<String> adapter;

    private SwipeRefreshLayout swipeRefresh;
    private static final int Edit_Diary=2;
    public static MySQLiteOpenHelper getDbHelper(){
        return dbHelper;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dbHelper=new MySQLiteOpenHelper(MainActivity.this,DB_NAME,null,VERSION);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button add= findViewById(R.id.add);
        swipeRefresh= findViewById(R.id.swipe_refresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            String username = bundle.getString("username");
            author_using = username;
        }
        dbHelper.getWritableDatabase();
        init();
        //添加笔记
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,EditDiaryActivity.class);
                intent.putExtra("TAG",TAG_INSERT);
                intent.putExtra("author",author_using);
                startActivityForResult(intent,Edit_Diary);
            }
        });

        //设置列表项目点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent=new Intent(MainActivity.this,EditDiaryActivity.class);
                Id=getDiaryId(position);
                intent.putExtra("ID",Id);
                intent.putExtra("TAG",TAG_UPDATE);
                intent.putExtra("author",author_using);
                startActivityForResult(intent,Edit_Diary);
            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent  data) {
        super.onActivityResult(requestCode, resultCode, data);
        init();

    }


    @SuppressLint("Range")
    private void init(){
        db=dbHelper.getWritableDatabase();
        diary.clear();
        //查询数据库，将title一列添加到列表项目中
        Cursor cursor=db.query(TABLE_NAME,null,"author=?",new String[]{String.valueOf(author_using)},null,null,null);
        if(cursor.moveToFirst()){
            String diary_item;
            do{
                diary_item = cursor.getString(cursor.getColumnIndex("title"));
                diary.add(diary_item);
            }while(cursor.moveToNext());
        }
        cursor.close();
        adapter=new ArrayAdapter<String>(
                MainActivity.this,android.R.layout.simple_list_item_1,diary);
        listView=findViewById(R.id.list_item);
        listView.setAdapter(adapter);
    }

    //刷新列表
    private void refresh(){
        new Thread(new Runnable(){
            @Override
            public void run() {
                try{
                    Thread.sleep(1000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        init();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }
    //将menu中的actionbar添加进来
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_userinfo,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.userinfo) {
            Intent jump_user = new Intent(MainActivity.this,EditUserActivity.class);
            jump_user.putExtra("author",author_using);
            startActivity(jump_user);
        }
        return true;
    }


    @SuppressLint("Range")
    private int getDiaryId(int position){
        //获取所点击的日记的title
        int Id;
        select_item=diary.get(position);
        //获取id
        db=dbHelper.getWritableDatabase();
        Cursor cursor=db.query(TABLE_NAME,new String[]{"id"},"title=?",
                new String[]{select_item},null,null,null);
        cursor.moveToFirst();
        Id=cursor.getInt(cursor.getColumnIndex("id"));
        return Id;
    }
}
