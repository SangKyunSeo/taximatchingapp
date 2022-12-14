package com.example.chatt;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

class osshow{
    public String room;
    public String TimeStamp;
    public osshow(){};
    public osshow(String TimeStamp,String room){
        this.TimeStamp = TimeStamp;
        this.room=room;
    }
}

public class osActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    public LinearLayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    private Button btn_create;
    arrayAdapter arrayAdapter;

    //private ArrayAdapter<String> arrayAdapter;
    private ArrayList<chat> arr_roomList = new ArrayList<>();

    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private String name;
    private String str_name;
    private String str_room;
    private Date date;
    private long now;
    private long end;
    private String time;
    private int count=0;

    LinkedHashMap<String, Object> map = new LinkedHashMap<>();
    LinkedHashMap<String, Object> t_map = new LinkedHashMap<>();
    osshow s = new osshow();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTitle("Taxi");
        setContentView(R.layout.activity_otos);
        Intent intent = getIntent();

        str_name = intent.getStringExtra("name");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_list);
        btn_create = (Button)findViewById(R.id.btn_create);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
//        recyclerView. = layoutManager;

        arrayAdapter = new arrayAdapter(arr_roomList);
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText et_inDialog = new EditText(osActivity.this);
                final AlertDialog.Builder builder = new AlertDialog.Builder(osActivity.this);
                builder.setTitle("????????? ?????? ??????");
                builder.setView(et_inDialog);
                builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        now = System.currentTimeMillis();
                        t_map.put(String.valueOf(now),"");
                        reference.child("osTime").updateChildren(t_map);

                        str_room = et_inDialog.getText().toString();
                        chat c = new chat(str_room,now);

                        if(arrayAdapter.getCheckName(str_room)){
                            AlertDialog.Builder builder = new AlertDialog.Builder(osActivity.this);
                            builder.setTitle("??????!");
                            builder.setMessage("????????? ????????? ?????????????????????. ?????? ????????? ?????????.");
                            builder.setPositiveButton("??????",null);
                            builder.show();
                        }else{
                            map.put(c.getRoom(),"");
                            reference.child("From o_station to univ").updateChildren(map);
                            s.TimeStamp = String.valueOf(now);
                            s.room = str_room;
                            reference.child("os").push().setValue(s);

                            Intent intent = new Intent(getApplicationContext(),osChatActivity.class);
                            intent.putExtra("room_name",str_room);
                            intent.putExtra("user_name",str_name);
                            startActivity(intent);
                        }

                    }
                });
                builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });

        reference.orderByChild("TimeStamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ArrayList<chat> m = new ArrayList<>();

                Iterator i = snapshot.child("From o_station to univ").getChildren().iterator();
                Iterator j = snapshot.child("osTime").getChildren().iterator();
                Iterator s = snapshot.child("os").getChildren().iterator();

                while(s.hasNext()){
                    osshow show = ((DataSnapshot)s.next()).getValue(osshow.class);
                    m.add(new chat(show.room,Long.valueOf(String.valueOf((((DataSnapshot)j.next()).getKey())))));
                }

//                while(i.hasNext()){
//                    m.add(new chat(((DataSnapshot)i.next()).getKey(),Long.valueOf(String.valueOf((((DataSnapshot)j.next()).getKey())))));
//                }

                arr_roomList.clear();
                arr_roomList.addAll(m);

                recyclerView.setAdapter(arrayAdapter);

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        arrayAdapter.setOnItemClickListener(new arrayAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View v, int pos) {
                Intent intent = new Intent(getApplicationContext(),osChatActivity.class);
                intent.putExtra("room_name",arr_roomList.get(pos).getRoom());
                intent.putExtra("user_name",str_name);
                startActivity(intent);
            }
        });

    }
}