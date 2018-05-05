package com.example.user.firebaseinstagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class chat extends AppCompatActivity {

    ArrayList<String> mesaj;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    //ArrayAdapter adapter;
    ListView listView;
    EditText gidecekmesaj;
    Button gönder;
    PostClass2 adapter2;
    ArrayList<String> chatgonderici;
    ArrayList<String> chatalici;
    ArrayList<String> chatmesaj;
    FirebaseUser chatuser = FirebaseAuth.getInstance().getCurrentUser();
    public String chatuserUid = chatuser.getUid();
    public String aliciuseruid;
    public String useralici;

    private void writenewmesaj(String mesajtext,String gonderici,String alici){

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:dd");
        String zaman = sdf.format(new Date());

        Message yenimesaj = new Message(mesajtext,gonderici,alici,zaman);
        myRef.child("messages").child(chatuserUid).child(alici).push().setValue(yenimesaj);
        myRef.child("messages").child(alici).child(chatuserUid).push().setValue(yenimesaj);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_post, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.add_post) {

            Intent intent = new Intent(getApplicationContext(), UploadActivity.class);
            startActivity(intent);

        }

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
            Toast.makeText(this.getApplicationContext(), "Logged out", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);

        Bundle extras = getIntent().getExtras();
        useralici = extras.getString("alici_email");

        chatgonderici = new ArrayList<String>();
        chatalici = new ArrayList<String>();
        chatmesaj = new ArrayList<String>();

        adapter2 = new PostClass2(chatgonderici,chatalici,chatmesaj,this);

        //mesaj = new ArrayList<String>();
        gidecekmesaj = findViewById(R.id.editText2);
        gönder = findViewById(R.id.button5);

        gönder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ms = String.valueOf(gidecekmesaj.getText());
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String userEmail = user.getEmail().toString();
                writenewmesaj(ms,userEmail,aliciuseruid);

            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

        //adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, mesaj);

        ListView listView = (ListView) findViewById(R.id.listView);

        listView.setAdapter(adapter2);


        DatabaseReference reference = firebaseDatabase.getReference().child("users");
        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()){

                    HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();

                    if(useralici.equals(hashMap.get("email"))){
                        aliciuseruid = ds.getKey();
                        getDataFromFirebase(aliciuseruid);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    protected void getDataFromFirebase(String s){

        DatabaseReference newReference = firebaseDatabase.getReference("messages").child(chatuserUid).child(s);
        newReference.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                chatgonderici.clear();
                chatalici.clear();
                chatmesaj.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()){

                    HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();

                    chatgonderici.add(hashMap.get("gonderici"));
                    chatalici.add(hashMap.get("zaman"));
                    chatmesaj.add(hashMap.get("mesajText"));
                        adapter2.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}

