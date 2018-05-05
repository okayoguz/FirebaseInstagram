package com.example.user.firebaseinstagram;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;

    EditText emailText;
    EditText passwordText;
    ImageView imageView;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    private StorageReference mStorageRef;
    Uri selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        emailText = (EditText) findViewById(R.id.emailText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        imageView = (ImageView) findViewById(R.id.imageView2);

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };

    }

    public void signUp(View view){

        UUID uuidImage = UUID.randomUUID();

        String imageName = "ppictures/"+uuidImage+".jpg";

        StorageReference storageReference = mStorageRef.child(imageName);

        storageReference.putFile(selected).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                String downloadURL = taskSnapshot.getDownloadUrl().toString();

                String userEmail = emailText.getText().toString();

                UUID uuıd = UUID.randomUUID();
                String uuidString = uuıd.toString();

                myRef.child("Ppictures").child(uuidString).child("useremail").setValue(userEmail);
                myRef.child("Ppictures").child(uuidString).child("downloadurl").setValue(downloadURL);

                //Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });

        mAuth.createUserWithEmailAndPassword(emailText.getText().toString(), passwordText.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "User Created", Toast.LENGTH_LONG).show();

                    FirebaseDatabase firebaseDatabase;
                    DatabaseReference myRef;
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    myRef = firebaseDatabase.getReference();
                    FirebaseUser newuser = FirebaseAuth.getInstance().getCurrentUser();
                    String newuserUid = newuser.getUid();
                    myRef.child("users").child(newuserUid).child("email").setValue(newuser.getEmail());

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                if (e != null){
                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }
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

    public void chooseImage(View view){

        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[] {android.Manifest.permission.READ_EXTERNAL_STORAGE},1);

        }else{
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 2);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 2 && resultCode == RESULT_OK && data != null){

            selected = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),selected);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}
