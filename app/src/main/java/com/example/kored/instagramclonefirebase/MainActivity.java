package com.example.kored.instagramclonefirebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth.AuthStateListener;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements
    OnCompleteListener<AuthResult>, OnFailureListener {


  private FirebaseAuth Auth;
  private FirebaseAuth.AuthStateListener authStateListener;
  EditText edtad, edtpassword;


  Object request;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Fabric.with(this, new Crashlytics());

    setContentView(R.layout.activity_main);

    edtad = (EditText) findViewById(R.id.edt_name);
    edtpassword = (EditText) findViewById(R.id.editText2);
    Auth = FirebaseAuth.getInstance();
    authStateListener = new AuthStateListener() {
      @Override
      public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

      }
    };
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    Auth.removeAuthStateListener(authStateListener);
  }

  @Override
  protected void onStart() {
    super.onStart();
    Auth.addAuthStateListener(authStateListener);
  }

  public void signin(View view) {
    request = myRequestEnum.SignIn;
    Auth.signInWithEmailAndPassword(edtad.getText().toString(), edtpassword.getText().toString())
        .addOnCompleteListener(this)
        .addOnFailureListener(this);
  }

  public void signup(View view) {
    request = myRequestEnum.SignUp;
    Auth.createUserWithEmailAndPassword(edtad.getText().toString(),
        edtpassword.getText().toString())
        .addOnCompleteListener(this)
        .addOnFailureListener(this);
  }

  @Override
  public void onComplete(@NonNull Task<AuthResult> task) {
    if (task.isSuccessful() && request == myRequestEnum.SignIn) {
      Intent intent = new Intent(getApplicationContext(), FeedActivity.class);
      startActivity(intent);
    }
    if (task.isSuccessful() && request == myRequestEnum.SignUp) {
      Toast.makeText(this, "Kayıt Başarılı", Toast.LENGTH_SHORT).show();
    }
  }

  @Override
  public void onFailure(@NonNull Exception e) {
    Toast.makeText(MainActivity.this, e.getLocalizedMessage().toString(),
        Toast.LENGTH_SHORT).show();
  }
}
