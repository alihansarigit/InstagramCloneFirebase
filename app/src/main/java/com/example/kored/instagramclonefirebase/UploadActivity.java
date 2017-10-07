package com.example.kored.instagramclonefirebase;

import android.Manifest.permission;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask.TaskSnapshot;
import io.fabric.sdk.android.Fabric;
import java.io.IOException;
import java.util.UUID;

@SuppressWarnings("VisibleForTests")
public class UploadActivity extends AppCompatActivity {

  Uri selected;
  ImageView chooseimage;
  EditText comment;
  FirebaseDatabase firebaseDatabase;
  DatabaseReference myDatabaseRef;

  private FirebaseStorage firebaseStorage;
  private StorageReference myStorageRef;
  private FirebaseAuth Auth;
  private FirebaseAuth.AuthStateListener authStateListener;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Fabric.with(this, new Crashlytics());
    setContentView(R.layout.activity_upload);
    chooseimage = (ImageView) findViewById(R.id.img_uploadimage);
    comment = (EditText) findViewById(R.id.edttext_uploadmessage);
    firebaseDatabase = FirebaseDatabase.getInstance();
    myDatabaseRef = firebaseDatabase.getReference();
    firebaseStorage = FirebaseStorage.getInstance();
    myStorageRef = firebaseStorage.getReference();
    Auth = FirebaseAuth.getInstance();
  }

  public void upload(View view) {
    if (selected != null) {
      ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(
          Context.CONNECTIVITY_SERVICE);
      NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();

      if (netInfo != null && netInfo.isConnected()) {
        UUID uuıd = UUID.randomUUID();
        String format = "images/" + uuıd + ".jpg";
        myStorageRef.child(format).putFile(selected)
            .addOnPausedListener(this,
                new OnPausedListener<TaskSnapshot>() {
                  @Override
                  public void onPaused(TaskSnapshot taskSnapshot) {
                    Toast.makeText(UploadActivity.this, "Durakladı", Toast.LENGTH_SHORT).show();
                  }
                })
            .addOnFailureListener(this, new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getLocalizedMessage().toString(),
                    Toast.LENGTH_SHORT);
              }
            })
            .addOnSuccessListener(this, new OnSuccessListener<TaskSnapshot>() {
              @Override
              public void onSuccess(TaskSnapshot taskSnapshot) {
                String dowloadURL = taskSnapshot.getDownloadUrl().toString();
                FirebaseUser user = Auth.getCurrentUser();
                String userEmail = user.getEmail();
                String userComment = comment.getText().toString();

                String random = UUID.randomUUID().toString();
                myDatabaseRef.child("Posts").child(random).child("E-Mail").setValue(userEmail);
                myDatabaseRef.child("Posts").child(random).child("Comment").setValue(userComment);
                myDatabaseRef.child("Posts").child(random).child("DowloadUrl").setValue(dowloadURL);
                Toast.makeText(UploadActivity.this, "Post Shared", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(getApplicationContext(), FeedActivity.class);
                startActivity(i);
              }
            });
      }
      else
      {
        Toast.makeText(this, "İnternetin Yok", Toast.LENGTH_SHORT).show();
      }
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == 2 && permissions.length > 0) {
      if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        Intent intent = new Intent(Intent.ACTION_PICK, Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2);
      }
    }
  }

  public void choosenimage(View view) {
    if (VERSION.SDK_INT >= VERSION_CODES.M) {
      if (checkSelfPermission(permission.READ_EXTERNAL_STORAGE)
          == PackageManager.PERMISSION_GRANTED) {
        Intent intent = new Intent(Intent.ACTION_PICK, Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2);
      } else {
        requestPermissions(new String[]{permission.READ_EXTERNAL_STORAGE}, 1);
      }
    } else {
      Intent intent = new Intent(Intent.ACTION_PICK, Media.EXTERNAL_CONTENT_URI);
      startActivityForResult(intent, 2);
    }

  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 2 && resultCode == RESULT_OK) {
      selected = data.getData();
      try {
        Bitmap bmp = Media.getBitmap(getContentResolver(), selected);
        chooseimage.setImageBitmap(bmp);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
