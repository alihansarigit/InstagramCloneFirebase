package com.example.kored.instagramclonefirebase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

/**
 * Created by kored on 4.10.2017.
 */

public class myAdapter extends BaseAdapter {

  ArrayList<String> userName = new ArrayList<>();
  ArrayList<String> userComment = new ArrayList<>();
  ArrayList<String> userDowloadUrl = new ArrayList<>();

  Context ctc;

  public myAdapter(Context context,ArrayList<String> username,ArrayList<String> usercoment,ArrayList<String> userdowloadurl){
    ctc = context;
    userName = username;
    userComment = usercoment;
    userDowloadUrl = userdowloadurl;
  }
  @Override
  public int getCount() {
    return userName.size();
  }

  @Override
  public Object getItem(int i) {
    return userName.get(i);
  }

  @Override
  public long getItemId(int i) {
    return 0;
  }

  @Override
  public View getView(int i, View view, ViewGroup viewGroup) {
    view  = LayoutInflater.from(ctc).inflate(R.layout.custom_list_view,viewGroup,false);

    TextView txtuser = view.findViewById(R.id.custom_txt_isim);
    TextView txtcomment = view.findViewById(R.id.custom_comment);
    ImageView imgview = view.findViewById(R.id.custom_img_post);

    Picasso.with(ctc).load(userDowloadUrl.get(i)).into(imgview);
    txtuser.setText(userName.get(i));
    txtcomment.setText(userComment.get(i));
    return view;
  }
}
