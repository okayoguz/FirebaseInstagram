package com.example.user.firebaseinstagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class WriteCommentActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    EditText editText;
    TextView textView;
    String postKey;
    String userEmail;
    String comment;
    String userImage;
    String userComment;
    Long userLikes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_comment);
        setTitle("Write Comment");

        editText = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.textView);

        postKey = getIntent().getExtras().getString("userKey");
        userEmail = getIntent().getExtras().getString("userEmail");

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

        textView.setText("Comment to " + userEmail.split("@")[0]);

    }

    public void sendComment(View view){

        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();

        userComment = editText.getText().toString();

        myRef.child("Posts").child(postKey).child("usercomments").child(uuidString).child("comment").setValue(userComment);

        Toast.makeText(getApplicationContext(), "Comment Shared", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(getApplicationContext(), CommentsActivity.class);
        intent.putExtra("userEmail", userEmail);
        intent.putExtra("userKey", postKey);
        startActivity(intent);

    }
}
