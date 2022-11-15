package com.example.chatt;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class chatAdapter extends BaseAdapter {

    private List<ChatMessage> chatList = new ArrayList<>();

    public chatAdapter(){}
    public chatAdapter(String name, String message){
        ChatMessage chatmessage = new ChatMessage(name,message);
        List<ChatMessage> list = new ArrayList<>();
        list.add(chatmessage);
        this.chatList = list;
    }

    public void add(String name,String message){
        ChatMessage chatmessage = new ChatMessage();
        chatmessage.setName(name);
        chatmessage.setMessage(message);
        chatList.add(chatmessage);
    }

    @Override
    public int getCount() {
        return chatList.size();
    }

    @Override
    public Object getItem(int index) {
        return chatList.get(index);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int index, View view, ViewGroup viewGroup) {

        View row = view;

        Context context = viewGroup.getContext();

        if(row==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.chatting_message,viewGroup,false);
        }

        ChatMessage msg = (ChatMessage) chatList.get(index);

        TextView nameText = (TextView) row.findViewById(R.id.name);
        TextView msgText = (TextView) row.findViewById(R.id.message);
        nameText.setText(msg.getName());
        msgText.setText(msg.getMessage());
       // msgText.setBackground(context.getResources().getDrawable(R.drawable.img));

        return row;
    }
}
