package com.example.user.firebaseinstagram;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by user on 10.03.2018.
 */

public class PostClass extends ArrayAdapter<String> {

    private final ArrayList<String> userEmail;
    private final ArrayList<String> userImage;
    private final ArrayList<String> userComment;
    private final ArrayList<Long> userLikes;
    private final ArrayList<String> userKey;
    private final ArrayList<Integer> commentSize;
    private final Activity context;

    public PostClass(ArrayList<String> userEmail, ArrayList<String> userImage, ArrayList<String> userComment, ArrayList<Long> userLikes, ArrayList<String> userKey, ArrayList<Integer> commentSize, Activity context) {
        super(context, R.layout.custom_view,userEmail);
        this.userEmail = userEmail;
        this.userImage = userImage;
        this.userComment = userComment;
        this.userLikes = userLikes;
        this.userKey = userKey;
        this.commentSize = commentSize;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = context.getLayoutInflater();
        View customView = layoutInflater.inflate(R.layout.custom_view,null,true);

        TextView userEmailText = (TextView) customView.findViewById(R.id.username);
        TextView commentText = (TextView) customView.findViewById(R.id.commentText);
        ImageView imageView = (ImageView) customView.findViewById(R.id.imageView);
        Button likesButton = (Button) customView.findViewById(R.id.likes);
        Button comments = (Button) customView.findViewById(R.id.comments);

        userEmailText.setText(userEmail.get(position).split("@")[0]);
        commentText.setText(userComment.get(position));
        Picasso.get().load(userImage.get(position)).into(imageView);
        likesButton.setText(userLikes.get(position).toString()+" Likes");
        comments.setText(commentSize.get(position) + " Comments");

        userEmailText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(),ProfilActivity.class);
                intent.putExtra("userEmail", userEmail.get(position));
                context.startActivity(intent);

            }
        });

        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                int likes = userLikes.get(position).intValue();
                likes++;

                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference myRef = firebaseDatabase.getReference();
                myRef.child("Posts").child(userKey.get(position)).child("likes").setValue(likes);

                Toast.makeText(getContext(),"Liked", Toast.LENGTH_LONG).show();
                return false;
            }
        });

        likesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int likes = userLikes.get(position).intValue();
                likes++;

                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference myRef = firebaseDatabase.getReference();
                myRef.child("Posts").child(userKey.get(position)).child("likes").setValue(likes);

                Toast.makeText(getContext(),"Liked", Toast.LENGTH_LONG).show();

            }
        });

        comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(),CommentsActivity.class);
                intent.putExtra("userEmail", userEmail.get(position));
                intent.putExtra("userKey", userKey.get(position));

                context.startActivity(intent);

            }
        });

        return customView;
    }
}
