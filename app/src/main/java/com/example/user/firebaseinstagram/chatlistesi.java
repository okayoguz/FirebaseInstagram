package com.example.user.firebaseinstagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class chatlistesi extends AppCompatActivity {

    ArrayList<String> userlist;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    ArrayAdapter adapter;
    ListView listView;
    FirebaseUser chatuser = FirebaseAuth.getInstance().getCurrentUser();
    public String useremail = chatuser.getEmail();
    public String useruid = chatuser.getUid();

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

            Intent intent = new Intent(getApplicationContext(), chat.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_listesi);

        userlist = new ArrayList<String>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, userlist);

        ListView listView = (ListView) findViewById(R.id.gridView);

        listView.setAdapter(adapter);

        getDataFromFirebase();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), chat.class);
                intent.putExtra("alici_email",selectedItem);
                startActivity(intent);
            }
        });

    }

    protected void getDataFromFirebase(){

        DatabaseReference newReference = firebaseDatabase.getReference().child("messages").child(useruid);
        newReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()){

                    String uid =ds.getKey();

                    DatabaseReference reference = firebaseDatabase.getReference().child("users").child(uid).child("email");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            userlist.add(dataSnapshot.getValue().toString());
                            adapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}


