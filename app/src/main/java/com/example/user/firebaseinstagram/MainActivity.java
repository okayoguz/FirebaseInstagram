package com.example.user.firebaseinstagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;

    EditText emailText;
    EditText passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailText = (EditText) findViewById(R.id.emailText);
        passwordText = (EditText) findViewById(R.id.passwordText);

        mAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };

    }

    public void signIn(View view){

        mAuth.signInWithEmailAndPassword(emailText.getText().toString(), passwordText.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    Intent intent = new Intent(getApplicationContext(), FeedActivity.class);
                    startActivity(intent);
                    finish();



                }

            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext(), e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mAuthStateListener != null){
            mAuth.removeAuthStateListener(mAuthStateListener);
        }

    }

    public void goSignUp (View view){


        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
        startActivity(intent);

    }

}
