package com.example.kored.instagramclonefirebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;

public class FeedActivity extends AppCompatActivity implements ValueEventListener {


  ListView lst;
  ArrayList<String> username, usercomment, dowloadurl;
  DatabaseReference myDatabaseRef = FirebaseDatabase.getInstance().getReference("Posts");
  myAdapter myAdapter;
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_item_UploadAct:
        Intent i = new Intent(getApplicationContext(), UploadActivity.class);
        startActivity(i);
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Fabric.with(this, new Crashlytics());
    setContentView(R.layout.activity_feed);
    lst = (ListView) findViewById(R.id.feed_activity_lstview1);

    usercomment = new ArrayList<>();
    dowloadurl = new ArrayList();
    username= new ArrayList<>();

    myAdapter = new myAdapter(getApplicationContext(),username,usercomment,dowloadurl);
    lst.setAdapter(myAdapter);
      event();
  }
  protected  void event(){
  myDatabaseRef.addValueEventListener(this);
  }

  @Override
  public void onDataChange(DataSnapshot dataSnapshot) {
    for (DataSnapshot ds : dataSnapshot.getChildren()){
      username.add(ds.child("E-Mail").getValue().toString());
      usercomment.add(ds.child("Comment").getValue().toString());
      dowloadurl.add(ds.child("DowloadUrl").getValue().toString());
      myAdapter.notifyDataSetChanged();
    }
  }

  @Override
  public void onCancelled(DatabaseError databaseError) {

  }
}
