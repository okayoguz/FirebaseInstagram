package com.example.user.firebaseinstagram;

import android.Manifest;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class UploadActivity extends AppCompatActivity {

    EditText commentText;
    ImageView imageView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    private FirebaseDatabase mAuth;
    private StorageReference mStorageRef;
    Uri selected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        commentText = (EditText) findViewById(R.id.commentText);
        imageView = (ImageView) findViewById(R.id.imageView);
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();
        mAuth = firebaseDatabase.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();


    }

    public void upload(View view){

        UUID uuidImage = UUID.randomUUID();

        String imageName = "images/"+uuidImage+".jpg";

        StorageReference storageReference = mStorageRef.child(imageName);

        storageReference.putFile(selected).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                String downloadURL = taskSnapshot.getDownloadUrl().toString();

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String userEmail = user.getEmail().toString();
                String userComment = commentText.getText().toString();
                int likes = 0;

                UUID uuid = UUID.randomUUID();
                String uuidString = uuid.toString();

                myRef.child("Posts").child(uuidString).child("useremail").setValue(userEmail);
                myRef.child("Posts").child(uuidString).child("comment").setValue(userComment);
                myRef.child("Posts").child(uuidString).child("downloadurl").setValue(downloadURL);
                myRef.child("Posts").child(uuidString).child("likes").setValue(likes);

                Toast.makeText(getApplicationContext(), "Post Shared", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), FeedActivity.class);
                startActivity(intent);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void chooseImage(View view){

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},1);

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
