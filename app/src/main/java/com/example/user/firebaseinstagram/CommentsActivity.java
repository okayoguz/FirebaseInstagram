package com.example.user.firebaseinstagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class CommentsActivity extends AppCompatActivity {

    ArrayList<String> userEmailsFromFB;
    ArrayList<String> userImageFromFB;
    ArrayList<String> userCommentFromFB;
    ArrayList<Long> userLikesFromFB;
    ArrayList<String> userKeyFromFB;
    ArrayList<String> postedCommentFromFB;
    ArrayList<Integer> commentSizeFromFB;
    String userEmail;
    String postKey;
    PostClass adapter;
    ArrayAdapter adapter2;
    FirebaseDatabase firebaseDatabase;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_post, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.add_post){

            Intent intent = new Intent(getApplicationContext(), UploadActivity.class);
            startActivity(intent);

        }

        if (item.getItemId() == R.id.mesaj){

            Intent intent = new Intent(getApplicationContext(), chatlistesi.class);
            startActivity(intent);

        }

        if (item.getItemId() == R.id.friends){

            Intent intent = new Intent(getApplicationContext(), friendlist.class);
            startActivity(intent);
        }

        if (item.getItemId() == R.id.search){

            Intent intent = new Intent(getApplicationContext(), search.class);
            startActivity(intent);
        }

        if (item.getItemId() == R.id.cikis){

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(this.getApplicationContext(), "Logged out",Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WriteCommentActivity.class);
                intent.putExtra("userEmail", userEmail);
                intent.putExtra("userKey", postKey);
                startActivity(intent);
            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();


        userEmailsFromFB = new ArrayList<String>();
        userImageFromFB = new ArrayList<String>();
        userCommentFromFB = new ArrayList<String>();
        userLikesFromFB = new ArrayList<Long>();
        userKeyFromFB = new ArrayList<String>();
        postedCommentFromFB = new ArrayList<String>();
        commentSizeFromFB = new ArrayList<Integer>();


        userEmail = getIntent().getExtras().getString("userEmail");
        postKey = getIntent().getExtras().getString("userKey");

        ListView listView = (ListView) findViewById(R.id.listView);
        ListView listView2 = (ListView) findViewById(R.id.listView2);

        getDataFromFirebase();
        getProfileFromFirebase();

        adapter = new PostClass(userEmailsFromFB,userImageFromFB,userCommentFromFB,userLikesFromFB, userKeyFromFB, commentSizeFromFB, this);
        listView.setAdapter(adapter);

        adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, postedCommentFromFB);

        listView2.setAdapter(adapter2);

    }

    protected void getDataFromFirebase(){

        DatabaseReference newReference = firebaseDatabase.getReference().child("Posts").child(postKey).child("usercomments");
        newReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.getValue() == null){
                    postedCommentFromFB.add("No comment here.");
                }else{

                    for (DataSnapshot ds : dataSnapshot.getChildren()){

                        HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();
                        postedCommentFromFB.add(hashMap.get("comment"));

                        adapter2.notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.toString(), Toast.LENGTH_LONG).show();

            }
        });

    }

    protected void getProfileFromFirebase(){

        DatabaseReference newReference = firebaseDatabase.getReference("Posts").child(postKey);
        newReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                userEmailsFromFB.clear();
                userImageFromFB.clear();
                userCommentFromFB.clear();
                userLikesFromFB.clear();
                userKeyFromFB.clear();

                HashMap<String, String> hashMap = (HashMap<String, String>) dataSnapshot.getValue();
                HashMap<String, Long> hashMap1 = (HashMap<String, Long>) dataSnapshot.getValue();
                HashMap<String, HashMap> hashMap2 = (HashMap<String, HashMap>) dataSnapshot.getValue();

                userEmailsFromFB.add(hashMap.get("useremail"));
                userImageFromFB.add(hashMap.get("downloadurl"));
                userCommentFromFB.add(hashMap.get("comment"));
                userLikesFromFB.add(hashMap1.get("likes"));
                userKeyFromFB.add(dataSnapshot.getKey());
                adapter.notifyDataSetChanged();

                if (hashMap.get("usercomments") != null)
                    commentSizeFromFB.add(hashMap2.get("usercomments").keySet().size());
                else
                    commentSizeFromFB.add(0);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(),databaseError.toString(), Toast.LENGTH_LONG).show();

            }
        });

    }

}
