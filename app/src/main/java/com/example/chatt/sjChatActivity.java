package com.example.chatt;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;






public class sjChatActivity extends AppCompatActivity {

    private ListView lv_chatting;
    private EditText et_send;
    private Button btn_send;
    private TextView room_view;

    private chatAdapter chatadapter;
    private ArrayList<String> arr_room = new ArrayList<>();

    private String str_room_name;
    private String str_user_name;

    private DatabaseReference reference;
    private String key;
    private String chat_user;
    private String chat_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sjchat);

        et_send = (EditText) findViewById(R.id.et_send);
        lv_chatting = (ListView)findViewById(R.id.lv_chatting);
        room_view = (TextView)findViewById(R.id.room_name);
        btn_send = (Button)findViewById(R.id.btn_send);

        str_room_name = getIntent().getExtras().getString("room_name");
        str_user_name = getIntent().getExtras().getString("user_name");
        reference = FirebaseDatabase.getInstance().getReference().child("From univ to j_station").child(str_room_name);

        room_view.setText(str_room_name);

        chatadapter = new chatAdapter(str_user_name,"");
        lv_chatting.setAdapter(chatadapter);
        lv_chatting.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String,Object> map = new HashMap<String,Object>();
                key = reference.push().getKey();
                reference.updateChildren(map);

                DatabaseReference root = reference.child(key);

                Map<String,Object> objectMap = new HashMap<String,Object>();

                objectMap.put("name",str_user_name);
                objectMap.put("message",et_send.getText().toString());

                root.updateChildren(objectMap);

                et_send.setText("");
            }
        });

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                chatConversation(snapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                chatConversation(snapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void chatConversation(DataSnapshot dataSnapshot){
        Iterator<DataSnapshot> i = dataSnapshot.getChildren().iterator();

        while(i.hasNext()){

            chat_message = i.next().getValue().toString();
            chat_user = i.next().getValue().toString();

            chatadapter.add(chat_user,chat_message);

        }
        chatadapter.notifyDataSetChanged();

    }

}
