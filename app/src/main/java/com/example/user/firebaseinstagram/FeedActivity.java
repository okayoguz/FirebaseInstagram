package com.example.user.firebaseinstagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class FeedActivity extends AppCompatActivity {

    ArrayList<String> userEmailsFromFB;
    ArrayList<String> userImageFromFB;
    ArrayList<String> userCommentFromFB;
    ArrayList<Long> userLikesFromFB;
    ArrayList<Integer> commentSizeFromFB;
    ArrayList<String> userKeyFromFB;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    PostClass adapter;
    ListView listView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_post, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.mesaj) {

            Intent intent = new Intent(getApplicationContext(), chatlistesi.class);
            startActivity(intent);
        }

        if (item.getItemId() == R.id.friends) {

            Intent intent = new Intent(getApplicationContext(), friendlist.class);
            startActivity(intent);
        }

        if (item.getItemId() == R.id.search) {

            Intent intent = new Intent(getApplicationContext(), search.class);
            startActivity(intent);
        }

        if (item.getItemId() == R.id.cikis) {

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(this.getApplicationContext(), "Logged out", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        userEmailsFromFB = new ArrayList<String>();
        userImageFromFB = new ArrayList<String>();
        userCommentFromFB = new ArrayList<String>();
        userLikesFromFB = new ArrayList<Long>();
        userKeyFromFB = new ArrayList<String>();
        commentSizeFromFB = new ArrayList<Integer>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

        listView = (ListView) findViewById(R.id.listView);

        getDataFromFirebase();

        adapter = new PostClass(userEmailsFromFB, userImageFromFB, userCommentFromFB, userLikesFromFB, userKeyFromFB, commentSizeFromFB, this);
        listView.setAdapter(adapter);

        System.out.println(commentSizeFromFB);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UploadActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void getDataFromFirebase() {

        DatabaseReference newReference = firebaseDatabase.getReference("Posts");
        newReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                userEmailsFromFB.clear();
                userImageFromFB.clear();
                userCommentFromFB.clear();
                userLikesFromFB.clear();
                userKeyFromFB.clear();
                commentSizeFromFB.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();
                    HashMap<String, Long> hashMap1 = (HashMap<String, Long>) ds.getValue();
                    HashMap<String, HashMap> hashMap2 = (HashMap<String, HashMap>) ds.getValue();

                    userEmailsFromFB.add(hashMap.get("useremail"));
                    userImageFromFB.add(hashMap.get("downloadurl"));
                    userCommentFromFB.add(hashMap.get("comment"));
                    userLikesFromFB.add(hashMap1.get("likes"));
                    userKeyFromFB.add(ds.getKey());

                    if (hashMap.get("usercomments") != null)
                        commentSizeFromFB.add(hashMap2.get("usercomments").keySet().size());
                    else
                        commentSizeFromFB.add(0);

                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
