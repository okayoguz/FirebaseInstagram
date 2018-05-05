package com.example.user.firebaseinstagram;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ProfilActivity extends AppCompatActivity {

    ArrayList<String> userlist;
    ArrayList<String> userEmailsFromFB;
    ArrayList<String> userImageFromFB;
    ArrayList<String> userCommentFromFB;
    ArrayList<Long> userLikesFromFB;
    ArrayList<String> userKeyFromFB;
    ArrayList<Integer> commentSizeFromFB;
    ArrayList<String> count;
    String pPicture;
    String userKey;
    String userKey2;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    private StorageReference mStorageRef;
    PostClass adapter;
    GridView gridView;
    ImageView imageView;
    Uri selected;
    Button takip;
    Button mesaj;
    public String aliciuseruid;
    public String aliciuseruid2;
    public String aliciuseruid3;
    FirebaseUser chatuser = FirebaseAuth.getInstance().getCurrentUser();
    public String useremail = chatuser.getEmail();
    public String useruid = chatuser.getUid();
    public String useruid2;
    ArrayList<String> followerlist;
    ArrayList<String> followlist;
    TextView followcount;
    TextView followercount;

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
        setContentView(R.layout.activity_profil);

        userEmailsFromFB = new ArrayList<String>();
        userImageFromFB = new ArrayList<String>();
        userCommentFromFB = new ArrayList<String>();
        userLikesFromFB = new ArrayList<Long>();
        userKeyFromFB = new ArrayList<String>();
        commentSizeFromFB = new ArrayList<Integer>();

        followerlist = new ArrayList<String>();
        followlist = new ArrayList<String>();
        count = new ArrayList<String>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        gridView = (GridView) findViewById(R.id.gridView);
        imageView = (ImageView) findViewById(R.id.imageView3);
        takip = (Button) findViewById(R.id.button8);
        mesaj = (Button) findViewById(R.id.button7);
        followcount = (TextView) findViewById(R.id.textView9);
        followercount = (TextView)findViewById(R.id.textView30);
        final String useralici = getIntent().getExtras().getString("userEmail");




        getUserUid();
        getDataFromFirebase();
        getPPFromFirebase();
        getFollows();




        adapter = new PostClass(userEmailsFromFB,userImageFromFB,userCommentFromFB,userLikesFromFB, userKeyFromFB, commentSizeFromFB, this);
        gridView.setAdapter(adapter);



        mesaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), chat.class);
                intent.putExtra("alici_email", (getIntent().getExtras().getString("userEmail")));
                startActivity(intent);
            }
        });

        takip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (takip.getText().equals("Follow")) {

                    takip.setText("Unfollow");
                    final DatabaseReference reference = firebaseDatabase.getReference().child("users");

                    reference.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();

                                if (useralici.equals(hashMap.get("email"))) {
                                    aliciuseruid = ds.getKey();


                                    myRef.child("users").child(useruid).child("follows").child(aliciuseruid).child("id").setValue(aliciuseruid);
                                    myRef.child("users").child(aliciuseruid).child("follower").child(useruid).child("id").setValue(useruid);
                                }

                            }
                            reference.removeEventListener(this);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });


                } else if (takip.getText().equals("Unfollow")) {

                    takip.setText("Follow");
                    final DatabaseReference reference = firebaseDatabase.getReference().child("users");
                    reference.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();

                                if (useralici.equals(hashMap.get("email"))) {
                                    aliciuseruid2 = ds.getKey();


                                    myRef.child("users").child(useruid).child("follows").child(aliciuseruid2).child("id").removeValue();
                                    myRef.child("users").child(aliciuseruid2).child("follower").child(useruid).child("id").removeValue();

                                }

                            }
                            reference.removeEventListener(this);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }


            }
        });




    }

    public void getUserUid(){
        DatabaseReference reference2 = firebaseDatabase.getReference().child("users");
        reference2.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();
                    if (getIntent().getExtras().getString("userEmail").equals(hashMap.get("email"))) {
                        aliciuseruid3 = ds.getKey();
                        getfollowercount(aliciuseruid3);
                        getfollowcount(aliciuseruid3);


                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    protected void getFollows() {

        userlist = new ArrayList<String>();

        final DatabaseReference newReference = firebaseDatabase.getReference().child("users").child(useruid).child("follows");
        newReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();

                    String uid = hashMap.get("id");

                    final DatabaseReference reference = firebaseDatabase.getReference().child("users").child(uid).child("email");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {


                            userlist.add(dataSnapshot.getValue().toString());


                            if (userlist.contains(getIntent().getExtras().getString("userEmail"))) {
                                takip.setText("Unfollow");
                            } else {
                                takip.setText("Follow");
                            }
                            reference.removeEventListener(this);
                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
                newReference.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    protected void getDataFromFirebase(){

        DatabaseReference newReference = firebaseDatabase.getReference("Posts");
        newReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                userEmailsFromFB.clear();
                userImageFromFB.clear();
                userCommentFromFB.clear();
                userLikesFromFB.clear();
                userKeyFromFB.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()){

                    HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();
                    HashMap<String, Long> hashMap1 = (HashMap<String, Long>) ds.getValue();
                    HashMap<String, HashMap> hashMap2 = (HashMap<String, HashMap>) ds.getValue();

                    if (hashMap.get("useremail").equals(getIntent().getExtras().getString("userEmail"))) {

                        userEmailsFromFB.add(hashMap.get("useremail"));
                        userImageFromFB.add(hashMap.get("downloadurl"));
                        userCommentFromFB.add(hashMap.get("comment"));
                        userLikesFromFB.add(hashMap1.get("likes"));
                        userKeyFromFB.add(ds.getKey());
                        adapter.notifyDataSetChanged();

                        if (hashMap.get("usercomments") != null)
                            commentSizeFromFB.add(hashMap2.get("usercomments").keySet().size());
                        else
                            commentSizeFromFB.add(0);

                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    protected void getPPFromFirebase() {

        DatabaseReference newReference = firebaseDatabase.getReference("Ppictures");
        newReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();

                    if (hashMap.get("useremail").equals(getIntent().getExtras().getString("userEmail"))) {

                        pPicture = hashMap.get("downloadurl");
                        Picasso.get().load(pPicture).into(imageView);
                        userKey = ds.getKey();


                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void setfollowercount(int sayi){
        followercount.setText(String.valueOf(sayi));
    }


    public void getfollowercount(String uk){


        DatabaseReference newReference2 = firebaseDatabase.getReference().child("users").child(uk).child("follower");
        newReference2.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                followerlist.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()){

                    HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();

                    String uid =hashMap.get("id");

                    DatabaseReference reference = firebaseDatabase.getReference().child("users").child(uid).child("email");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            followerlist.add(dataSnapshot.getValue().toString());
                            setfollowercount(followerlist.size());

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



    public void setfollowcount(int sayi){
        followcount.setText(String.valueOf(sayi));
    }

    public void getfollowcount(String uk){

        DatabaseReference newReference3 = firebaseDatabase.getReference().child("users").child(uk).child("follows");
        newReference3.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                followlist.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()){

                    HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();

                    String uid =hashMap.get("id");

                    DatabaseReference reference = firebaseDatabase.getReference().child("users").child(uid).child("email");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            followlist.add(dataSnapshot.getValue().toString());
                            setfollowcount(followlist.size());


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

    public void chooseImage(View view) {

        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 2);

        }

    }

    public void changePP() {

        UUID uuidImage = UUID.randomUUID();

        String imageName = "ppictures/" + uuidImage + ".jpg";

        StorageReference storageReference = mStorageRef.child(imageName);

        storageReference.putFile(selected).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                String downloadURL = taskSnapshot.getDownloadUrl().toString();

                myRef.child("Ppictures").child(userKey).child("downloadurl").setValue(downloadURL);

                Toast.makeText(getApplicationContext(), "Successfully Changed", Toast.LENGTH_LONG).show();

                System.out.println(selected);
                System.out.println(downloadURL);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });

        //Toast.makeText(getApplicationContext(),"DONE!",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {

            selected = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selected);
                changePP();
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}
