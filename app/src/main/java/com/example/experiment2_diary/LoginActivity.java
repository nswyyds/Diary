package com.example.experiment2_diary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button register,login;
    private EditText username,password;
    private MySQLiteOpenHelper mySQLiteOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mySQLiteOpenHelper = new MySQLiteOpenHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        find();
        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    public void find(){
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        username = findViewById(R.id.lusername);
        password = findViewById(R.id.lpassword);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        if (id == R.id.login) {
            String n = username.getText().toString();
            String p = password.getText().toString();
            if (n.equals("")) {
                Toast.makeText(this, "请输入账号", Toast.LENGTH_SHORT).show();
            } else if (p.equals("")) {
                Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            } else {
                boolean login = mySQLiteOpenHelper.login(n, p);
                if (login) {
                    Toast.makeText(this, "登录成功！", Toast.LENGTH_SHORT).show();
                    User select = mySQLiteOpenHelper.select(n);
                    Intent home = new Intent(LoginActivity.this, MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("username", select.username);
                    bundle.putString("emil", select.emil);
                    bundle.putString("phone", select.phone);
                    home.putExtras(bundle);
                    username.setText("");
                    password.setText("");
                    startActivity(home);
                } else {
                    Toast.makeText(this, "账号或密码错误，请重新输入", Toast.LENGTH_SHORT).show();
                    password.setText("");
                }
            }
        } else if (id == R.id.register) {
            Intent re = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(re);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}