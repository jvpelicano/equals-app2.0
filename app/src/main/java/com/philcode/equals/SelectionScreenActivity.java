package com.philcode.equals;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.philcode.equals.EMP.LoginActivity_emp;
import com.philcode.equals.PWD.PWD_LoginActivity;



public class SelectionScreenActivity extends Activity {

    private Button btnEMP, btnPWD;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_pwd_emp);

        btnEMP= (Button) findViewById(R.id.buttonSignin_emp);
        btnPWD = (Button) findViewById(R.id.buttonSignin_pwd);

        btnPWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectionScreenActivity.this, PWD_LoginActivity.class));
            }
        });
        btnEMP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectionScreenActivity.this, LoginActivity_emp.class));
            }
        });
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        }
        return super.onKeyDown(keyCode, event);
    }
}