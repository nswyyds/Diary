package com.example.experiment2_diary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mySQLiteOpenHelper = new MySQLiteOpenHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //初始化获取的布局元素
        find();
    }
    //获取注册按钮，预先定义，下方输入框同理
    private Button register;
    private EditText username,password,phone,emil;

    //使用MySQLiteOpenHelper类中的方法
    private MySQLiteOpenHelper mySQLiteOpenHelper;

    //控制注册按钮点击完后不给点击了
    boolean flag = false;

    //跳转回登录页面
    public void gobak(View view){
        Intent gl = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(gl);
    }
    //初始化获取的布局元素
    public void find(){
        username = findViewById(R.id.rusername);
        password = findViewById(R.id.rpassword);
        phone = findViewById(R.id.rphone);
        emil = findViewById(R.id.remil);
        register = findViewById(R.id.mregister);
        register.setOnClickListener(this);
    }
    //注册逻辑实现
    public void register(){
        String ru = username.getText().toString();
        String rps = password.getText().toString();
        String rph = phone.getText().toString();
        String rem = emil.getText().toString();

        if(ru.equals("")){
            Toast.makeText(this, "账号不能为空！", Toast.LENGTH_SHORT).show();
        } else if (rps.equals("")) {
            Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show();
        } else if (rph.equals("")) {
            Toast.makeText(this, "电话不能为空!", Toast.LENGTH_SHORT).show();
        }else if(rem.equals("")){
            Toast.makeText(this, "邮箱不能为空！", Toast.LENGTH_SHORT).show();
        }else{

            MD5 jiami_md5 = new MD5(rps,ru);
            rps = jiami_md5.getMD5();
            User user = new User(ru,rps,rph,rem);
            System.out.println(rps);
            long r1 = mySQLiteOpenHelper.register(user);
            register.setEnabled(false);
            register.setTextColor(0xFFD0EFC6);
            if(r1!=-1){
                Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(this, "注册失败！", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    //监听按钮点击事件
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.mregister) {
            register();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}