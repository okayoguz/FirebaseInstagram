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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class search extends AppCompatActivity {

    ArrayList<String> userlist;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    ArrayAdapter adapter;
    public String ms;
    EditText username;
    Button search;

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

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        userlist = new ArrayList<String>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, userlist);

        ListView listView = (ListView) findViewById(R.id.listView3);

        listView.setAdapter(adapter);

        username = findViewById(R.id.editText);
        search = findViewById(R.id.button6);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               ms = String.valueOf(username.getText());
                getuser();


            }
        });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), ProfilActivity.class);
                intent.putExtra("userEmail",selectedItem);
                startActivity(intent);
            }
        });

    }

    protected void getuser(){

        DatabaseReference newReference = firebaseDatabase.getReference().child("users");
        newReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                userlist.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()){

                    HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();


                    if((hashMap.get("email")).contains(ms)){

                        userlist.add(hashMap.get("email"));
                    }

                    adapter.notifyDataSetChanged();

                }

                if(userlist.isEmpty()){
                    userlist.add("Kullanıcı Bulunamadı..");
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}




