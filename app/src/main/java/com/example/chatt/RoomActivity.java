package com.example.chatt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RoomActivity extends AppCompatActivity {

    private Button btn_sj;
    private Button btn_js;
    private Button btn_so;
    private Button btn_os;
    private String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("출발지 목적지 선택");
        setContentView(R.layout.activity_room);
        Intent intent = getIntent();
        user = intent.getStringExtra("name");
        btn_sj = (Button)findViewById(R.id.btn_sj);
        btn_js = (Button)findViewById(R.id.btn_js);
        btn_so = (Button)findViewById(R.id.btn_so);
        btn_os = (Button)findViewById(R.id.btn_os);

        btn_sj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RoomActivity.this,sjActivity.class);
                intent.putExtra("name",user);
                startActivity(intent);
            }
        });
        btn_js.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RoomActivity.this,jsActivity.class);
                intent.putExtra("name",user);
                startActivity(intent);
            }
        });
        btn_so.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RoomActivity.this,soActivity.class);
                intent.putExtra("name",user);
                startActivity(intent);
            }
        });
        btn_os.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RoomActivity.this,osActivity.class);
                intent.putExtra("name",user);
                startActivity(intent);
            }
        });
    }
}